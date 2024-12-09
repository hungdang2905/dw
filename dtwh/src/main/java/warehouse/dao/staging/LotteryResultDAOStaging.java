package warehouse.dao.staging;

import warehouse.dao.DBContext;
import warehouse.entity.Lottery_Result;
import org.jdbi.v3.core.Handle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class LotteryResultDAOStaging {
	public List<Lottery_Result> getAllStagingData(LocalDate date) {
        try (Handle handle = DBContext.connectStaging().open()) {
            String query = "SELECT * FROM staging.lottery_result WHERE date = ?";
            return handle.createQuery(query)
                    .bind(0, date)
                    .mapToBean(Lottery_Result.class)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
	public static void main(String[] args) {
     
    }
}
