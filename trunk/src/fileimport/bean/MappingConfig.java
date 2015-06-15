package fileimport.bean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.dom4j.Element;

import fileimport.util.PropUtil;
import fileimport.util.XMLUtil;

public class MappingConfig extends BaseConfigBean {

	/** 配置文件的文件名 */
	private String fileName = "";

	/** 导入后最终转换成的javabean */
	private String javaBeanName = "";

	/** 一次性能导入的最多数据，默认是Integer.MAX_VALUE */
	private int maxRow = Integer.MAX_VALUE;

	private String validateFuncName = "";

	private String desc = "";

	/** uk数据列的集合，类似于数据库中的UK键 ，导入时做唯一性校验 */
	private String[] ukProperties = null;

	/** 必填数据列的集合，用于导入时的校验 */
	private String[] requiredColumns = null;

	/** 为了保证导出模板时模板的列与xml配置文件中的顺序一致，所以需要维护此map的顺序 */
	private HashMap dataMappingMap = new LinkedHashMap();

	/** */
	private HashMap fillMappingMap = new HashMap();
	
	private int titleRowNum = 0;

	public MappingConfig(String mappingFileName) throws Exception {
		this.fileName = mappingFileName;
		String configRootPath = PropUtil.getString("fileimport.config.path");
		if (!configRootPath.endsWith("/")) {
			configRootPath += "/";
		}
		
		InputStream is = null;
		Element rootElement = null;
			
		try{
		is = this.getClass().getResourceAsStream(
				configRootPath + "mapping/" + mappingFileName);
		rootElement = XMLUtil.getRootElement(is);
		}finally{
			if(is!=null){
				is.close();
			}
		}

		this.validateFuncName = rootElement.attributeValue("validateFunc");
		this.javaBeanName = rootElement.attributeValue("class");
		try {
			this.titleRowNum = Integer
					.parseInt(rootElement.attributeValue("titleRowNum"));
		} catch (Exception e) {
			this.titleRowNum = -1;
		}
		this.desc = rootElement.elementTextTrim("desc");
		try {
			this.maxRow = Integer
					.parseInt(rootElement.attributeValue("maxRow"));
		} catch (NumberFormatException e) {
			this.maxRow = Integer.MAX_VALUE;
		}

		initData(rootElement);
		initFill(rootElement);
	}

	private void initFill(Element rootElement) {
		List mappingNodeList = rootElement.selectNodes("./fill/mapping");
		Mapping mapping;
		Element mappingElement;

		for (Iterator iterator = mappingNodeList.iterator(); iterator.hasNext();) {
			mappingElement = (Element) iterator.next();
			mapping = new Mapping(mappingElement);

			// allMapping.add(mapping);
			fillMappingMap.put(mapping.getTo(), mapping);
		}
	}

	private void initData(Element rootElement) {
		List mappingNodeList = rootElement.selectNodes("./data/mapping");
		Mapping mapping;
		Element mappingElement;
		List ukPropertiesList = new ArrayList();
		List requiredColumnsList = new ArrayList();

		for (Iterator iterator = mappingNodeList.iterator(); iterator.hasNext();) {
			mappingElement = (Element) iterator.next();
			mapping = new Mapping(mappingElement);

			// allMapping.add(mapping);
			dataMappingMap.put(mapping.getFrom(), mapping);
			if (mapping.isUk()) {
				ukPropertiesList.add(mapping.getTo());
			}
			if (mapping.isRequired()) {
				requiredColumnsList.add(mapping.getFrom());
			}
		}
		ukProperties = (String[]) ukPropertiesList.toArray(new String[0]);
		requiredColumns = (String[]) requiredColumnsList.toArray(new String[0]);

	}

	/**
	 * @return the titleRowNum
	 */
	public int getTitleRowNum() {
		return titleRowNum;
	}

	/**
	 * @param titleRowNum the titleRowNum to set
	 */
	public void setTitleRowNum(int titleRowNum) {
		this.titleRowNum = titleRowNum;
	}

	public String[] getUkProperties() {
		return ukProperties;
	}

	public void setUkProperties(String[] ukProperties) {
		this.ukProperties = ukProperties;
	}

	public String[] getRequiredColumns() {
		return requiredColumns;
	}

	public void setRequiredColumns(String[] requiredColumns) {
		this.requiredColumns = requiredColumns;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getJavaBeanName() {
		return javaBeanName;
	}

	public void setJavaBeanName(String javaBeanName) {
		this.javaBeanName = javaBeanName;
	}

	public Mapping getDataMapping(String from) {
		return (Mapping) dataMappingMap.get(from);
	}

	public Mapping getFillMapping(String to) {
		return (Mapping) fillMappingMap.get(to);
	}

	public String toString(boolean showMapping) {
		StringBuffer sbuff = new StringBuffer();

		sbuff.append("fileName:" + fileName).append("\n");
		sbuff.append("javaBeanName:" + javaBeanName).append("\n");
		sbuff.append("maxRow:" + maxRow).append("\n");
		sbuff.append("validateFuncName:" + validateFuncName).append("\n");
		sbuff.append("desc:" + desc).append("\n");
		sbuff.append("ukColumns:" + ArrayUtils.toString(ukProperties)).append(
				"\n");
		sbuff.append("requiredColumns:" + ArrayUtils.toString(requiredColumns))
				.append("\n");
		if (showMapping) {
			for (Iterator iterator = dataMappingMap.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				sbuff.append(dataMappingMap.get(key).toString()).append("\n");
			}
			for (Iterator iterator = fillMappingMap.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				sbuff.append(fillMappingMap.get(key).toString()).append("\n");
			}
		}

		return sbuff.toString();
	}

	public String toString() {
		return this.toString(false);
	}

	public HashMap getDataMappingMap() {
		return dataMappingMap;
	}

	public HashMap getFillMappingMap() {
		return fillMappingMap;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	/**
	 * @return the validateFuncName
	 */
	public String getValidateFuncName() {
		return validateFuncName;
	}

	/**
	 * @param validateFuncName
	 *            the validateFuncName to set
	 */
	public void setValidateFuncName(String validateFuncName) {
		this.validateFuncName = validateFuncName;
	}

}
