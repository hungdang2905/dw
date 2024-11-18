package model;

import java.util.Date;

public class Log {
	private int id;
	private long id_process;
	private String status;
	private String note;
	private Date create_date;
	@Override
	public String toString() {
		return "Log [id=" + id + ", id_process=" + id_process + ", status=" + status + ", note=" + note
				+ ", create_date=" + create_date + "]";
	}
	public Log(int id, long id_process, String status, String note, Date create_date) {
		super();
		this.id = id;
		this.id_process = id_process;
		this.status = status;
		this.note = note;
		this.create_date = create_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getId_process() {
		return id_process;
	}
	public void setId_process(long id_process) {
		this.id_process = id_process;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
}
