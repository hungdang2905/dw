package script;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mysql.cj.jdbc.CallableStatement;

import config.ConfigProperties;
import dao.ControlDAO;
import dao.ImportFileDAO;
import helper.SendMail;
import model.Config;

public class ImportFile {

	public void loadToStaging(Config config) {
		System.out.println(config.toString());
		ImportFileDAO stagingDao = new ImportFileDAO();
		new ControlDAO().updateStatus(config.getId(), "STAGING_LOADING");
		try {
			stagingDao.truncateTable();
			stagingDao.extractDataToStaging(config.getPathDetail());

		} catch (SQLException e) {
			new ControlDAO().updateStatus(config.getId(), "ERROR");
			SendMail.Send("Log Datawarehouse",
					"Lỗi khi extract data\nId Config: " + config.getId() + "\nNội dung lỗi: " + e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		new ControlDAO().updateStatus(config.getId(), "STAGING_LOADED");
		transformData(config);

	}

	public void transformData(Config config) {
		new ControlDAO().updateStatus(config.getId(), "TRANSFORMING");
		try {
			new ImportFileDAO().transformData();
			
		} catch (SQLException e) {
			e.printStackTrace();
			new ControlDAO().updateStatus(config.getId(), "ERROR");
			SendMail.Send("Log Datawarehouse",
					"Lỗi khi transform data\nId Config: " + config.getId() + "\nNội dung lỗi: " + e.toString());
			return;
		}
		new ControlDAO().updateStatus(config.getId(), "TRANSFORMED");
		loadToDataWarehouse(config);


	}

	public void loadToDataWarehouse(Config config) {
		new ControlDAO().updateStatus(config.getId(), "WH_LOADING");
		try {
			new ImportFileDAO().loadDataToDataWarehouse();

		} catch (SQLException e) {
			new ControlDAO().updateStatus(config.getId(), "ERROR");
			// TODO Auto-generated catch block
			SendMail.Send("Log Datawarehouse",
					"Lỗi khi load data to datawarehouse\nId Config: " + config.getId() + "\nNội dung lỗi: " + e.toString());
			e.printStackTrace();
			return;
		}
		new ControlDAO().updateStatus(config.getId(), "WH_LOADED");
		loadToDataMart(config);
	}

	public void loadToDataMart(Config config) {
		new ControlDAO().updateStatus(config.getId(), "MART_LOADING");
		try {
			new ImportFileDAO().loadDataToAggregate();
			new ImportFileDAO().loadDataToMart();

		} catch (SQLException e) {
			new ControlDAO().updateStatus(config.getId(), "ERROR");
			// TODO Auto-generated catch block
			SendMail.Send("Log Datawarehouse",
					"Lỗi khi load data to mart\nId Config: " + config.getId() + "\nNội dung lỗi: " + e.toString());
			e.printStackTrace();
			return;
		}
		new ControlDAO().updateStatus(config.getId(), "DONE");

	}
	
	
}
