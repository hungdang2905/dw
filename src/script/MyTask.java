package script;

import java.util.Date;
import java.util.TimerTask;

import config.ConfigProperties;
import dao.ControlDAO;
import model.Config;

public class MyTask extends TimerTask {
	@Override
	public void run() {
		RunScript.run();
	}
}