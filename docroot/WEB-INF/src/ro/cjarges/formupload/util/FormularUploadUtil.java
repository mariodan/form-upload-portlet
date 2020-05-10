
/**
 * @author marian
 *
 */
package ro.cjarges.formupload.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletPreferences;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.service.EmailAddressLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.service.ExpandoRowLocalServiceUtil;
import ro.cjarges.formupload.model.FormUploadModel;


public class FormularUploadUtil {
	
	private static Pattern pattern;
	private static Matcher matcher;

	public static final int ORGANIZATION_ID = 11651;
	
	private static Logger logger = Logger.getLogger(FormularUploadUtil.class);
	
	private static String fileSizeMax = PortletPropsValues.UPLOAD_FILE_MAX_SIZE_KB;
	private static String allowedFileExtensions = PortletPropsValues.UPLOAD_FILE_EXTENSIONS;
	
	private static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Validate emailAddress
	 * 
	 * @param emailAddress
	 * @return
	 */
	public static boolean validateEmailAddress(String emailAddress) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(emailAddress);
		
		return matcher.matches();
	}
	
	
	/**
	 * Check file size
	 * @param file
	 * @return
	 */
	public static boolean validateFileSize(File file) {
		long fileSize = file.length() / 1024;
		return fileSize < Long.valueOf(fileSizeMax);
	}
	
	/**
	 * Check file name length
	 * @param file
	 * @return
	 */
	public static boolean validateFileNameLength(File file) {
		return file.getName().length() < 60;
	}
	
	/**
	 * Check file extension
	 * @param file
	 * @return
	 */
	public static boolean validateFileExtension(File file) {
		return FilenameUtils
				.getExtension(file.getName().toLowerCase())
				.contains(allowedFileExtensions.toLowerCase());
	}
	
	
	/**
	 * Validate Phone Number
	 * 
	 * @param phoneNo
	 * @return
	 */
	public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
        //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        //return false if nothing matches the input
        else return false;
    }


	public static String getEmailFromAddress(PortletPreferences preferences,
			long companyId) throws SystemException {
		// TODO Auto-generated method stub
		return PortalUtil.getEmailFromAddress(preferences, companyId, PortletPropsValues.EMAIL_FROM_ADDRESS);
	}

	
	/**
	 * Find the primary email address of the suborganization/subsite
	 * 
	 * @param preferences
	 * @param companyId
	 * @param themeDisplay
	 * @return
	 */
	public static String getEmailToAddress(PortletPreferences preferences, long companyId, ThemeDisplay themeDisplay) {
		
		long organizationId = 0L;
		String emailTo = "";
		
		/* log method call */
		logger.info("[ getEmailToAddress ]:"+companyId);
		
		try {
			organizationId=themeDisplay.getLayout().getGroup().getOrganizationId();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if(organizationId == 0) {
			organizationId = ORGANIZATION_ID;
		}

		List<EmailAddress> emailList = null;
		
		try {
			emailList = EmailAddressLocalServiceUtil.getEmailAddresses(companyId, "com.liferay.portal.model.Organization", organizationId);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(emailList.size()>0) {
			
			for(Iterator<EmailAddress> ea = emailList.iterator(); ea.hasNext();) {
				EmailAddress email = ea.next();
				
				if(email.isPrimary()) {
					logger.info("[Web Form Portlet] - [EmailAddress-PRIMARY] - "+organizationId+": "+email.getAddress());
					emailTo = email.getAddress();
				} else {
					logger.info("[Web Form Portlet] - [EmailAddress] - "+organizationId+": "+email.getAddress());
				}
			}
		} else {
			logger.info("Could not get email for companyID:"+companyId+", organizationId:"+organizationId);
		}
		
		return emailTo;
	}
	

	public static void checkTable(long companyId, String databaseTableName,
			PortletPreferences preferences) {
		// TODO Auto-generated method stub
	}
	
	public static String getEmailFromName(PortletPreferences preferences, long companyId) throws SystemException {

		return PortalUtil.getEmailFromName(preferences, companyId, PortletPropsValues.EMAIL_FROM_NAME);
	}

	public static String getNewDatabaseTableName(String portletId) throws SystemException {

		long formId = CounterLocalServiceUtil.increment(FormularUploadUtil.class.getName());

		return portletId + StringPool.UNDERLINE + formId;
	}

	public static int getTableRowsCount(long companyId, String tableName) throws SystemException {

		return ExpandoRowLocalServiceUtil.getRowsCount(companyId, FormularUploadUtil.class.getName(), tableName);
	}
	
	
	public static FormUploadModel getFormUploadFieldsDTO(Map<String, String> fieldsMap) {
		
		/* create audienta object */
		FormUploadModel formUpload = new FormUploadModel();
		
		/* Get table fields */
		String nume = fieldsMap.get("nume");
		String prenume = fieldsMap.get("prenume");
		String telefon = fieldsMap.get("telefon");
		String email = fieldsMap.get("email");
		
		Date createdAt = Calendar.getInstance().getTime();
	
		/* Set object properties */
		formUpload.setNume(nume);
		formUpload.setPrenume(prenume);
		formUpload.setEmail(email);
		formUpload.setTelefon(telefon);
		formUpload.setCreatedAt(createdAt);
		
		return formUpload;
		
	}

	
}