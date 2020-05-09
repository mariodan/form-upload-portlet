package ro.cjarges.formupload.portlet;


import com.liferay.util.bridges.mvc.MVCPortlet;
import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.log4j.Logger;

import ro.cjarges.formupload.dao.FormUploadDAO;
import ro.cjarges.formupload.model.FileModel;
import ro.cjarges.formupload.model.FormUploadModel;
import ro.cjarges.formupload.util.FormularUploadUtil;

/**
 * Portlet implementation class SesizariMVCPortlet
 */
public class FormularUploadPortlet extends MVCPortlet {
	
	private static Logger logger = Logger.getLogger(FormularUploadPortlet.class);
	private String realPath;
	
	
	//Default Render Method.  
	public void doView(RenderRequest renderRequest,	RenderResponse renderResponse) throws PortletException, IOException {
		//Render Logic.
		super.doView(renderRequest, renderResponse);
	}
	
	/**
	 * Method that saves the data posted by form
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void uploadForm(ActionRequest request, ActionResponse response) throws Exception {

		ArrayList<String> validationErrorFields = new ArrayList<String>();
		Map<String, String> fieldsMap = new LinkedHashMap<String, String>();

		
		/* get portlet preferences */
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);

		String portletId = (String)request.getAttribute(WebKeys.PORTLET_ID);

		PortletPreferences preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletId);

		boolean requireCaptcha = GetterUtil.getBoolean(preferences.getValue("requireCaptcha", StringPool.BLANK));
		String successURL = GetterUtil.getString(preferences.getValue("successURL", StringPool.BLANK));
		boolean sendAsEmail = GetterUtil.getBoolean(preferences.getValue("sendAsEmail", StringPool.BLANK));
		boolean saveToDatabase = GetterUtil.getBoolean(preferences.getValue("saveToDatabase", StringPool.BLANK));
		String databaseTableName = GetterUtil.getString(preferences.getValue("databaseTableName", StringPool.BLANK));
		boolean saveToFile = GetterUtil.getBoolean(preferences.getValue("saveToFile", StringPool.BLANK));
		String fileName = GetterUtil.getString(preferences.getValue("fileName", StringPool.BLANK));
		
		
		if (requireCaptcha) {
			try {
				CaptchaUtil.check(request);
			} catch (CaptchaTextException cte) {
				SessionErrors.add(request, CaptchaTextException.class.getName());
				return;
			}
		}
		

		fieldsMap.put("nume", request.getParameter("nume"));
		fieldsMap.put("email", request.getParameter("email"));
		fieldsMap.put("prenume", request.getParameter("prenume"));
		fieldsMap.put("telefon", request.getParameter("telefon"));
		fieldsMap.put("email", request.getParameter("email"));
		
		
		/**
		 * Manage file upload
		 */
		realPath = getPortletContext().getRealPath("/");
		byte[] bytes = null;
		PortletContext portletContext = request.getPortletSession().getPortletContext();
		try { 
			
		    UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(request);
		    String sourceFileName = uploadRequest.getFileName("fileName");
		    
		    logger.info("Filename: " + sourceFileName);
		    
			File file = uploadRequest.getFile("fileName");
			try {
				bytes = FileUtil.getBytes(file);
			} catch (IOException e2) {			
				e2.printStackTrace();
			}
			
			File newFile=null;
			
			if ((bytes != null) && (bytes.length > 0)) {
			
				try {
					newFile = new File(realPath + "cjarges/formupload/"+ sourceFileName);
					FileInputStream fileInputStream = new FileInputStream(file);
					FileOutputStream fileOutputStream = new FileOutputStream(newFile);			
					fileInputStream.read(bytes);				
					fileOutputStream.write(bytes, 0, bytes.length);					
					fileOutputStream.close();
					fileInputStream.close();
				} 
				catch (FileNotFoundException e) {
					System.out.println("File Not Found.");				
					e.printStackTrace();
				}
				catch (IOException e1){
					System.out.println("Error Reading The File.");
					e1.printStackTrace();
				}
			}
	        
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		// end file upload
		
		
		/* check required fields */
		for(Map.Entry<String, String> entry: fieldsMap.entrySet()) {
			String key 		= entry.getKey();
			String value 	= entry.getValue();
			
			/* fill all fields with their submitted values */
			
			/* Pass values back to response -> available in ${nume} */
			request.setAttribute(key, value);
			
			/* Validate Phone */
			if(key.equals("telefon") && value.length()>=10) {
				if(FormularUploadUtil.validatePhoneNumber(value)) {
					continue;
				} else {
					SessionErrors.add(request, key+"-invalid");
					response.setRenderParameter("error-"+key, "has-error has-feedback");
					validationErrorFields.add(key);
				}
			}
			
			/* Validate Email address */
			if(key.equals("email") && value.length()>=4 && value.length()<=128) {
				if(FormularUploadUtil.validateEmailAddress(value)) {
					continue;
				} else {
					SessionErrors.add(request, key+"-invalid");
					response.setRenderParameter("error-"+key, "has-error has-feedback");
					validationErrorFields.add(key);
				}
			}
			
			if(Validator.isNull(value) || value.length()<=1 || value.equalsIgnoreCase("")) {
				
				validationErrorFields.add(key);
				
				/* available in <liferay-ui:error key="error" */
				SessionErrors.add(request, key+"-required");
				
				response.setRenderParameter("error-"+key, "has-error has-feedback");
				response.setRenderParameter("error-"+key+"-span", "glyphicon glyphicon-remove form-control-feedback");
			}
		}
		

		/* final phase: check and sendEmail/saveDatabase/SaveFile */
		
		if(validationErrorFields.size()==0) {
			boolean emailSuccess = true;
			boolean databaseSuccess = true;
			boolean fileSuccess = true;

			if (sendAsEmail) {
				emailSuccess = sendEmail(themeDisplay.getCompanyId(), fieldsMap, preferences);
				logger.info("[ sendEMAIL ]: emailSuccess:" + emailSuccess);
			}

			if (saveToDatabase) {
				if (Validator.isNull(databaseTableName)) {
					databaseTableName = FormularUploadUtil.getNewDatabaseTableName(portletId);

					preferences.setValue("databaseTableName", databaseTableName);
					preferences.store();
				}

				databaseSuccess = saveDatabase(themeDisplay.getCompanyId(), fieldsMap, preferences,databaseTableName);
				
				logger.info("[ saveDatabase ]: databaseSuccess:"+databaseSuccess);
			}

			if (saveToFile) {
				fileSuccess = saveFile(fieldsMap, fileName);
				logger.info("[ saveFile ]: fileSuccess:"+fileSuccess);
			}

			if (emailSuccess && databaseSuccess && fileSuccess) {
				SessionMessages.add(request, "success");
				logger.info("[ SUCCESS ]: emailSuccess / databaseSuccess / fileSuccess");
			}
			else {
				SessionErrors.add(request, "error");
				logger.info("[ ERROR ]: emailSuccess="+emailSuccess+" || databaseSuccess="+databaseSuccess+" || fileSuccess="+fileSuccess);
			}
			
		}
		
		
		/* SUCCESS / ERROR phase redirect */
		/* daca nu avem erori facem clean */
		if (SessionErrors.size(request)==0) {
		
			SessionErrors.clear(request);
			 
			/* disable Success message on next Page. We'll display this at the end */
			SessionMessages.add(request, "request_processed", "Formularul a fost trimis cu succes!");
			SessionMessages.add(request, "success", "Formularul a fost trimis cu succes!");
			
			//actionResponse.setRenderParameter("jspPage", getInitParameter("page2-jsp"));
			logger.info("Sesizari form completed successfully!");
			response.setRenderParameter("jspPage","/success.jsp"); 
			
		} else {			 
			SessionErrors.add(request, "error");
			 
			/* disabling custom error message - doesn't work*/
			PortletConfig portletConfig= (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages. KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			 
			logger.info(FormularUploadPortlet.class.getName()+" : Eroare la completarea formularului");
		}
	}
	
	
	/**
	 * Save to database
	 * 
	 * @param companyId
	 * @param fieldsMap
	 * @param preferences
	 * @param databaseTableName
	 * @return
	 * @throws Exception
	 */
	protected boolean saveDatabase(
			long companyId, 
			Map<String, String> fieldsMap, 
			PortletPreferences preferences, 
			String databaseTableName) throws Exception {

		FormularUploadUtil.checkTable(companyId, databaseTableName, preferences);

		long classPK = CounterLocalServiceUtil.increment(FormularUploadPortlet.class.getName());

		/* create sesizare object */
		FormUploadModel formUploadModel = FormularUploadUtil.getFormUploadFieldsDTO(fieldsMap);
		
		FileModel fileModel = new FileModel();
		fileModel.setNumeFisier("kaka.txt");
		fileModel.setPath("/cjarges/portlets");
		fileModel.setSize("1234");
		
		List<FileModel> files = new ArrayList<FileModel>();
		files.add(fileModel);
		
		formUploadModel.setFiles(files);
		
		/* Save to SQL Database */
		FormUploadDAO.addFormUpload(formUploadModel);
		
		return true;
	}
	
	/**
	 * Generate Email Body
	 * 
	 * @param fieldsMap
	 * @return
	 */
	protected String getMailBody(Map<String, String> fieldsMap) {
		StringBuilder sb = new StringBuilder();

		for (String fieldLabel : fieldsMap.keySet()) {
			String fieldValue = fieldsMap.get(fieldLabel);

			sb.append(fieldLabel);
			sb.append(" : ");
			sb.append(fieldValue);
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Save to file
	 * 
	 * @param fieldsMap
	 * @param fileName
	 * @return
	 */
	protected boolean saveFile(Map<String, String> fieldsMap, String fileName) {

		// Save the file as a standard Excel CSV format. Use ; as a delimiter,
		// quote each entry with double quotes, and escape double quotes in
		// values a two double quotes.

		StringBuilder sb = new StringBuilder();

		for (String fieldLabel : fieldsMap.keySet()) {
			String fieldValue = fieldsMap.get(fieldLabel);

			sb.append("\"");
			sb.append(StringUtil.replace(fieldValue, "\"", "\"\""));
			sb.append("\";");
		}

		String s = sb.substring(0, sb.length() - 1) + "\n";

		try {
			FileUtil.write(fileName, s, false, true);

			return true;
		}
		catch (Exception e) {
			logger.error("The web form data could not be saved to a file", e);

			return false;
		}
	}

	
	/**
	 * Send email
	 * 
	 * @param companyId
	 * @param fieldsMap
	 * @param preferences
	 * @return
	 */
	protected boolean sendEmail(
		long companyId, Map<String, String> fieldsMap,
		PortletPreferences preferences) {

		try {
			String emailAddresses = preferences.getValue("emailAddress", StringPool.BLANK);

			if (Validator.isNull(emailAddresses)) {
				logger.error("The web form email cannot be sent because no email address is configured");
				return false;
			}

			InternetAddress fromAddress = new InternetAddress(
				FormularUploadUtil.getEmailFromAddress(preferences, companyId),
				FormularUploadUtil.getEmailFromName(preferences, companyId));
			String subject = preferences.getValue("subject", StringPool.BLANK);
			String body = getMailBody(fieldsMap);

			MailMessage mailMessage = new MailMessage(fromAddress, subject, body, false);
			InternetAddress[] toAddresses = InternetAddress.parse(emailAddresses);
			mailMessage.setTo(toAddresses);
			MailServiceUtil.sendEmail(mailMessage);

			return true;
		}
		catch (Exception e) {
			logger.error("The web form email could not be sent", e);
			return false;
		}
	}

	@Override
	public void serveResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);
		
		logger.info("CMD:"+cmd);
		
		try {
			if (cmd.equals("captcha")) {
				serveCaptcha(resourceRequest, resourceResponse);
			}
			else if (cmd.equals("export")) {
				exportData(resourceRequest, resourceResponse);
			}
		}
		catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	
	/**
	 * Export CSV Data
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @throws Exception
	 */
	protected void exportData(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {

	
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

		String portletId = PortalUtil.getPortletId(resourceRequest);
		PortletPermissionUtil.check(themeDisplay.getPermissionChecker(), themeDisplay.getPlid(), portletId, ActionKeys.CONFIGURATION);
		PortletPreferences preferences = PortletPreferencesFactoryUtil.getPortletSetup(resourceRequest);
		
		String title = preferences.getValue("title", "no-title");

		logger.info("EXPORT-DATA:"+title);
		
		StringBuilder sb = new StringBuilder();
		//List<Sesizare> sesizari = SesizareDAO.getSesizari();

		List<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Nume");
		headers.add("Prenume");
		headers.add("Auditor");
		headers.add("Email");
		headers.add("Telefon");
		headers.add("Tip");
		headers.add("Subiect");
		headers.add("Descriere");
		headers.add("Data audienta");
		headers.add("Ora");
		headers.add("Data creare");
		
		String headerCSV = headers.toString();
		headerCSV.replaceAll("\\[", "");
		headerCSV.replaceAll("\\]", "");
		headerCSV.replace("[", "");
		headerCSV.replace("]","");
		
		
		sb.append(headerCSV);
		sb.append(StringPool.NEW_LINE);
		
		/*
		for (Sesizare sesizare: sesizari) {
			
			sb.append("\"");
			sb.append(sesizare.getSesizareId());
			sb.append("\"");
			sb.append(StringPool.COMMA);
						
			sb.append("\"");
			sb.append(sesizare.getNumeCetatean().replaceAll("\"", "\\\""));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			sb.append("\"");
			sb.append(sesizare.getPrenumeCetatean().replaceAll("\"", "\\\""));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
						
			sb.append("\"");
			sb.append(sesizare.getEmailCetatean().replaceAll("\"", "\\\""));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			sb.append("\"");
			sb.append(sesizare.getTelefonCetatean().replaceAll("\"", "\\\""));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			sb.append("\"");
			sb.append(sesizare.getTipSesizare().replaceAll("\"", "\\\""));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			sb.append("\"");
			sb.append(sesizare.getSubiectSesizare().replaceAll("\"", "\\\""));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			sb.append("\"");
			sb.append(sesizare.getMotiveSesizare().replaceAll("\"", "\\\""));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			sb.append("\"");
			sb.append(sesizare.getDataSesizare());
			sb.append("\"");
			sb.append(StringPool.COMMA);
		
			sb.append("\"");
			sb.append(sesizare.getAddedDate());
			sb.append("\"");
			
			sb.append(StringPool.NEW_LINE);
			
		}
		*/

		String fileName = title + ".csv";
		byte[] bytes = sb.toString().getBytes();
		String contentType = ContentTypes.APPLICATION_TEXT;

		PortletResponseUtil.sendFile(resourceRequest, resourceResponse, fileName, bytes, contentType);
	}
	
	
	protected void serveCaptcha(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
		CaptchaUtil.serveImage(resourceRequest, resourceResponse);
	}
	
}
