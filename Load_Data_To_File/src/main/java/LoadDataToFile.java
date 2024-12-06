import com.opencsv.CSVWriter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoadDataToFile {
    private Config configReader;
    Connect connection;
    String url;
    String user;
    String password;
    String csv;
    String moduleFile;
    String moduleSuccess;
    String moduleLoad;
    String moduleProcess;
    String csvFilePath;


    public LoadDataToFile(Config configReader) {
        this.configReader = new Config();
        loadConfig();
    }


    //2. Load config module
    public void loadConfig() {
        url = configReader.getProperty("url");
        user = configReader.getProperty("username");
        password = configReader.getProperty("password");
        moduleLoad = configReader.getProperty("Module.LoadDataToFile");
        moduleSuccess = configReader.getProperty("Module.success");
        moduleFile = configReader.getProperty("Module.LogsError");
        moduleProcess = configReader.getProperty("Module.process");
        csv = configReader.getProperty("csv");
        csvFilePath = configReader.getProperty("csvFilePath");
    }

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    public void crawlAndSaveData(String url) {
        try {
            Connection.Response response = Jsoup.connect(url).userAgent(USER_AGENT).execute();
            if (response.statusCode() != 200) {
                System.err.println("Failed to fetch data. HTTP Status Code: " + response.statusCode());
                return;
            }

            String charset = response.charset();
            System.out.println("Charset of the webpage: " + charset);

            Document document = response.parse();
            Elements tables = document.select("table.rightcl");

            List<String[]> allData = new ArrayList<>();

            for (Element table : tables) {
                Elements rows = table.select("tbody tr");

                String province = normalizeProvince(rows.get(0).select("td.tinh a").text());
                String area = normalizeProvince(rows.get(1).select("td.matinh").text());
                String date_lottery = getCurrentDate();

                List<String[]> data = new ArrayList<>();
                for (int i = 2; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    String name_prize = row.select("td").attr("class");
                    String result = row.select("div").text();

                    String created_at = getCurrentTimestamp();
                    String updated_at = getCurrentTimestamp();
                    String created_by = "YourCreatedBy";
                    String updated_by = "YourUpdatedBy";

                    String[] rowData = {province, area, date_lottery, name_prize, result, created_at, updated_at, created_by, updated_by};
                    data.add(rowData);
                }

                allData.addAll(data);
            }

            String csvFile = "XOSO_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date());
            String filePath = csvFilePath + File.separator + csvFile + ".csv";
            System.out.println("File path: " + filePath);

            // Ensure the directory exists, create if not
            File directory = new File(csvFilePath);
            if (!directory.exists()) {
                directory.mkdirs();  // Creates the directory if it doesn't exist
            }

            // 8. chạy saveToCSV()
            saveToCSV(allData, filePath);

            System.out.println("Data successfully crawled and saved to CSV file");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to crawl data from source: " + e.getMessage());
        }
    }

    private static String normalizeProvince(String input) {
        return input.replaceAll("\\p{M}", ""); // Remove diacritics
    }

    private static void saveToCSV(List<String[]> data, String filePath) {
        //9 Check success
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.ISO_8859_1))) {
            // Write CSV header
            writer.writeNext(new String[]{"Province", "area", "date_lottery", "name_prize", "result", "created_at", "updated_at", "created_by", "updated_by"});
            System.out.println("Saving data to CSV...");
            for (String[] rowData : data) {
                String[] results = rowData[4].split("\\s+");

                for (String result : results) {
                    String[] newRowData = {
                            normalizeProvince(rowData[0]),  // province
                            rowData[1],  // area
                            rowData[2],  // date_lottery
                            rowData[3],  // name_prize
                            result,      // result
                            rowData[5],  // created_at
                            rowData[6],  // updated_at
                            rowData[7],  // created_by
                            rowData[8]   // updated_by
                    };
                    writer.writeNext(newRowData);

                }

            }
            System.out.println("Data successfully saved to CSV file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            //9.1 insert record into logs with writeLog()
            System.err.println("Failed to save data to CSV: " + e.getMessage());
        }
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private static String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private void udpateStatusDataFiles() {
        java.sql.Connection connect = connection.getConnection();
        String query = "UPDATE data_files SET status = ?, note = ?, updated_at = current_date(), updated_by = ? WHERE id = ?";
        // 10. check update successfully status to table data_files
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1, moduleLoad);
            pre.setString(2, moduleSuccess);
            pre.setString(3, "root");
            pre.setInt(4, 1);
            pre.executeUpdate();
            pre.close();
            String logFilePath = configReader.getProperty("Module.LogsError");

            connection.writeLogToFile(logFilePath, "ERROR", "Invalid file path");
        } catch (Exception e) {
            e.printStackTrace();
            // 10.1 insert record into logs wite writeLog()
            writeLog(e.getMessage());
        }
    }

    public void insertLogProcess() {
        java.sql.Connection connect = connection.getConnection();
        String query = "INSERT INTO Logs(event, status) VALUES (?, ?)";
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1, moduleLoad);
            pre.setString(2, moduleProcess);
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            writeLog(e.getMessage());
        }
    }
    // 2.3 add source path to table data_file_configs
    private void insertSourcePath(String sourcePath) {
        String csvFile = "XOSO_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date());
        String location = csvFilePath + File.separator + csvFile + ".csv";
        java.sql.Connection connect = connection.getConnection();
        String query = "INSERT INTO data_file_configs(name,source_path, location)" +
                "VALUES (?, ?, ?)";
        // 6. check success
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1, moduleLoad);
            pre.setString(2, sourcePath);
            pre.setString(3, location);
            pre.executeUpdate();
            pre.close();
        } catch (Exception e) {
            // 6.1 insert record into logs with writeLog()
            writeLog(e.getMessage());
        }
    }

    // 2.4 add news row to table data_files with id of data_file_configs
    public void insertDataFiles() {
        connection = new Connect(url, user, password);
        String query = "INSERT INTO data_files(name, row_count, status, note, created_at, updated_at, created_by, updated_by)" +
                "VALUES (?, ?, ?, ?, current_date(), current_date(), ?, ?)";

        // 7. check success
        try {
            PreparedStatement pre = connection.getConnection().prepareStatement(query);
            String csvFile = "XOSO_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date());
            String filePath = csvFilePath + File.separator + csvFile + ".csv";
            // Replace "csv" with the actual variable or value you intend to use
            String csvValue = "XOSO_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date());
            pre.setString(1, csvValue);
            pre.setInt(2, countCSVLines(filePath));
            pre.setString(3, moduleLoad);
            pre.setString(4, moduleSuccess);
            pre.setString(5, "root");
            pre.setString(6, "root");

            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            writeLog(e.getMessage());
        }
    }

    private int countCSVLines(String filePath) {
        int lineCount = 0;
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return 0; // hoặc xử lý theo cách bạn muốn, như ném lỗi
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }


    private void writeLog(String errorMessage) {
        java.sql.Connection connect = connection.getConnection();
        String query = "INSERT INTO logs (event, status, error_message) VALUES (?, ?, ?)";
        try {
            PreparedStatement pre = connect.prepareStatement(query);
            pre.setString(1, moduleFile);
            pre.setString(2, "ERROR");
            pre.setString(3, errorMessage);
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String loadFilePath() {

        String filePath = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (filePath == null || filePath.isEmpty()) {
            connection.writeLogToFile(moduleFile, "error", "Invalid file path");
        } else {
            filePath = csvFilePath;
            System.out.println(filePath);
        }
        return filePath;
    }

    public void run() {
        // 3. Connect database control
        connection = new Connect(url, user, password);
        try {
            //4. Checking connection to database control
            java.sql.Connection connect = connection.getConnection();
            if (connect == null) {
                // 4.1 insert record into logs with writeLogToFile()
                connection.writeLogToFile(moduleFile, "error", "Lỗi kết nối");
                return;
            }

            //4.2 Insert status "PROCESSING" into Log Process
            insertLogProcess();
            Statement statement = connect.createStatement();
            String sourcePath = "https://www.minhngoc.net.vn/xo-so-truc-tiep/mien-nam.html";
            //5 insert source_path to data_file_configs
            insertSourcePath(sourcePath);
            //6.2 insert news row in data_files with id of data_file_configs
            insertDataFiles();
            //7.2. run phương thức crawlAndSaveData(sourcePath)
            crawlAndSaveData(sourcePath);
            //9.2 update data_files status
            udpateStatusDataFiles();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Config config = new Config();
        LoadDataToFile dataLoader = new LoadDataToFile(config); // Assuming Config class exists
        System.out.println(dataLoader.csvFilePath);
        dataLoader.run();

    }
}





