package fileimport.convert.impl;

import test.bean.Work;
import fileimport.IImportRequest;
import fileimport.IValidateFunction;

public class AfterValidateFunc implements IValidateFunction {

	public void validate(Object obj, IImportRequest request) throws Exception {
		System.out.println("after validate function...");
		Work w = (Work) obj;
		if("名称5".equals(w.getWorkName()) && !w.isLeader()){
			throw new Exception("名称5的任务只能是领导");
		}
	}

}
