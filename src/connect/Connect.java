package connect;

import java.io.IOException;
import java.lang.module.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.ConfigProperties;

public class Connect {
	private static Connection connection;
	private static Connect connect;

	private Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String JDBC_URL = ConfigProperties.URL_MYSQL;
			String USER = ConfigProperties.USER_DB;
			String PASSWORD = ConfigProperties.PASSWORD_DB;
			connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Connect getInstance() {
		if(connect == null)
			connect = new Connect();
		return connect;
	}
	
	public Connection getConnection() {
		return connection;
	}
	public static void main(String[] args) {
		Connection con = Connect.getInstance().getConnection();
		
	}
}
