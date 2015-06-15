package fileimport;

import javax.servlet.http.HttpServletRequest;

public class ImportRunnerFactroy {

	public static IImportRunner getRunner(String configFileName)
			throws Exception {

		return ImportRunnerFactroy.getRunner(configFileName, null);
	}

	public static IImportRunner getRunner(String configFileName,
			HttpServletRequest request) throws Exception {

		if (configFileName.endsWith(".xls.xml")) {
			return new XlsImportRunner(configFileName, request);
		} else {
			//TODO 
			throw new UnsupportedOperationException();
		}
	}

}
