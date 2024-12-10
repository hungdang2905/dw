package warehouse.dao.datawarehouse;

import loadToDatamart.entity.LotteryResult;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;
import warehouse.Services.EmailUtils;
import warehouse.dao.ControlDAO;
import warehouse.dao.DBContext;
import warehouse.dao.LogDAO;
import warehouse.dao.staging.LotteryResultDAOStaging;
import warehouse.entity.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class LotteryDAOWareHouse {

    private boolean isRewardIdValid(Handle handle, int rewardId) {
        String checkRewardExistenceQuery = "SELECT COUNT(*) FROM reward_dim WHERE id = ?";
        int rewardCount = handle.createQuery(checkRewardExistenceQuery)
                .bind(0, rewardId)
                .mapTo(Integer.class)
                .findOnly();
        return rewardCount > 0;
    }

    public void insertDateDim(Date_dim dateDim) {
        try (Handle handle = DBContext.connectWarehouse().open()) {
            String query = "INSERT INTO date_dim (full_date, day_of_week, month, year) VALUES (?, ?, ?, ?)";
            int rowsAffected = handle.createUpdate(query)
                    .bind(0, dateDim.getFull_date())
                    .bind(1, dateDim.getDay_of_week())
                    .bind(2, dateDim.getMonth())
                    .bind(3, dateDim.getYear())
                    .execute();
            if (rowsAffected > 0) {
                System.out.println("Inserted successfully into DateDim.");
            }
        }
    }

    public void insertProvince(Province_Dim provinceDim) {
        try (Handle handle = DBContext.connectWarehouse().open()) {
            handle.begin();
            try {
                String query = "INSERT INTO province_dim (name, region) VALUES (?, ?)";
                int rowsAffected = handle.createUpdate(query)
                        .bind(0, provinceDim.getName())
                        .bind(1, provinceDim.getRegion())
                        .execute();
                if (rowsAffected > 0) {
                    handle.commit();
                    System.out.println("Inserted successfully into ProvinceDim.");
                } else {
                    handle.rollback();
                    System.err.println("Failed to insert into ProvinceDim.");
                }
            } catch (Exception e) {
                handle.rollback();
                e.printStackTrace();
            }
        }
    }

    public int getIdDate(LocalDate date) {
        try (Handle handle = DBContext.connectWarehouse().open()) {
            String query = "SELECT id FROM date_dim WHERE full_date = ?";
            return handle.createQuery(query)
                    .bind(0, date)
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElse(0);
        }
    }

    public int getIdProvince(String name) {
        try (Handle handle = DBContext.connectWarehouse().open()) {
            String query = "SELECT id FROM province_dim WHERE name = ?";
            return handle.createQuery(query)
                    .bind(0, "Xổ số " + name)
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElse(0);
        }
    }

    public int getReward(String type) {
        try (Handle handle = DBContext.connectWarehouse().open()) {
            String query = "SELECT id FROM reward_dim WHERE type = ?";
            return handle.createQuery(query)
                    .bind(0, type)
                    .mapTo(Integer.class)
                    .findFirst()
                    .orElse(-1);
        }
    }

    private void insertLotteryResult(Lottery_Result_Fact result) {
        try (Handle handle = DBContext.connectWarehouse().open()) {
            if (isRewardIdValid(handle, result.getId_reward())) {
                String query = "INSERT INTO lottery_result_fact " +
                        "(id_reward, id_date, id_province, special_prize, first_prize, second_prize, " +
                        "third_prize, fourth_prize, fifth_prize, sixth_prize, seventh_prize, eighth_prize) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                handle.createUpdate(query)
                        .bind(0, result.getId_reward())
                        .bind(1, result.getId_date())
                        .bind(2, result.getId_province())
                        .bind(3, result.getSpecial_prize())
                        .bind(4, result.getFirst_prize())
                        .bind(5, result.getSecond_prize())
                        .bind(6, result.getThird_prize())
                        .bind(7, result.getFourth_prize())
                        .bind(8, result.getFifth_prize())
                        .bind(9, result.getSixth_prize())
                        .bind(10, result.getSeventh_prize())
                        .bind(11, result.getEighth_prize())
                        .execute();
                System.out.println("Inserted successfully into LotteryResult.");
            } else {
                System.err.println("Invalid Reward ID.");
            }
        }
    }

    public void transferLotteryResultData() {
        LocalDate dateToUse = LocalDate.now();
        if (LocalTime.now().getHour() < 17) {
            dateToUse = LocalDate.now().minusDays(1);
        }

        if (!new LogDAO().isLastLogStatusRunning("xosohomnay", "Load Data From Staging to Data Warehouse", "Success")) {
            new LogDAO().insertLog("xosohomnay", "Load Data From Staging to Data Warehouse", "Start");

            LotteryResultDAOStaging stagingDAO = new LotteryResultDAOStaging();
            List<Lottery_Result> stagingData = stagingDAO.getAllStagingData(dateToUse);

            for (Lottery_Result stagingResult : stagingData) {
                int id_date = getIdDate(stagingResult.getDate());
                if (id_date == 0) {
                    Date_dim dateDim = new Date_dim(
                            stagingResult.getDate(),
                            stagingResult.getDate().getDayOfWeek().toString(),
                            stagingResult.getDate().getMonth().toString(),
                            String.valueOf(stagingResult.getDate().getYear())
                    );
                    insertDateDim(dateDim);
                    id_date = getIdDate(stagingResult.getDate());
                }

                int id_province = getIdProvince(stagingResult.getName_province());
                if (id_province == 0) {
                    String region = determineRegion(stagingResult.getRegions());
                    insertProvince(new Province_Dim("Xổ số " + stagingResult.getName_province(), region));
                    id_province = getIdProvince(stagingResult.getName_province());
                }

                int id_reward = getReward(determineRewardType(stagingResult.getRegions()));
                if (id_date > 0 && id_province > 0 && id_reward > 0) {
                    Lottery_Result_Fact resultFact = new Lottery_Result_Fact();
                    resultFact.setId_date(id_date);
                    resultFact.setId_province(id_province);
                    resultFact.setId_reward(id_reward);
                    resultFact.setSpecial_prize(stagingResult.getGiaiDacBiet());
                    resultFact.setEighth_prize(stagingResult.getGiaiTam());
                    resultFact.setSeventh_prize(stagingResult.getGiaiBay());
                    resultFact.setSixth_prize(stagingResult.getGiaiSau());
                    resultFact.setFifth_prize(stagingResult.getGiaiNam());
                    resultFact.setFourth_prize(stagingResult.getGiaiTu());
                    resultFact.setThird_prize(stagingResult.getGiaiBa());
                    resultFact.setSecond_prize(stagingResult.getGiaiNhi());
                    resultFact.setFirst_prize(stagingResult.getGiaiNhat());
                    insertLotteryResult(resultFact);
                }
            }

            new LogDAO().insertLog("xosohomnay", "Load Data From Staging to Data Warehouse", "Success");
        }
    }

    private String determineRegion(String regions) {
        if (regions.toLowerCase().contains("miền nam")) return "Miền Nam";
        else if (regions.toLowerCase().contains("miền trung")) return "Miền Trung";
        else return "Miền Bắc";
    }

    private String determineRewardType(String regions) {
        return (regions.toLowerCase().contains("miền nam") || regions.toLowerCase().contains("miền trung")) ? "1" : "0";
    }
}
