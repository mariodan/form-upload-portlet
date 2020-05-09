package ro.cjarges.formupload.util;

import ro.cjarges.formupload.model.FormUploadModel;
import com.liferay.portal.kernel.util.OrderByComparator;

public class TelefonComparator extends OrderByComparator {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7813725177520880039L;
	public static String ORDER_BY_ASC = "status ASC";
	public static String ORDER_BY_DESC = "status DESC";
	
	public TelefonComparator()
	{
		this(false);
	}

	public TelefonComparator(boolean asc) {
		_asc = asc;
	}


	public int compare(Object obj1, Object obj2) {
		FormUploadModel instance1 = (FormUploadModel) obj1;
		FormUploadModel instance2 = (FormUploadModel) obj2;
		
		int value = instance1.getTelefon().toLowerCase().compareTo(instance2.getTelefon().toLowerCase());
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
