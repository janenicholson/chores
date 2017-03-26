package nz.net.chaosanddarkness.chores.asana;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.asana.Client;

public class AsanaUpdater {
	private final Client asana;

	public AsanaUpdater(String token) {
		asana = Client.accessToken(token);
	}

	// provided for tests that don't want to update a live thing
	AsanaUpdater(Client asana) {
		this.asana = asana;
	}

	public void refreshTask(String taskId, LocalDate dueDate) {
		String dueOn = dueDate.format(DateTimeFormatter.ISO_DATE);
		try {
			asana.tasks.update(String.format("%s?completed=false&due_on=%s", taskId, dueOn)).execute();
		} catch (IOException e) {
		}
	}

}
