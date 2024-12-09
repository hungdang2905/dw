package warehouse.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Date_dim {
    private  int id;
    private LocalDate full_date;
    private String day_of_week;
    private String month;
    private String year;
    
	public Date_dim(int id, LocalDate full_date, String day_of_week, String month, String year) {
		super();
		this.id = id;
		this.full_date = full_date;
		this.day_of_week = day_of_week;
		this.month = month;
		this.year = year;
	}
	
	public Date_dim(LocalDate full_date, String day_of_week, String month, String year) {
		super();
		this.full_date = full_date;
		this.day_of_week = day_of_week;
		this.month = month;
		this.year = year;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getFull_date() {
		return full_date;
	}
	public void setFull_date(LocalDate full_date) {
		this.full_date = full_date;
	}
	public String getDay_of_week() {
		return day_of_week;
	}
	public void setDay_of_week(String day_of_week) {
		this.day_of_week = day_of_week;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
    
    
}
