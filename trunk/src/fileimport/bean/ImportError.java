package fileimport.bean;

public class ImportError {
	private int rowIndex;
	private String errorMsg="";
	private String xlsColumnName="";
	private String javaBeanPropertyName="";
	private String xlsValue="";

	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("rowIndex:" + rowIndex).append(",");
		sbuff.append("errorMsg:" + errorMsg).append(",");
		sbuff.append("xlsColumnName:" + xlsColumnName).append(",");
		sbuff.append("javaBeanPropertyName:" + javaBeanPropertyName).append(
				",");
		sbuff.append("xlsValue:" + xlsValue).append(",");

		return sbuff.toString();
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getXlsColumnName() {
		return xlsColumnName;
	}

	public void setXlsColumnName(String xlsColumnName) {
		this.xlsColumnName = xlsColumnName;
	}

	public String getJavaBeanPropertyName() {
		return javaBeanPropertyName;
	}

	public void setJavaBeanPropertyName(String javaBeanPropertyName) {
		this.javaBeanPropertyName = javaBeanPropertyName;
	}

	public String getXlsValue() {
		return xlsValue;
	}

	public void setXlsValue(String xlsValue) {
		this.xlsValue = xlsValue;
	}

}
