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
public class DataCreareComparator extends OrderByComparator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5223702163495333689L;
	public static String ORDER_BY_ASC = "status ASC";
	public static String ORDER_BY_DESC = "status DESC";
	
	public DataCreareComparator()
	{
		this(false);
	}

	public DataCreareComparator(boolean asc) {
		_asc = asc;
	}


	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.util.OrderByComparator#compare(java.lang.Object, java.lang.Object)
	 */
	
	public int compare(Object obj1, Object obj2) {
		FormUploadModel instance1 = (FormUploadModel) obj1;
		FormUploadModel instance2 = (FormUploadModel) obj2;
		
		int value = instance1.getCreatedAt().compareTo(instance2.getCreatedAt());
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
