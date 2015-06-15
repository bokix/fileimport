package fileimport.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MsgUtil {
	private static Log log = LogFactory.getLog(MsgUtil.class);
	private static ResourceBundle bundle = ResourceBundle
			.getBundle("fileimport_msg");

	public static String getMsg(String key) {
		String s = "";
		try {
			s = bundle.getString(key);
		} catch (Exception e) {
			log.error("get msg error. key:" + key, e);
			return s;
		}
		return s;
	}

	public static String getMsg(String key, Object[] arg) {
		return MessageFormat.format(getMsg(key), arg);
	}

	public static void main(String[] args) {
		String s = getMsg("test");
		System.out.println(s);
	}
}
