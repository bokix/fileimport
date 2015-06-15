package test;

import java.util.List;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import fileimport.IImportRunner;
import fileimport.ImportRunnerFactroy;
import fileimport.bean.ImportResult;

public class MainTest {
	private final static String configfile = "test.xls.xml";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		IImportRunner runner = null;
		List list = null, error = null;
		ImportResult result = null;
		runner = ImportRunnerFactroy.getRunner(configfile, null);

	}

}
