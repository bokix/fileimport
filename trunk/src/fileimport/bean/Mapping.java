package fileimport.bean;

import org.dom4j.Element;

import fileimport.util.XMLUtil;


public class Mapping extends BaseConfigBean {
	private String from;
	private String to;
	private boolean required;
	private String convert;
	private boolean uk;
	private String value;


	public Mapping(Element mappingElement) {
		from = mappingElement.attributeValue("from");
		to = mappingElement.attributeValue("to");
		required = XMLUtil.getBooleanAttributeValue(mappingElement, "required");
		convert = mappingElement.attributeValue("convert");
		uk = XMLUtil.getBooleanAttributeValue(mappingElement, "uk");
		value = mappingElement.attributeValue("value");
	}

	public String toString() {
		StringBuffer sbuff = new StringBuffer();

		sbuff.append("-->").append("from:" + from).append("\n");
		sbuff.append("-->").append("to:" + to).append("\n");
		sbuff.append("-->").append("required:" + required).append("\n");
		sbuff.append("-->").append("convert:" + convert).append("\n");
		sbuff.append("-->").append("uk:" + uk).append("\n");
		sbuff.append("-->").append("value:" + value).append("\n");

		return sbuff.toString();
	}
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getConvert() {
		return convert;
	}

	public void setConvert(String convert) {
		this.convert = convert;
	}

	public boolean isUk() {
		return uk;
	}

	public void setUk(boolean uk) {
		this.uk = uk;
	}

}
