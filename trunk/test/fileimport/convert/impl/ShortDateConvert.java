package fileimport.convert.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileimport.IConvert;
import fileimport.IImportRequest;

public class ShortDateConvert implements IConvert {

	public Object convert(Object oldValue, IImportRequest request) {
		System.out.println("short date convert...");
		
		if(oldValue==null){
			return null;
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-mm-dd");
		Date result = null;

		if (oldValue instanceof Date) {
			result = new Date(((Date) oldValue).getTime());
		} else {
			try {
				result =  f.parse(String.valueOf(oldValue));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}