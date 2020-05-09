/**
 * 
 */
package ro.cjarges.formupload.util;

import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author marian
 *
 */
public class CustomComparatorUtil {

	public static OrderByComparator getSesizareOrderByComparator(String orderByCol, String orderByType) {
		
		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator orderByComparator = null;
		
		if (orderByCol.equalsIgnoreCase("nume")) {
			orderByComparator = new NameComparator(orderByAsc);
		}
		else if (orderByCol.equalsIgnoreCase("email")) {
			orderByComparator = new EmailComparator(orderByAsc);
		}
		else if (orderByCol.equalsIgnoreCase("telefon")) {
			orderByComparator = new TelefonComparator(orderByAsc);
		} 
		else if (orderByCol.equalsIgnoreCase("createdAt")) {
			orderByComparator = new DataCreareComparator(orderByAsc);
		}
		
		return orderByComparator;
	}


}
