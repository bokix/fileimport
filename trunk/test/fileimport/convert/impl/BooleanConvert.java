package fileimport.convert.impl;

import fileimport.IConvert;
import fileimport.IImportRequest;

public class BooleanConvert implements IConvert {

	public Object convert(Object oldValue, IImportRequest request) {
		System.out.println("boolean convert...");
		
		if(oldValue==null){
			return null;
		}
		String v = String.valueOf(oldValue);
		return new Boolean("1".equalsIgnoreCase(v) || "ÊÇ".equalsIgnoreCase(v)
				|| "true".equalsIgnoreCase(v));
	}

}
