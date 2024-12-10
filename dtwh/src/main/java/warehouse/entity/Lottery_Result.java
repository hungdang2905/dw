package warehouse.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Lottery_Result {
    private int id;
    private LocalDate date;
    private String regions;
    private String name_province;
    private String giaiTam;
    private String tienThuong_Tam;
    private String giaiBay;
    private String tienThuong_Bay;
    private String giaiSau;
    private String tienThuong_Sau;
    private String giaiNam;
    private String tienThuong_Nam;
    private String giaiTu;
    private String tienThuong_Tu;
    private String giaiBa;
    private String tienThuong_Ba;
    private String giaiNhi;
    private String tienThuong_Nhi;
    private String giaiNhat;
    private String tienThuong_Nhat;
    private String giaiDacBiet;
    private String tienThuong_DB;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getRegions() {
		return regions;
	}
	public void setRegions(String regions) {
		this.regions = regions;
	}
	public String getName_province() {
		return name_province;
	}
	public void setName_province(String name_province) {
		this.name_province = name_province;
	}
	public String getGiaiTam() {
		return giaiTam;
	}
	public void setGiaiTam(String giaiTam) {
		this.giaiTam = giaiTam;
	}
	public String getTienThuong_Tam() {
		return tienThuong_Tam;
	}
	public void setTienThuong_Tam(String tienThuong_Tam) {
		this.tienThuong_Tam = tienThuong_Tam;
	}
	public String getGiaiBay() {
		return giaiBay;
	}
	public void setGiaiBay(String giaiBay) {
		this.giaiBay = giaiBay;
	}
	public String getTienThuong_Bay() {
		return tienThuong_Bay;
	}
	public void setTienThuong_Bay(String tienThuong_Bay) {
		this.tienThuong_Bay = tienThuong_Bay;
	}
	public String getGiaiSau() {
		return giaiSau;
	}
	public void setGiaiSau(String giaiSau) {
		this.giaiSau = giaiSau;
	}
	public String getTienThuong_Sau() {
		return tienThuong_Sau;
	}
	public void setTienThuong_Sau(String tienThuong_Sau) {
		this.tienThuong_Sau = tienThuong_Sau;
	}
	public String getGiaiNam() {
		return giaiNam;
	}
	public void setGiaiNam(String giaiNam) {
		this.giaiNam = giaiNam;
	}
	public String getTienThuong_Nam() {
		return tienThuong_Nam;
	}
	public void setTienThuong_Nam(String tienThuong_Nam) {
		this.tienThuong_Nam = tienThuong_Nam;
	}
	public String getGiaiTu() {
		return giaiTu;
	}
	public void setGiaiTu(String giaiTu) {
		this.giaiTu = giaiTu;
	}
	public String getTienThuong_Tu() {
		return tienThuong_Tu;
	}
	public void setTienThuong_Tu(String tienThuong_Tu) {
		this.tienThuong_Tu = tienThuong_Tu;
	}
	public String getGiaiBa() {
		return giaiBa;
	}
	public void setGiaiBa(String giaiBa) {
		this.giaiBa = giaiBa;
	}
	public String getTienThuong_Ba() {
		return tienThuong_Ba;
	}
	public void setTienThuong_Ba(String tienThuong_Ba) {
		this.tienThuong_Ba = tienThuong_Ba;
	}
	public String getGiaiNhi() {
		return giaiNhi;
	}
	public void setGiaiNhi(String giaiNhi) {
		this.giaiNhi = giaiNhi;
	}
	public String getTienThuong_Nhi() {
		return tienThuong_Nhi;
	}
	public void setTienThuong_Nhi(String tienThuong_Nhi) {
		this.tienThuong_Nhi = tienThuong_Nhi;
	}
	public String getGiaiNhat() {
		return giaiNhat;
	}
	public void setGiaiNhat(String giaiNhat) {
		this.giaiNhat = giaiNhat;
	}
	public String getTienThuong_Nhat() {
		return tienThuong_Nhat;
	}
	public void setTienThuong_Nhat(String tienThuong_Nhat) {
		this.tienThuong_Nhat = tienThuong_Nhat;
	}
	public String getGiaiDacBiet() {
		return giaiDacBiet;
	}
	public void setGiaiDacBiet(String giaiDacBiet) {
		this.giaiDacBiet = giaiDacBiet;
	}
	public String getTienThuong_DB() {
		return tienThuong_DB;
	}
	public void setTienThuong_DB(String tienThuong_DB) {
		this.tienThuong_DB = tienThuong_DB;
	}


}
