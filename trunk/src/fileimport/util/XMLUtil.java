package fileimport.util;

import java.io.File;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {

	private XMLUtil() {
		super();
	}

	/**
	 * @deprecated 当通过war包的形式部署应用时，是没有办法通过文件路径（realpath）来获取资源文件的，
	 *             最好是通过classloader的getResourceAsStream
	 * 
	 * @param p_sFileName
	 * @return
	 * @throws Exception
	 */
	public final static Document getDoc(String p_sFileName) throws Exception {
		return getDoc(new File(p_sFileName));
	}

	public final static Document getDoc(InputStream is) throws Exception {
		try {
			SAXReader reader = new SAXReader();
			return reader.read(is);
		} catch (Exception ex) {
			throw new Exception("Failed to load XML from file input stream.",
					ex);
		}
	}

	public final static Document getDoc(File _file) throws Exception {
		if (!_file.exists()) {
			throw new Exception("File not found: " + _file.getAbsolutePath());
		}

		try {
			SAXReader reader = new SAXReader();
			return reader.read(_file);
		} catch (Exception ex) {
			throw new Exception("Failed to load XML from file: "
					+ _file.getAbsolutePath(), ex);
		}
	}

	public final static Element getRootElement(String p_sFileName)
			throws Exception {
		Document xmlDoc = getDoc(p_sFileName);
		return (xmlDoc == null ? null : xmlDoc.getRootElement());
	}

	public final static Element getRootElement(File p_sFileName)
			throws Exception {
		Document xmlDoc = getDoc(p_sFileName);
		return (xmlDoc == null ? null : xmlDoc.getRootElement());
	}

	public static boolean getBooleanAttributeValue(Element mappingElement,
			String attribute) {
		return "true".equals(mappingElement.attributeValue(attribute));
	}

	public static Element getRootElement(InputStream is) throws Exception {
		Document xmlDoc = getDoc(is);
		return (xmlDoc == null ? null : xmlDoc.getRootElement());
	}

}