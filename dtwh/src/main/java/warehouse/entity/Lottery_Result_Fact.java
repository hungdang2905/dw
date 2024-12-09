package warehouse.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lottery_Result_Fact {
    private int sk;
    private int id;
    private int id_reward;
    private int id_date;
    private int id_province;
    private String special_prize;
    private String first_prize;
    private String second_prize;
    private String third_prize;
    private String fourth_prize;
    private String fifth_prize;
    private String sixth_prize;
    private String seventh_prize;
    private String eighth_prize;
	public int getSk() {
		return sk;
	}
	public void setSk(int sk) {
		this.sk = sk;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_reward() {
		return id_reward;
	}
	public void setId_reward(int id_reward) {
		this.id_reward = id_reward;
	}
	public int getId_date() {
		return id_date;
	}
	public void setId_date(int id_date) {
		this.id_date = id_date;
	}
	public int getId_province() {
		return id_province;
	}
	public void setId_province(int id_province) {
		this.id_province = id_province;
	}
	public String getSpecial_prize() {
		return special_prize;
	}
	public void setSpecial_prize(String special_prize) {
		this.special_prize = special_prize;
	}
	public String getFirst_prize() {
		return first_prize;
	}
	public void setFirst_prize(String first_prize) {
		this.first_prize = first_prize;
	}
	public String getSecond_prize() {
		return second_prize;
	}
	public void setSecond_prize(String second_prize) {
		this.second_prize = second_prize;
	}
	public String getThird_prize() {
		return third_prize;
	}
	public void setThird_prize(String third_prize) {
		this.third_prize = third_prize;
	}
	public String getFourth_prize() {
		return fourth_prize;
	}
	public void setFourth_prize(String fourth_prize) {
		this.fourth_prize = fourth_prize;
	}
	public String getFifth_prize() {
		return fifth_prize;
	}
	public void setFifth_prize(String fifth_prize) {
		this.fifth_prize = fifth_prize;
	}
	public String getSixth_prize() {
		return sixth_prize;
	}
	public void setSixth_prize(String sixth_prize) {
		this.sixth_prize = sixth_prize;
	}
	public String getSeventh_prize() {
		return seventh_prize;
	}
	public void setSeventh_prize(String seventh_prize) {
		this.seventh_prize = seventh_prize;
	}
	public String getEighth_prize() {
		return eighth_prize;
	}
	public void setEighth_prize(String eighth_prize) {
		this.eighth_prize = eighth_prize;
	}
    
}
