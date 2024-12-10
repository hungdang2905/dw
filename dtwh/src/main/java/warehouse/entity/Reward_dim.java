package warehouse.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Reward_dim {
    private int id;
    private String special_prize;
    private String eighth_prize;
    private String seventh_prize;
    private String sixth_prize;
    private String fifth_prize;
    private String fourth_prize;
    private String third_prize;
    private String second_prize;
    private String first_prize;
    private LocalDate date;
    private String type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSpecial_prize() {
		return special_prize;
	}
	public void setSpecial_prize(String special_prize) {
		this.special_prize = special_prize;
	}
	public String getEighth_prize() {
		return eighth_prize;
	}
	public void setEighth_prize(String eighth_prize) {
		this.eighth_prize = eighth_prize;
	}
	public String getSeventh_prize() {
		return seventh_prize;
	}
	public void setSeventh_prize(String seventh_prize) {
		this.seventh_prize = seventh_prize;
	}
	public String getSixth_prize() {
		return sixth_prize;
	}
	public void setSixth_prize(String sixth_prize) {
		this.sixth_prize = sixth_prize;
	}
	public String getFifth_prize() {
		return fifth_prize;
	}
	public void setFifth_prize(String fifth_prize) {
		this.fifth_prize = fifth_prize;
	}
	public String getFourth_prize() {
		return fourth_prize;
	}
	public void setFourth_prize(String fourth_prize) {
		this.fourth_prize = fourth_prize;
	}
	public String getThird_prize() {
		return third_prize;
	}
	public void setThird_prize(String third_prize) {
		this.third_prize = third_prize;
	}
	public String getSecond_prize() {
		return second_prize;
	}
	public void setSecond_prize(String second_prize) {
		this.second_prize = second_prize;
	}
	public String getFirst_prize() {
		return first_prize;
	}
	public void setFirst_prize(String first_prize) {
		this.first_prize = first_prize;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
}
