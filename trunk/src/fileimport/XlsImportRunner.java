package fileimport;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import fileimport.bean.ImportError;
import fileimport.bean.ImportResult;
import fileimport.bean.Mapping;
import fileimport.bean.MappingConfig;
import fileimport.impl.ImportRequestImpl;
import fileimport.util.FunctionUtil;
import fileimport.util.MsgUtil;

public class XlsImportRunner implements IImportRunner {

	private final int CELL_REQUIRED_STYLE = 1;
	private final int CELL_NORMAL_STYLE = 0;

	private MappingConfig mappingConfig;
	private IImportRequest request;
	private Log log = LogFactory.getLog(this.getClass());
	private String[] xlsHeads = null;
	private Class cls;
	private ImportResult result;
	private short firstCellNum;
	private short lastCellNum;
	private HashSet ukCheckSet;

	protected XlsImportRunner(String configFileName, HttpServletRequest req)
			throws Exception {
		this.mappingConfig = new MappingConfig(configFileName);
		this.request = new ImportRequestImpl(req);
		cls = Class.forName(mappingConfig.getJavaBeanName());
		result = new ImportResult();
		ukCheckSet = new HashSet();
	}

	public ImportResult importFile(InputStream importFileInputStream)
			throws Exception {
		HSSFWorkbook workBook = new HSSFWorkbook(importFileInputStream);

		HSSFSheet sheet = workBook.getSheetAt(0);
		int firstRowNum = mappingConfig.getTitleRowNum() < 0 ? sheet
				.getFirstRowNum() : mappingConfig.getTitleRowNum();
		int lastRowNum = sheet.getLastRowNum();

		log.debug("firstRowNum:" + firstRowNum + ",lastRowNum:" + lastRowNum);

		if ((lastRowNum - firstRowNum) > mappingConfig.getMaxRow()) {
			throw new Exception(MsgUtil.getMsg("max.row") + "("
					+ (lastRowNum - firstRowNum) + "/"
					+ mappingConfig.getMaxRow() + ")");
		}
		if (xlsHeads == null) {
			fillHeads(sheet.getRow(firstRowNum));
			log.debug("heads:" + ArrayUtils.toString(xlsHeads));
		}

		String[] lostHeads = getLostHeads();
		if (lostHeads != null && lostHeads.length > 0) {
			throw new Exception(MsgUtil.getMsg("lost.column") + ":"
					+ ArrayUtils.toString(lostHeads));
		}

		HSSFRow row = null;
		for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				fillBean(row);
			}
		}

		return result;
	}

	/**
	 * 校验mapping文件中定义的required column.
	 * 
	 * @return
	 */
	private String[] getLostHeads() {

		String[] requiredColumns = mappingConfig.getRequiredColumns();
		if (requiredColumns == null) {
			log.debug("not defined required columns. validate success.");
			return null;
		}

		log.debug("required columns:" + ArrayUtils.toString(requiredColumns));

		List lost = new ArrayList();
		String[] tmp = (String[]) xlsHeads.clone();
		Arrays.sort(tmp);

		for (int i = 0; i < requiredColumns.length; i++) {
			if (Arrays.binarySearch(tmp, requiredColumns[i]) < 0) {
				lost.add(requiredColumns[i]);
			}
		}
		return (String[]) lost.toArray(new String[0]);
	}

	private void fillBean(HSSFRow row) throws Exception {

		Object bean = cls.newInstance();
		HSSFCell cell = null;
		Mapping mapping = null;
		ImportError ie = null;

		for (short i = firstCellNum, headIndex = 0; headIndex < xlsHeads.length; i++, headIndex++) {
			cell = row.getCell(i);
			mapping = mappingConfig.getDataMapping(xlsHeads[headIndex]);
			if (mapping == null) {
				log.debug("skip! mapping can not found:" + xlsHeads[headIndex]);
				continue;
			}
			Object value = null;
			try {
				value = getCellValue(cell);

				if (mapping.getConvert() != null && mapping.getConvert() != "") {
					IConvert convert = FunctionUtil.getConvert(mapping
							.getConvert());
					if (convert == null) {
						throw new Exception(MsgUtil.getMsg(
								"function.not.defined",
								new Object[] { mapping.getConvert() }));
					}
					value = convert.convert(value, this.request);
				}

				/**
				 * 注意，value为null和“”是不一样的，当需要将对象字段设置为空的时候，只要导入的value为“”，
				 * 通过BeanUtils.setProperty就可以设置字段为空值
				 */

				if (mapping.isRequired()
						&& (null == value || StringUtils.isBlank(String
								.valueOf(value)))) {
					throw new Exception(MsgUtil.getMsg("value.required",
							new Object[] { mapping.getFrom() }));
				}

				if (value == null) {
					continue;
				}

				BeanUtils.setProperty(bean, mapping.getTo(), value);

			} catch (Exception e) {
				log.error("get cell value error.", e);
				if (ie == null) {
					ie = new ImportError();
				}

				ie.setJavaBeanPropertyName(ie.getJavaBeanPropertyName() + ","
						+ mapping.getTo());
				ie.setRowIndex(row.getRowNum() + 1);
				ie.setXlsColumnName(ie.getXlsColumnName() + ","
						+ mapping.getFrom());
				ie.setXlsValue(ie.getXlsValue() + "[" + String.valueOf(value)
						+ "]");
				String msg = ie.getErrorMsg();
				if (StringUtils.isNotEmpty(msg)) {
					msg += ";" + e.getMessage();
				} else {
					msg = e.getMessage();
				}
				ie.setErrorMsg(msg);

				continue;
			}

		}

		/***/
		if (ie != null) {
			result.addErrorOne(ie);
			return;
		}
		/***/

		fillValue(bean);

		if (isDuplicate(bean)) {
			log.error("found a duplicate data. ");
			ie = new ImportError();

			ie.setJavaBeanPropertyName(null);
			ie.setRowIndex(row.getRowNum() + 1);
			ie.setXlsColumnName(null);
			ie.setXlsValue(null);
			ie.setErrorMsg(MsgUtil.getMsg("duplicate.data"));

			result.addErrorOne(ie);

			// 直接返回，继续处理下一行数据。
			return;

		}

		try {
			if (mappingConfig.getValidateFuncName() != null
					&& mappingConfig.getValidateFuncName() != "") {
				IValidateFunction validateFunction = FunctionUtil
						.getValidateFunction(mappingConfig
								.getValidateFuncName());
				if (validateFunction == null) {
					throw new Exception(
							MsgUtil.getMsg("function.not.defined",
									new Object[] { mappingConfig
											.getValidateFuncName() }));
				}
				validateFunction.validate(bean, request);
			}
		} catch (Exception e) {
			log.error("validate error. [" + mappingConfig.getValidateFuncName()
					+ "]" + e.getMessage());
			ie = new ImportError();
			ie.setJavaBeanPropertyName(null);
			ie.setRowIndex(row.getRowNum() + 1);
			ie.setXlsColumnName(null);
			ie.setXlsValue(null);
			ie.setErrorMsg(e.getMessage());

			result.addErrorOne(ie);

			// 直接返回，继续处理下一行数据。
			return;
		}

		result.addSuccessOne(bean);

	}

	private void fillValue(Object bean) throws Exception {

		HashMap fillMap = mappingConfig.getFillMappingMap();

		if (fillMap == null || fillMap.size() <= 0) {
			return;
		}

		Mapping mapping;
		for (Iterator iterator = fillMap.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			mapping = (Mapping) fillMap.get(key);
			Object value = mapping.getValue();

			if (mapping.getConvert() != null && mapping.getConvert() != "") {
				IConvert convert = FunctionUtil
						.getConvert(mapping.getConvert());
				if (convert == null) {
					throw new Exception(MsgUtil.getMsg("function.not.defined",
							new Object[] { mapping.getConvert() }));
				}

				value = convert.convert(value, this.request);
			}

			BeanUtils.setProperty(bean, mapping.getTo(), value);
		}

	}

	/**
	 * 判断当前数据是否是重复的数据
	 * 
	 * @param bean
	 *            当前数据行转换以后的bean
	 * @return boolean. true:重复数据; false:不是重复数据
	 * @throws Exception
	 */
	private boolean isDuplicate(Object bean) throws Exception {

		String[] ukPros = mappingConfig.getUkProperties();
		if (ukPros == null || ukPros.length < 1) {
			return false;
		}

		String key = "";
		for (int i = 0; i < ukPros.length; i++) {
			key += BeanUtils.getProperty(bean, ukPros[i]) + "-";
		}
		if (ukCheckSet.contains(key)) {
			return true;
		} else {
			ukCheckSet.add(key);
			return false;
		}
	}

	private Object getCellValue(HSSFCell cell) throws Exception {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				DecimalFormat df = new DecimalFormat("##.####");
				return df.format(cell.getNumericCellValue());
			}

		case HSSFCell.CELL_TYPE_STRING:
			return StringUtils.trim(cell.getStringCellValue());

		case HSSFCell.CELL_TYPE_FORMULA:
			return new Double(cell.getNumericCellValue());

		case HSSFCell.CELL_TYPE_BOOLEAN:
			return new Boolean(cell.getBooleanCellValue());

		case HSSFCell.CELL_TYPE_ERROR:
			throw new Exception("error value:"
					+ String.valueOf(cell.getErrorCellValue()));
		default:
			return null;
		}
	}

	private void fillHeads(HSSFRow row) throws Exception {
		if (row == null) {
			throw new Exception(MsgUtil.getMsg("first.row.empty"));
		}
		firstCellNum = row.getFirstCellNum();
		lastCellNum = row.getLastCellNum();

		log.debug("firstCellNum:" + firstCellNum + ",lastCellNum:"
				+ lastCellNum);

		xlsHeads = new String[lastCellNum - firstCellNum];
		HSSFCell cell = null;

		for (int i = 0, index = firstCellNum; i < xlsHeads.length; index++, i++) {
			cell = row.getCell((short) index);
			if (cell == null) {
				xlsHeads[i] = "";
				continue;
			}
			xlsHeads[i] = cell.getStringCellValue();
		}
	}

	public HSSFWorkbook getTempleteFile() {
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("sheet1");
		HSSFRow title = sheet.createRow(0);

		HashMap mappingMap = mappingConfig.getDataMappingMap();
		HSSFCellStyle requiredStyle = getStyle(CELL_REQUIRED_STYLE, book);
		HSSFCellStyle normalStyle = getStyle(CELL_NORMAL_STYLE, book);

		List list = new ArrayList();
		list.addAll(mappingMap.values());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Mapping m1 = (Mapping) o1;
				Mapping m2 = (Mapping) o2;
				if (m1.isRequired() == m2.isRequired()) {
					return 0;
				}
				if (m1.isRequired()) {
					return -1;
				}
				return 1;
			}
		});

		Mapping mapping;
		HSSFCell cell;
		short lastCell = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			mapping = (Mapping) iterator.next();
			cell = title.createCell(lastCell++);

			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(mapping.getFrom());
			if (mapping.isRequired()) {
				cell.setCellStyle(requiredStyle);
			} else {
				cell.setCellStyle(normalStyle);
			}
		}

		return book;
	}

	private HSSFCellStyle getStyle(int styleType, HSSFWorkbook book) {
		HSSFCellStyle style = book.createCellStyle();

		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderLeft(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.SOLID_FOREGROUND);
		switch (styleType) {
		case CELL_REQUIRED_STYLE:
			style.setFillForegroundColor(HSSFColor.ORANGE.index);
			break;
		case CELL_NORMAL_STYLE:
			style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			break;
		default:
			break;
		}

		return style;
	}

	public List getRealHeader() {
		if (xlsHeads == null) {
			log.error("xls head is null! need import first!");
			throw new IllegalStateException();
		}
		List list = new ArrayList();
		Mapping mapping = null;
		for (int i = 0; i < xlsHeads.length; i++) {
			mapping = mappingConfig.getDataMapping(xlsHeads[i]);
			if (mapping != null) {
				list.add(mapping);
			}
		}
		return list;
	}

}
