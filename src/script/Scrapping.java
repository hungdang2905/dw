package script;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dao.ControlDAO;
import dao.ImportFileDAO;
import dto.KQSX;
import helper.ConvertDate;
import helper.SendMail;
import model.Config;

public class Scrapping {

	public void getData(Config config) {
		ControlDAO dao = new ControlDAO();
		//11. Cập nhật config.status = "CRAWLING"
		dao.updateStatus(config.getId(), "CRAWLING");
		String date = ConvertDate.ToString(config.getDateLottery());
		try {
			ArrayList<KQSX> data = new ArrayList<>();
			//12. Gọi hàm scrapping và crappingMB sử dụng jsoup lấy dữ liệu MN, MB, MT theo config.date_lottery
			data.addAll(scrapping(config, date, "https://www.minhngoc.net.vn/doi-so-trung/mien-nam/", "MN"));
			data.addAll(scrappingMB(config, date, "https://www.minhngoc.net.vn/doi-so-trung/mien-bac/"));
			data.addAll(scrapping(config, date, "https://www.minhngoc.net.vn/doi-so-trung/mien-trung/", "MT"));
			//13. Lưu lại file với đường dẫn config.location + "\\" + config.name + "-" + config.date_lottery + ".csv" và lưu đường dẫn đó vào biến path.
			String path = writeFile(data, config.getLocation(), config.getNameFile());
			//14. Cập nhật config.path_detail = "Đường dẫn file(biến path)"
			dao.updatePathDetail(config.getId(), path);

		} catch (Exception e) {
			//15. Kiểm tra quá trình thực thi
			//16. Cập nhật cofig.status = "ERROR"
			new ControlDAO().updateStatus(config.getId(), "ERROR");
			//17. Gửi mail báo lỗi và config.id
			SendMail.Send("Log Datawarehouse",
					"Lỗi khi thu thập dữ liệu xổ số\nId Config: " + config.getId() + "\nNội dung lỗi: " + e.toString());
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		//15. Kiểm tra quá trình thực thi
		//18. Cập nhật config.status = "CRAWED"
		dao.updateStatus(config.getId(), "CRAWLED");
		//19. Gọi hàm loadToStaging(config)
		new ImportFile().loadToStaging(config);
	}

	public List<KQSX> scrappingMB(Config config, String date, String url) throws Exception {
		String area = "MB";
		Document doc = null;
		LocalDateTime now = LocalDateTime.now();
		Timestamp time = Timestamp.valueOf(now);

		List<KQSX> result = new ArrayList<>();
		doc = Jsoup.connect(url + date + ".html").timeout(10 * 1000).userAgent("Jsoup client").timeout(20000).get();
		String syntax = "#noidung .box_kqxs .content>table>tbody";
		Elements list = doc.select(syntax);
		Element eDateLottery = doc.select("#getngaykqxs_1").first();
		String dateLottery = eDateLottery.attr("value");
		System.out.println(url + dateLottery + ".html");

		// System.out.println(countAward);
		// System.out.println(countProvince);
		// thong tin chung
		String titleTable = doc.select("#noidung .box_kqxs>.top>.bkl>.bkr>.bkm>.title").html();
		// String dateOfWeek = doc.select("#noidung
		// .box_kqxs>.content>table>tbody>tr:eq(0)>td:eq(0)").html();
		String province = getProvince(titleTable);
		for (Element e : list) {
			for (int i = 1; i <= 8; i++) {
				String award = doc.select("#noidung .box_kqxs:eq(1) .content>table>tbody>tr:eq(" + i + ")>td:eq(0)")
						.get(0).text();
				if (award.contains("ĐB"))
					award = award.replace("ĐB", "Đặc Biệt");
				// System.out.println(award);
				Elements resultTable = e.select("tr:" + "eq(" + i + ")>td:eq(1)>div");
				for (Element r : resultTable) {
					String numberResult = r.html();
					if (numberResult.isEmpty())
						continue;
					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					
					KQSX kqsx = new KQSX(province, area, dateLottery, award, numberResult);
					result.add(kqsx);
				}
			}
		}
		return result;
	}

	private String getProvince(String titleTable) {
		String[] tokens = titleTable.split("-");
		String title = tokens[2].trim();
		String[] strings = title.split(" ");
		String province = strings[2] + " " + strings[3];
		return province;
	}

	public List<KQSX> scrapping(Config config, String date, String url, String area) throws Exception {
		Document doc = null;
		LocalDate now2 = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime now = LocalDateTime.now();
		Timestamp time = Timestamp.valueOf(now);
		List<KQSX> result = new ArrayList<>();
		doc = Jsoup.connect(url + date + ".html").timeout(10 * 1000).userAgent("Jsoup client").timeout(20000).get();
		String syntax = "#noidung .box_kqxs:eq(1) .content>table>tbody>tr>td:eq(1)>table>tbody>tr>td";
		Elements list = doc.select(syntax);
		if (list.size() == 0) {
			return new ArrayList<>();
		}
		Element eDateLottery = doc.select("#getngaykqxs_1").first();
		String dateLottery = eDateLottery.attr("value");
		System.out.println(url + dateLottery + ".html");

		int pivot = list.size() - 1;
		if (list.get(list.size() - 2).select("table tbody tr:eq(0)>.tinh>a").size() == 0) {
			pivot = pivot - 1;
		}
		list = doc.select(syntax + ":lt(" + pivot + ")");
		Elements listValueAward = doc.select(
				"#noidung .box_kqxs:eq(1) .content>table>tbody>tr>td:eq(1)>table>tbody>tr>td:eq(" + pivot + ")");
		for (Element e : list) {
			String province = e.select("table tbody tr:eq(0)>.tinh a").html();
			for (int i = 2; i <= 10; i++) {
				String award = doc.select(
						"#noidung .box_kqxs:eq(1) .content>table>tbody>tr>td:eq(0)>table>tbody>tr:eq(" + i + ")").get(0)
						.text();
				Elements resultTable = e.select("table tbody tr:" + "eq(" + i + ")>td>div");
				for (Element r : resultTable) {
					String numberResult = r.html();

					if (numberResult.isEmpty())
						continue;
					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					KQSX kqsx = new KQSX(province, area, dateLottery, award, numberResult);

					result.add(kqsx);

				}
			}

		}
		return result;
	}

	private String writeFile(List<KQSX> kqxss, String location, String fileName) throws IOException {
		Date date = new Date();
		fileName += "-" + kqxss.get(0).getDateLottery();
		String pathString = location + "\\" + fileName + ".csv";
		Path path = Paths.get(location);
		Files.createDirectories(path);
		FileWriter fw = new FileWriter(pathString);
		int no = 1;
		fw.write("province,area,date_lottely,name_prize,result\n");
		StringBuffer b = new StringBuffer();
		for (KQSX m : kqxss) {
			b.append(m.getProvince());
			b.append(",");
			b.append(m.getArea());
			b.append(",");
			b.append(m.getDateLottery());
			b.append(",");
			b.append(m.getNamePrize());
			b.append(",");
			b.append(m.getResult());
			b.append("\n");
		}
		fw.write(b.toString());
		fw.close();
		return pathString;
	}

	public static void main(String[] args) {

	}

}
