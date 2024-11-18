package model;

import java.util.Date;

public class Config {
	private int id;
	private String nameFile;
	private String description;
	private Date dateLottery;
	private String location;
	private Date createdAt;
	private Date updatedAt;
	private int flag;
	private String status;
	private String pathDetail;
	private String createBy;
	public Config(int id, String nameFile, Date dateLottery , String location, int flag, String status, String pathDetail) {
		super();
		this.id = id;
		this.nameFile = nameFile;
		this.dateLottery = dateLottery;
		this.location = location;
		this.flag = flag;
		this.status = status;
		this.pathDetail = pathDetail;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNameFile() {
		return nameFile;
	}
	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateLottery() {
		return dateLottery;
	}
	public void setDateLottery(Date dateLottery) {
		this.dateLottery = dateLottery;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPathDetail() {
		return pathDetail;
	}
	public void setPathDetail(String pathDetail) {
		this.pathDetail = pathDetail;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Override
	public String toString() {
		return "Config [id=" + id + ", nameFile=" + nameFile + ", description=" + description + ", dateLottery="
				+ dateLottery + ", location=" + location + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", flag=" + flag + ", status=" + status + ", pathDetail=" + pathDetail + ", createBy=" + createBy
				+ "]";
	}
	

}
