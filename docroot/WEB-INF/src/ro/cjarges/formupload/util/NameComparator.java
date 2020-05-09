package ro.cjarges.formupload.util;

import ro.cjarges.formupload.model.FormUploadModel;
import com.liferay.portal.kernel.util.OrderByComparator;

public class NameComparator extends OrderByComparator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1233240708732123240L;
	public static String ORDER_BY_ASC = "status ASC";
	public static String ORDER_BY_DESC = "status DESC";
	
	public NameComparator()
	{
		this(false);
	}

	public NameComparator(boolean asc) {
		_asc = asc;
	}


	public int compare(Object obj1, Object obj2) {
		FormUploadModel instance1 = (FormUploadModel) obj1;
		FormUploadModel instance2 = (FormUploadModel) obj2;
		
		int value = instance1.getNume().toLowerCase().compareTo(instance2.getNume().toLowerCase());
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
