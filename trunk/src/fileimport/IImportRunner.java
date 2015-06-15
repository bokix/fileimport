package fileimport;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import fileimport.bean.ImportResult;

public interface IImportRunner {

	/**
	 * 导入
	 * 
	 * @return ImportResult
	 * @throws Exception
	 */
	public ImportResult importFile(InputStream importFileInputStream)
			throws Exception;

	/**
	 * 返回导入文件的文件头（即：标题行）。对于那些没有定义到mapping文件中的文件头，将不会返回。
	 * 
	 * @return list of Mapping
	 */
	public List getRealHeader();

	/**
	 * 获取模板文件
	 */
	public HSSFWorkbook getTempleteFile();
}
