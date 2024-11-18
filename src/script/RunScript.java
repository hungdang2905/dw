package script;

import java.util.Date;

import config.ConfigProperties;
import dao.ControlDAO;
import model.Config;

public class RunScript {
	public static void run() {
		ControlDAO control = new ControlDAO();
		long idProcess = new Date().getTime();
		int count = 0;
		boolean checkProcess = control.getLogRunning();
		while (count++ < ConfigProperties.MAX_ITERATOR) {
			if (!checkProcess)
				break;
			System.out.println("Chờ tiến trình khác đang chạy hoàn thành");
			try {
				Thread.sleep(ConfigProperties.TIMEOUT_WAITING);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			checkProcess = control.getLogRunning();
		}
		if (checkProcess) {
			control.insertLog(idProcess, "END", "Có một process khác đang chạy");
			return;
		}
		String note = "Bắt đầu thực thi: " + new Date().toLocaleString();
		control.insertLog(idProcess, "PROCESSING", note);

		for (Config tmp : new ControlDAO().getConfigWithFlagTrue()) {
			System.out.println("--------------------" + tmp.getId() + " " + tmp.getStatus());
			switch (tmp.getStatus()) {
			case "": {
				new Scrapping().getData(tmp);
				break;
			}
			case "DONE": {
				new Scrapping().getData(tmp);
				break;

			}
			case "CRAWLED": {
				new ImportFile().loadToStaging(tmp);
				break;

			}
			case "STAGING_LOADED": {
				new ImportFile().transformData(tmp);
				break;

			}
			case "TRANSFORMED": {
				new ImportFile().loadToDataWarehouse(tmp);
				break;

			}
			case "WH_LOADED": {
				new ImportFile().loadToDataMart(tmp);
				break;

			}

			}
		}
		note += " - Đã thực thi xong: " + new Date().toLocaleString();
		System.out.println(note);

		control.updateLog(idProcess, "PROCESSED", note);
	}
}
