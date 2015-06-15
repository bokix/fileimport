package fileimport;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import fileimport.bean.ImportResult;

public interface IImportRunner {

	/**
	 * ����
	 * 
	 * @return ImportResult
	 * @throws Exception
	 */
	public ImportResult importFile(InputStream importFileInputStream)
			throws Exception;

	/**
	 * ���ص����ļ����ļ�ͷ�����������У���������Щû�ж��嵽mapping�ļ��е��ļ�ͷ�������᷵�ء�
	 * 
	 * @return list of Mapping
	 */
	public List getRealHeader();

	/**
	 * ��ȡģ���ļ�
	 */
	public HSSFWorkbook getTempleteFile();
}
