package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import config.ConfigProperties;
import connect.Connect;
import model.Config;
import script.Scrapping;

public class ControlDAO {
	private Connection con;

	public ControlDAO() {
		super();
		this.con = Connect.getInstance().getConnection();
	}
	
	public int updateStatus(int config, String status) {
		try {
			PreparedStatement ps=con.prepareStatement(ConfigProperties.UPDATE_STATUS_CONFIG);
			ps.setString(1, status);
			ps.setInt(2, config);
			int affect=ps.executeUpdate();
			return affect;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public int insertLog(long idProcess, String status, String note) {
		try {
			PreparedStatement ps=con.prepareStatement(ConfigProperties.INSERT_LOG_CONFIG);
			ps.setLong(1, idProcess);
			ps.setString(2, status);
			ps.setString(3, note);
			int affect=ps.executeUpdate();
			return affect;
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public int updateLog(long idProcess, String status, String note) {
		try {
			PreparedStatement ps=con.prepareStatement(ConfigProperties.UPDATE_LOG_CONFIG);
			ps.setString(1, status);
			ps.setString(2, note);
			ps.setLong(3, idProcess);
			int affect=ps.executeUpdate();
			return affect;
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public int updatePathDetail(int config, String path) {
		try {
			PreparedStatement ps=con.prepareStatement(ConfigProperties.UPDATE_PATH_CONFIG);
			ps.setString(1, path);
			ps.setInt(2, config);
			int affect=ps.executeUpdate();
			return affect;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public boolean getLogRunning(){
		try {
			PreparedStatement ps=con.prepareStatement(ConfigProperties.SELECT_LOG_BY_RUNNING);
			ResultSet rs=ps.executeQuery();
			rs.next();
			return rs.getInt(1) >0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
	
	public List<Config> getConfigWithFlagTrue(){
		try {
			PreparedStatement ps=con.prepareStatement(ConfigProperties.SELECT_CONFIG_FLAG_TRUE);
			ResultSet rs=ps.executeQuery();
			ArrayList<Config> configs = new ArrayList<>();
			while (rs.next()) {
				Config config = new Config(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7));
				configs.add(config);
			}
			return configs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	public static void main(String[] args) {
		
		
	}
	
}
