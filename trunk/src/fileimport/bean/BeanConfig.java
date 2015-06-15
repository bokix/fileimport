package fileimport.bean;

import org.dom4j.Element;

public class BeanConfig extends BaseConfigBean {
	private String name;
	private String className;
	private String desc;

	public BeanConfig(Element convertElement) {
		this.name = convertElement.attributeValue("name");
		this.className = convertElement.attributeValue("class");
		this.desc = convertElement.selectSingleNode("desc").getText();
	}

	
	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("name:" + name).append("\n");
		sbuff.append("className:" + className).append("\n");
		sbuff.append("desc:" + desc).append("\n");
		
		return sbuff.toString();
	}


	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
