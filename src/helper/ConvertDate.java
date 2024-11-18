package helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConvertDate {
	public static String ToString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
		return dateFormat.format(date);
	}
	
	public static Date ToDate(String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			return formatter.parse(dateInString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
