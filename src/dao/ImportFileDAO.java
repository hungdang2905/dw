package dao;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import config.ConfigProperties;
import connect.Connect;
import helper.SendMail;
import model.Config;

public class ImportFileDAO {
	private Connection con;

	public ImportFileDAO() {
		super();
		this.con = Connect.getInstance().getConnection();
	}

	public boolean extractDataToStaging(String path) throws SQLException {
		PreparedStatement psLoadData;
		psLoadData = con.prepareStatement(ConfigProperties.LOAD_DATA_TO_STAGING);
		psLoadData.setString(1, path);
		int rs = psLoadData.executeUpdate();
		if (rs > 0)
			return true;
		return false;

	}

	public void truncateTable() {
		try {
			CallableStatement callableStatement = con
					.prepareCall("{CALL " + ConfigProperties.PROCEDURE_TRUNCATE_STAGIGN + "()}");
			callableStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy");
			LocalDateTime nowTime = LocalDateTime.now();
			String timeNow = nowTime.format(dtf);
			String subject = "Error Date: " + timeNow;
			String message = "Error with message: " + e.getMessage();
			SendMail.Send(subject, message);
		}
	}

	public void transformData() throws SQLException {
		CallableStatement callableStatement = con
				.prepareCall("{CALL " + ConfigProperties.PROCEDURE_TRANSFORM_STAGING + "()}");
		callableStatement.execute();

	}
	
	
	public void loadDataToAggregate() throws SQLException {
		CallableStatement callableStatement = con
				.prepareCall("{CALL " + ConfigProperties.LOAD_DATA_TO_AGGREGATE + "()}");
		callableStatement.execute();

	}

	public void loadDataToDataWarehouse() throws SQLException {
		CallableStatement callableStatement = con
				.prepareCall("{CALL " + ConfigProperties.LOAD_DATA_TO_WH + "()}");
		callableStatement.execute();

	}

	public void loadDataToMart() throws SQLException {
		CallableStatement callableStatement = con
				.prepareCall("{CALL " + ConfigProperties.LOAD_DATA_TO_MART + "()}");
		callableStatement.execute();

	}
	
	

	public static void main(String[] args) {

	}

}
