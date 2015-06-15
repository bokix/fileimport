package fileimport.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import fileimport.IConvert;
import fileimport.IFunction;
import fileimport.IValidateFunction;
import fileimport.bean.BeanConfig;

public class FunctionUtil {
	private Log log = LogFactory.getLog(this.getClass());
	private static FunctionUtil instance = null;

	private Map convertHandle = new HashMap();
	private Map functionHandle = new HashMap();

	private FunctionUtil() throws Exception {
		initConverts();
		initFunctions();
	}

	private void initFunctions() throws Exception {
		String configRootPath = PropUtil.getString("fileimport.config.path");
		if (!configRootPath.endsWith("/")) {
			configRootPath += "/";
		}
		InputStream is = null;
		Element rootElement, convertElement;
		String[] functionFiles = PropUtil.getString("fileimport.function.xml")
				.split(",");

		BeanConfig functionsBean;
		IFunction functionImpl;
		for (int i = 0; i < functionFiles.length; i++) {

			String path2 = configRootPath + "function/" + functionFiles[i];
			try {
				is = this.getClass().getResourceAsStream(path2);
				rootElement = XMLUtil.getRootElement(is);

				List list = rootElement.selectNodes("./function");

				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					convertElement = (Element) iterator.next();
					functionsBean = new BeanConfig(convertElement);
					functionImpl = (IFunction) Class.forName(
							functionsBean.getClassName()).newInstance();
					functionHandle.put(functionsBean.getName(), functionImpl);
				}

			} catch (Exception e) {
				log.error("initFunctions error,skip this one.", e);
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
	}

	private void initConverts() throws Exception {
		String configRootPath = PropUtil.getString("fileimport.config.path");
		if (!configRootPath.endsWith("/")) {
			configRootPath += "/";
		}
		InputStream is = null;
		Element rootElement, convertElement;
		String[] convertFiles = PropUtil.getString("fileimport.convert.xml")
				.split(",");
		BeanConfig convertBean;
		IFunction convertImpl;
		for (int i = 0; i < convertFiles.length; i++) {
			String path = configRootPath + "convert/" + convertFiles[i];
			try {
				is = this.getClass().getResourceAsStream(path);
				rootElement = XMLUtil.getRootElement(is);
				List list = rootElement.selectNodes("./convert");

				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					convertElement = (Element) iterator.next();
					convertBean = new BeanConfig(convertElement);
					convertImpl = (IFunction) Class.forName(
							convertBean.getClassName()).newInstance();
					convertHandle.put(convertBean.getName(), convertImpl);
				}
			} catch (Exception e) {
				log.error("initConverts error,skip this one.", e);
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
	}

	public static IConvert getConvert(String name) throws Exception {
		return getInstance().getConvertByName(name);
	}

	private IConvert getConvertByName(String name) {
		return (IConvert) convertHandle.get(name);
	}

	private IValidateFunction getValidateFunctionByName(String name) {
		return (IValidateFunction) functionHandle.get(name);
	}

	private static FunctionUtil getInstance() throws Exception {
		if (instance == null) {
			synchronized (FunctionUtil.class) {
				instance = new FunctionUtil();
			}
		}
		return instance;
	}

	public static IValidateFunction getValidateFunction(String validateFuncName)
			throws Exception {
		return getInstance().getValidateFunctionByName(validateFuncName);
	}

}
