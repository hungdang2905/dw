package dto;

import java.util.ArrayList;

public class KQSX {
	private String province;
	private String area;
	private String dateLottery;
	private String namePrize;
	private String result;
	public KQSX(String province, String area, String dateLottery, String namePrize, String result) {
		super();
		this.province = province;
		this.area = area;
		this.dateLottery = dateLottery;
		this.namePrize = namePrize;
		this.result = result;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDateLottery() {
		return dateLottery;
	}
	public void setDateLottery(String dateLottery) {
		this.dateLottery = dateLottery;
	}
	public String getNamePrize() {
		return namePrize;
	}
	public void setNamePrize(String namePrize) {
		this.namePrize = namePrize;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
	
}
