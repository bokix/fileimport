package fileimport.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropUtil {
	private static Log log = LogFactory.getLog(PropUtil.class);
	private static Properties prop = null;
	private static final String[] CONFIG_FILES = { "/fileimport.config.properties" };

	private static void initProperties() {
		if (prop == null) {
			InputStream stream = null;
			prop = new Properties();
			for (int i = 0; i < CONFIG_FILES.length; i++) {

				try {
					stream = PropUtil.class
							.getResourceAsStream(CONFIG_FILES[i]);
					if (stream == null) {
						log.warn(" failed to load " + CONFIG_FILES[i]);
						continue;
					}

					prop.load(stream);
				} catch (Exception e) {
					log.error("load property file error:" + CONFIG_FILES[i], e);
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}

	}

	public static boolean getBoolean(String key) {
		return PropUtil.getBoolean(key, false);
	}

	public static boolean getBoolean(String key, boolean def) {
		String value = PropUtil.getString(key, "").trim();
		if ("true".equalsIgnoreCase(value))
			return true;

		if ("false".equalsIgnoreCase(value))
			return false;

		return def;
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static int getInt(String key, int defaultValue) {
		String value = getString(key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static String getString(String key, String def) {
		initProperties();
		if (prop != null) {
			return prop.getProperty(key, def).trim();
		}
		return def;
	}

	public static String getString(String key) {
		return PropUtil.getString(key, "");
	}
}
