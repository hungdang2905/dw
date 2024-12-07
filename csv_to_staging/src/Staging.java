import org.w3c.dom.Text;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

public class Staging {
    public static void staging() throws SQLException, IOException {
        Connection conn = null;
        PreparedStatement pre_control = null;
        String link = ".\\config\\config.properties";
        // 1. Đọc file config.properties

        try (InputStream input = new FileInputStream(link)){
            Properties prop = new Properties();
            prop.load(input);
            // 2. Kết nối db control
            conn = new GetConnection().getConnection("control");
            try {
                // 3. Tìm các hàng có result Loading, phase STAGING và is_delete 0
                ResultSet re = checResult(conn, pre_control, "Thành công", "source to csv", false );
                // 4. Tìm các tiến trình đang chạy
                if(re.next()){
                    // 4.1 Thông báo
                    System.out.println("Currently, there is another process at work.");
                }else {
                    // 5. Tìm các hàng có result Sucess, phase CSV và is_delete 0
                         re = checResult(conn, pre_control, "Thành công", "source to csv", false   );
                         int id;
                         String filename = null;

            }
        } catch (SQLException e) {
                // TODO Auto-generated catch block
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            // 9. Đóng kết nối db
            pre_control.close();
            conn.close();
        }catch (IOException ex) {
            // 1.1 Thông báo không tìm thấy file
            System.out.println("Unknown file " + link);
            // 1.2 Log file
            new GetConnection().logFile("Unknown file " + link + "\n" + ex.getMessage());
            System.exit(0);
        }
    }
    public static ResultSet checResult(Connection conn, PreparedStatement pre_control, String result, String phase, boolean is_delete) throws SQLException{
        pre_control = conn.prepareStatement("SELECT * FROM log WHERE date(tracking_date_time)=? and result = ? AND phase =? AND is_delete = ?");
        pre_control.setString(1, LocalDateTime.now().toLocalDate().toString());
        pre_control.setString(2,result);
        pre_control.setString(3,phase);
        pre_control.setBoolean(4,is_delete);

        return pre_control.executeQuery();
    }

    public static void writeLog(Connection dbConn, String phase, String result, String detail) throws SQLException {

        // Insert log record into the database
        PreparedStatement stmt = dbConn.prepareStatement(
                "INSERT INTO log (tracking_date_time, source, connect_status, destination, phase, result, detail) VALUES (?, ?, ?, ?, ?, ?, ?)");
        stmt.setString(1, LocalDateTime.now().toString());
        stmt.setString(2, "D://DW/DDMMYYYY.csv");
        stmt.setInt(3, 1);
        stmt.setString(4, "db.staging");
        stmt.setString(5, phase);
        stmt.setString(6, result);
        stmt.setString(7, detail);

        stmt.executeUpdate();
    }
    public static void writeToStagingDB(Connection dbConn,String province, String prize_eight, String prize_seven, String prize_six, String prize_five, String prize_four, String prize_three, String prize_two, String prize_one, String prize_special) throws SQLException {

        // Insert log record into the database
        PreparedStatement stmt = dbConn.prepareStatement(
                "INSERT INTO lottery (province, prize_eight, prize_seven,  prize_six, prize_five,  prize_four, prize_three, prize_two,  prize_one,  prize_special,date) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?)");
        stmt.setString(1, province);
        stmt.setString(2, prize_eight);
        stmt.setString(3, prize_seven);
        stmt.setString(4, prize_six);
        stmt.setString(5, prize_five);
        stmt.setString(6, prize_four);
        stmt.setString(7, prize_three);
        stmt.setString(8, prize_two);
        stmt.setString(9, prize_one);
        stmt.setString(10, prize_special);
        stmt.setString(11, LocalDateTime.now().toString());



        stmt.executeUpdate();
    }
    public static void main(String[] args) throws FileNotFoundException {
        Connection conn = null;
        PreparedStatement pre_control = null;
        String link = ".\\config\\config.properties";
        // 1. Đọc file config.properties
        try (InputStream input = new FileInputStream(link)) {
            Properties prop = new Properties();
            prop.load(input);

            // 2. Kết nối db control

            conn = new GetConnection().getConnection("control");
            writeLog(conn,"connect_db_staging", "Thành công", "Kết nối db control thành công");

            ResultSet re = checResult(conn, pre_control, "Thành công", "source to csv", false );

            if(re.next()){
                // 4.1 Thông báo
                System.out.println("Kiểm tra tiến trình source to csv thành công");
                writeLog(conn, "check_csv","Tồn tại","Đã có file csv hôm nay");

                String filePath = "D:\\DW\\"+LocalDateTime.now().getDayOfMonth()+LocalDateTime.now().getMonthValue()+LocalDateTime.now().getYear()+"_xsmn.csv";
//                String filePath = "D:\\DW\\6122024_xsmn.csv ";
                File file = new File(filePath);

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    br.readLine();
                    conn = new GetConnection().getConnection("staging");
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split(",");
                        writeToStagingDB(conn,data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn = new GetConnection().getConnection("control");
                writeLog(conn, "csv to staging","Thành công","csv to staging completed");

            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
