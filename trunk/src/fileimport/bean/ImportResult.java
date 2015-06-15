package fileimport.bean;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
	private List successList = new ArrayList();
	private List errorList = new ArrayList();

	/**
	 * ��ӳɹ�����
	 * @param o �Զ������
	 */
	public void addSuccessOne(Object o) {
		this.successList.add(o);
	}

	/**
	 * ���ʧ�ܶ���
	 * @param e 
	 */
	public void addErrorOne(ImportError e) {
		this.errorList.add(e);
	}

	/**
	 * ��ȡ�ɹ��б�
	 * @return
	 */
	public List getSuccessList() {
		return successList;
	}

	/**
	 * ���óɹ��б�
	 * @param successList
	 */
	public void setSuccessList(List successList) {
		this.successList = successList;
	}

	/**
	 * ��ȡʧ���б�
	 * @return
	 */
	public List getErrorList() {
		return errorList;
	}

	/**
	 * ����ʧ���б�
	 * @param errorList
	 */
	public void setErrorList(List errorList) {
		this.errorList = errorList;
	}

	/**
	 * �Ƿ��д�������
	 * @return
	 */
	public boolean hasError() {
		return errorList.size() > 0;
	}

	/**
	 * �Ƿ���ڳɹ�������
	 * @return
	 */
	public boolean hasSuccess() {
		return successList.size() > 0;
	}

	/**
	 * �Ƿ�û���κγɹ�������
	 * @return
	 */
	public boolean hasNoSuccess() {
		return !hasSuccess();
	}

}
