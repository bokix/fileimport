package fileimport.bean;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
	private List successList = new ArrayList();
	private List errorList = new ArrayList();

	/**
	 * 添加成功对象
	 * @param o 自定义对象
	 */
	public void addSuccessOne(Object o) {
		this.successList.add(o);
	}

	/**
	 * 添加失败对象
	 * @param e 
	 */
	public void addErrorOne(ImportError e) {
		this.errorList.add(e);
	}

	/**
	 * 获取成功列表
	 * @return
	 */
	public List getSuccessList() {
		return successList;
	}

	/**
	 * 设置成功列表
	 * @param successList
	 */
	public void setSuccessList(List successList) {
		this.successList = successList;
	}

	/**
	 * 获取失败列表
	 * @return
	 */
	public List getErrorList() {
		return errorList;
	}

	/**
	 * 设置失败列表
	 * @param errorList
	 */
	public void setErrorList(List errorList) {
		this.errorList = errorList;
	}

	/**
	 * 是否有错误数据
	 * @return
	 */
	public boolean hasError() {
		return errorList.size() > 0;
	}

	/**
	 * 是否存在成功的数据
	 * @return
	 */
	public boolean hasSuccess() {
		return successList.size() > 0;
	}

	/**
	 * 是否没有任何成功的数据
	 * @return
	 */
	public boolean hasNoSuccess() {
		return !hasSuccess();
	}

}
