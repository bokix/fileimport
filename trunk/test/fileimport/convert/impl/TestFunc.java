package fileimport.convert.impl;

import fileimport.IImportRequest;
import fileimport.IValidateFunction;

public class TestFunc implements IValidateFunction {

	public void validate(Object obj, IImportRequest request) throws Exception {
		System.out.println("test validate...");
	}

}
