package nz.net.chaosanddarkness.chores.asana;

import java.io.IOException;
import java.util.Date;

import com.asana.Client;
import com.google.api.client.util.DateTime;

public class AsanaUpdater {
	private final Client asana;

	public AsanaUpdater(String token) {
		asana = Client.accessToken(token);
	}

	public void refreshTask(String taskid) {
		String dueOn = new DateTime(true, new Date().getTime(), 0).toStringRfc3339();
		try {
			asana.tasks.update(String.format("%s?completed=false&due_on=%s", taskid, dueOn)).execute();
		} catch (IOException e) {
		}
	}

}
