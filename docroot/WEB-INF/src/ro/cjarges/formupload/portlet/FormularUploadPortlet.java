package ro.cjarges.formupload.portlet;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ro.cjarges.formupload.dao.FormUploadDAO;
import ro.cjarges.formupload.model.FileModel;
import ro.cjarges.formupload.model.FormUploadModel;
import ro.cjarges.formupload.model.WrapperFormUploadModel;
import ro.cjarges.formupload.util.FormularUploadUtil;
import ro.cjarges.formupload.util.PortletPropsValues;

import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.mail.service.MailServiceUtil;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class FormularUploadPortlet
 */
public class FormularUploadPortlet extends MVCPortlet {
	
	private static Logger logger = Logger.getLogger(FormularUploadPortlet.class);
	private String realPath;
	private boolean fileUploadSuccess;
	
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

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		String portletId = (String)request.getAttribute(WebKeys.PORTLET_ID);
		PortletPreferences preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletId);

		boolean sendAsEmail = GetterUtil.getBoolean(preferences.getValue("sendAsEmail", StringPool.BLANK));
		boolean saveToDatabase = GetterUtil.getBoolean(preferences.getValue("saveToDatabase", StringPool.BLANK));
		boolean saveToFile = GetterUtil.getBoolean(preferences.getValue("saveToFile", StringPool.BLANK));
		
		String databaseTableName = GetterUtil.getString(preferences.getValue("databaseTableName", StringPool.BLANK));
		String successURL = GetterUtil.getString(preferences.getValue("successURL", StringPool.BLANK));
		
		
		/* Fetch required fields from configuration */
		Map<String, Boolean> requiredFields = new HashMap<String, Boolean>();
		
		requiredFields.put("nume", GetterUtil.getBoolean(preferences.getValue("nume", StringPool.BLANK)));
		requiredFields.put("prenume", GetterUtil.getBoolean(preferences.getValue("prenume", StringPool.BLANK)));
		requiredFields.put("email", GetterUtil.getBoolean(preferences.getValue("email", StringPool.BLANK)));
		requiredFields.put("telefon", GetterUtil.getBoolean(preferences.getValue("telefon", StringPool.BLANK)));
		requiredFields.put("file", GetterUtil.getBoolean(preferences.getValue("file", StringPool.BLANK)));
		

		/**
		 * Get fields from request
		 * if multipart/form-data
		 */
		fieldsMap.put("nume", request.getParameter("nume"));
		fieldsMap.put("email", request.getParameter("email"));
		fieldsMap.put("prenume", request.getParameter("prenume"));
		fieldsMap.put("telefon", request.getParameter("telefon"));
		fieldsMap.put("email", request.getParameter("email"));
		fieldsMap.put("file", request.getParameter("file"));
		
		
		PortletContext portletContext = request.getPortletSession().getPortletContext();
		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(request);
		File file = uploadRequest.getFile("file");
		String sourceFileName = uploadRequest.getFileName("file");
	    
		if (uploadRequest != null && uploadRequest.getParameter("nume") != null) {
			fieldsMap.put("nume", uploadRequest.getParameter("nume"));
			fieldsMap.put("email", uploadRequest.getParameter("email"));
			fieldsMap.put("prenume", uploadRequest.getParameter("prenume"));
			fieldsMap.put("telefon", uploadRequest.getParameter("telefon"));
			fieldsMap.put("email", uploadRequest.getParameter("email"));
			fieldsMap.put("file", getFileNameWithMillies(sourceFileName));
			fieldsMap.put("fileSize", String.valueOf(file.length()));
			//logger.info("Before processing files tempFile: " + file.getName() + ", real: " + sourceFileName + ", size: " + String.valueOf(file.length()));
		}
		
		
		/**
		 * Validate form fields
		 */
		for(Map.Entry<String, String> entry: fieldsMap.entrySet()) {
			String key 		= entry.getKey();
			String value 	= entry.getValue();
			
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
			
			/**
			 * Validate file
			 */
			if (key.equals("file")) {
				if(FormularUploadUtil.validateFileExtension(sourceFileName)) {
					continue;
				} else {
					logger.warn("File upload error: " + key + "-invalid-extension");
					
					SessionErrors.add(request, key+"-invalid-extension");
					response.setRenderParameter("error-"+key, "has-error has-feedback");
					validationErrorFields.add(key);
				}
				
				if(FormularUploadUtil.validateFileNameLength(file)) {
					continue;
				} else {
					logger.warn("File upload error: " + key + "-invalid-name");
					
					SessionErrors.add(request, key+"-invalid-name");
					response.setRenderParameter("error-"+key, "has-error has-feedback");
					validationErrorFields.add(key);
				}
				
				if(FormularUploadUtil.validateFileSize(file)) {
					continue;
				} else {
					logger.warn("File upload error: " + key + "-invalid-size");
					
					SessionErrors.add(request, key+"-invalid-size");
					response.setRenderParameter("error-"+key, "has-error has-feedback");
					validationErrorFields.add(key);
				}
			}
			
			/* Generic validation fields */
			if(requiredFields.containsKey(key) && requiredFields.get(key) && (Validator.isNull(value) || value.equalsIgnoreCase(""))) {
				validationErrorFields.add(key);
				SessionErrors.add(request, key+"-invalid");
				response.setRenderParameter("error-"+key, "has-error has-feedback");
				response.setRenderParameter("error-"+key+"-span", "glyphicon glyphicon-remove form-control-feedback");
			}
			
			/* Pass values back to response -> available in ${nume} */
			request.setAttribute(key, value);
		}
		
		/**
		 * Process file upload
		 * Stores file in a directory
		 */
		processFileUpload(fieldsMap, validationErrorFields, uploadRequest, response, request);
		

		/* final phase: check and sendEmail/saveDatabase/SaveFile */
		if(validationErrorFields.size()==0) {
			boolean emailSuccess = false;
			boolean databaseSuccess = false;
			
			if (sendAsEmail && fileUploadSuccess) {
				emailSuccess = sendEmail(themeDisplay.getCompanyId(), fieldsMap, preferences);
				logger.info("[ sendEmail ]: " + emailSuccess);
			}

			if (saveToDatabase) {
				if (Validator.isNull(databaseTableName)) {
					databaseTableName = FormularUploadUtil.getNewDatabaseTableName(portletId);

					preferences.setValue("databaseTableName", databaseTableName);
					preferences.store();
				}
				
				databaseSuccess = saveDatabase(themeDisplay.getCompanyId(), fieldsMap, preferences,databaseTableName);
				logger.info("[ saveDatabase ]: "+databaseSuccess);
			}

			if (emailSuccess && databaseSuccess) {
				SessionMessages.add(request, "success");
				logger.info("[ SUCCESS ]: emailSuccess / databaseSuccess");
			} else {
				SessionErrors.add(request, "error");
				logger.info("[ ERROR ]: emailSuccess="+emailSuccess+" || databaseSuccess="+databaseSuccess);
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
			logger.info("FormUpload completed successfully!");
			response.setRenderParameter("jspPage","/success.jsp"); 
			
		} else {			 
			SessionErrors.add(request, "error");
			 
			/* disabling custom error message - doesn't work*/
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages. KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			 
			logger.info("Eroare la completarea formularului");
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

		/* create sesizare object */
		FormUploadModel formUploadModel = FormularUploadUtil.getFormUploadFieldsDTO(fieldsMap);
		
		FileModel fileModel = new FileModel();
		fileModel.setNumeFisier(fieldsMap.get("file"));
		fileModel.setPath("/cjarges/portlets/" + fieldsMap.get("file"));
		fileModel.setSize(fieldsMap.get("fileSize"));
		
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

		String emailContent = loadEmailBody();
		
		for (String fieldLabel : fieldsMap.keySet()) {
			String fieldValue = fieldsMap.get(fieldLabel);
			
			/**
			 * in multipart/form-data some keys are null
			 */
			if (fieldLabel == null) {
				continue;
			}

			sb.append(fieldLabel);
			sb.append(" : ");
			sb.append(fieldValue);
			sb.append("\n");
			
			if (fieldLabel.equals("nume")) {
				emailContent = emailContent.replaceFirst("_NUME_", fieldValue);
			}
			if (fieldLabel.equals("prenume")) {
				emailContent = emailContent.replaceFirst("_PRENUME_", fieldValue);
			}
			if (fieldLabel.equals("email")) {
				emailContent = emailContent.replaceFirst("_EMAIL_", fieldValue);
			}
			if (fieldLabel.equals("telefon")) {
				emailContent = emailContent.replaceFirst("_TELEFON_", fieldValue);
			}
			if (fieldLabel.equals("file")) {
				emailContent = emailContent.replaceFirst("_NUME_FISIER_", fieldValue);
			}
			if (fieldLabel.equals("fileSize")) {
				emailContent = emailContent.replaceFirst("_FILE_SIZE_", getFormattedFileSize(Long.valueOf(fieldValue)));
			}
		}
		
		emailContent = emailContent.replaceFirst("_CREATED_AT_", getCurrentFormattedDate());

		return emailContent != null ? emailContent : sb.toString();
	}
	
	
	public String getCurrentFormattedDate() {
		TimeZone timeZone = TimeZone.getTimeZone("Europe/Athens");
		Calendar cal = Calendar.getInstance(timeZone);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		sdf.setTimeZone(cal.getTimeZone());
		return sdf.format(cal.getTime());
	}

	/**
	 * Send email
	 * 
	 * @param companyId
	 * @param fieldsMap
	 * @param preferences
	 * @return
	 */
	protected boolean sendEmail(long companyId, Map<String, String> fieldsMap, PortletPreferences preferences) {

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
			
			String formTitle = preferences.getValue("title", StringPool.BLANK);
			logger.info("Form title: " + formTitle);
			
			String body = getMailBody(fieldsMap);
			body = body.replaceFirst("_TITLU_", formTitle);
			
			String fileUrl = PortletPropsValues.FILE_DOWNLOAD_URL;
			//String fileDownloadDir = PortletPropsValues.FILE_DOWNLOAD_DIR;
			
			body = body.replaceFirst("_LINK_FISIER_", fileUrl + fieldsMap.get("file"));
			
			if (body == null) {
				logger.error("Cannot send email, body is null!");
				return false;
			}

			MailMessage mailMessage = new MailMessage(fromAddress, subject, body, true);
			
			InternetAddress[] toAddresses = InternetAddress.parse(emailAddresses);
			mailMessage.setTo(toAddresses);
			
			MailServiceUtil.sendEmail(mailMessage);

			return true;
		} catch (Exception e) {
			logger.error("The web form email could not be sent", e);
			return false;
		}
	}

	@Override
	public void serveResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);
		
		try {
			if (cmd.equals("captcha")) {
				serveCaptcha(resourceRequest, resourceResponse);
			} else if (cmd.equals("export")) {
				exportData(resourceRequest, resourceResponse);
			}
		} catch (Exception e) {
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

		logger.info("EXPORT-DATA:" + title);
		
		List<WrapperFormUploadModel> formUploads = FormUploadDAO.getLiferayContainerFormUploads();
		
		StringBuilder sb = new StringBuilder();

		List<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Nume");
		headers.add("Prenume");
		headers.add("Telefon");
		headers.add("Email");
		headers.add("Fisier");
		headers.add("Dimensiune Fisier");
		headers.add("Data creare");
		
		String headerCSV = headers.toString();
		headerCSV.replaceAll("\\[", "");
		headerCSV.replaceAll("\\]", "");
		headerCSV.replace("[", "");
		headerCSV.replace("]", "");
		
		
		sb.append(headerCSV);
		sb.append(StringPool.NEW_LINE);
		
		for (WrapperFormUploadModel formUpload: formUploads) {
			
			/* ID */
			sb.append("\"");
			sb.append(formUpload.getId());
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			/* Nume */
			sb.append("\"");
			sb.append(formUpload.getNume());
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			/* Prenume */
			sb.append("\"");
			sb.append(formUpload.getPrenume());
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			/* Telefon */
			sb.append("\"");
			sb.append(formUpload.getTelefon());
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			/* Email */
			sb.append("\"");
			sb.append(formUpload.getEmail());
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			
			/* Nume Fisier */
			
			sb.append("\"");
			sb.append(formUpload.getFileName());
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			/* Size fisier*/
			sb.append("\"");
			sb.append(formUpload.getFileSize());
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			/* Data fisier*/
			sb.append("\"");
			sb.append(FormularUploadUtil.formatDate(formUpload.getCreatedAt()));
			sb.append("\"");
			sb.append(StringPool.COMMA);
			
			
			sb.append(StringPool.NEW_LINE);
		}
		

		String fileName = title + ".csv";
		byte[] bytes = sb.toString().getBytes();
		String contentType = ContentTypes.APPLICATION_TEXT;

		PortletResponseUtil.sendFile(resourceRequest, resourceResponse, fileName, bytes, contentType);
	}
	
	
	protected void serveCaptcha(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
		CaptchaUtil.serveImage(resourceRequest, resourceResponse);
	}
	
	/**
	 * Load HTML into a String
	 * @return
	 */
	protected String loadEmailBody() {
		ClassLoader classLoader = getClass().getClassLoader();
		
		InputStream in = classLoader.getResourceAsStream("email-template/email-form-upload.html");
		String emailContent = null;
		
		try {
			emailContent = IOUtils.toString(in, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emailContent;
	}
	
	protected String getFormattedFileSize(long fileSize) {
		long size = fileSize / 1024;
		return String.format("%s", size) + " KB";
	}
	
	/**
	 * Process file from form
	 * @param uploadRequest 
	 * @param response 
	 * @param validationErrorFields 
	 * @param request 
	 * @return
	 */
	protected boolean processFileUpload(
			Map<String, String> fieldsMap, 
			ArrayList<String> validationErrorFields,
			UploadPortletRequest uploadRequest,
			final ActionResponse response, 
			final ActionRequest request) {
		
		boolean hasError = false;
		
		File file = uploadRequest.getFile("file");
		String sourceFileName = uploadRequest.getFileName("file");
		
		try { 
			
			
			if (file != null) {
				logger.info("FormUpload: " + sourceFileName + ", " + file.getAbsolutePath() + ", " + file.getName() + ", new name: " + fieldsMap.get("file"));
			}
			
			byte[] bytes = null;
			try {
				bytes = FileUtil.getBytes(file);
			} catch (IOException e2) {
				hasError = true;
				e2.printStackTrace();
			}
			
			File newFile = null;
			String newFilePath = PortletPropsValues.FILE_DOWNLOAD_DIR + fieldsMap.get("file");
			
			fieldsMap.put("fileSize", String.valueOf(bytes.length));
			fieldsMap.put("filePath", newFilePath);
			
			if ((bytes != null) && (bytes.length > 0)) {
			
				try {
					
					newFile = new File(newFilePath);
					FileInputStream fileInputStream = new FileInputStream(file);
					FileOutputStream fileOutputStream = new FileOutputStream(newFile);			
					fileInputStream.read(bytes);				
					fileOutputStream.write(bytes, 0, bytes.length);					
					fileOutputStream.close();
					fileInputStream.close();
					
					fileUploadSuccess = true;
					
				} catch (FileNotFoundException e) {
					hasError = true;
					logger.error("File Not Found...");				
					e.printStackTrace();
					
				} catch (IOException e1){
					hasError = true;
					logger.error("Error Reading The File...");
					e1.printStackTrace();
				}
			}
	        
		} catch (Exception e) {
			hasError = true;
			logger.error("Error placing file to dest directory: " + e.getMessage() + ", " + e.toString());
			e.printStackTrace();
		}
		
		if (hasError) {
			SessionErrors.add(request, "error");
			 
			/* disabling custom error message - doesn't work*/
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages. KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			 
			logger.error("Eroare cu fisierul uploadat: " + sourceFileName);
		}
		
		return !hasError;
	}
	
	
	public String getFileNameWithMillies(String originalFileName) {
		String fileExtension = FilenameUtils.getExtension(originalFileName);
		String epochMillis = String.valueOf(FormularUploadUtil.getEpochMillis());
		String newFileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "_" + epochMillis + "." + fileExtension;
		return newFileName;
	}
	
}
