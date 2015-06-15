package test.bean;

import java.util.Date;

public class Work {

	private int id;
	private String workName;
	private int personId;
	private String personName;
	private Date workDate;
	private boolean leader;
	private Date createDate;

	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("id:" + id).append(",");
		sbuff.append("workName:" + workName).append(",");
		sbuff.append("personId:" + personId).append(",");
		sbuff.append("personName:" + personName).append(",");
		sbuff.append("workDate:" + workDate).append(",");
		sbuff.append("isLeader:" + leader).append(",");
		sbuff.append("createDate:" + createDate).append(",");

		return sbuff.toString();
	}

	public boolean isLeader() {
		return leader;
	}

	public void setLeader(boolean isLeader) {
		this.leader = isLeader;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
