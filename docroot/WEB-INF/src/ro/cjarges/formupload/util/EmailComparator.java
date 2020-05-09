/**
 * 
 */
package ro.cjarges.formupload.util;

import ro.cjarges.formupload.model.FormUploadModel;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author marian
 *
 */
public class EmailComparator extends OrderByComparator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5440141496458568114L;
	public static String ORDER_BY_ASC = "status ASC";
	public static String ORDER_BY_DESC = "status DESC";
	
	public EmailComparator()
	{
		this(false);
	}

	public EmailComparator(boolean asc) {
		_asc = asc;
	}


	public int compare(Object obj1, Object obj2) {
		FormUploadModel instance1 = (FormUploadModel) obj1;
		FormUploadModel instance2 = (FormUploadModel) obj2;
		
		int value = instance1.getEmail().toLowerCase().compareTo(instance2.getEmail().toLowerCase());
		if(_asc)
		{
			return value;
		} else
		{
			return -value;
		}
	}
	public String getOrderBy() {
		if (_asc) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	private boolean _asc;
	  
}
