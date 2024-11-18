

import java.util.Calendar;
import java.util.Timer;

import script.MyTask;
import script.RunScript;

public class Run {
	public static void main(String[] args) throws InterruptedException {
		RunScript.run();
		if(args.length >0) {
			if(args[0].trim().toLowerCase().equals("runnow")) {
				RunScript.run();
			}
		}else {
			MyTask myTask = new MyTask();
			Timer timer = new Timer();
			// Lấy thời điểm hiện tại
			Calendar now = Calendar.getInstance();

			// Đặt lịch chạy công việc vào 8 giờ tối
			Calendar scheduledTime = Calendar.getInstance();
			scheduledTime.set(Calendar.HOUR_OF_DAY, 20); // 8 giờ tối
			scheduledTime.set(Calendar.MINUTE, 0);
			scheduledTime.set(Calendar.SECOND, 0);
	        // Nếu thời điểm hiện tại đã qua thời điểm đặt lịch, thì cộng thêm 1 ngày

			if (now.after(scheduledTime)) {
				scheduledTime.add(Calendar.DAY_OF_MONTH, 1);
			}
			// Tính khoảng thời gian delay
	        long delay = scheduledTime.getTimeInMillis() - now.getTimeInMillis();
	     // Đặt lịch chạy công việc
	        System.out.println("Đặt lịch chạy 20:00 mỗi ngày");
	        timer.scheduleAtFixedRate(myTask, delay, 24 * 60 * 60 * 1000); // Lặp lại mỗi 24 giờ
		}
		
		
	}
}
