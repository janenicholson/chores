package nz.net.chaosanddarkness.chores;

import java.util.Date;
import java.util.Optional;

import com.asana.models.Task;
import com.google.api.client.util.DateTime;

import lombok.RequiredArgsConstructor;
import nz.net.chaosanddarkness.chores.asana.AsanaReader;
import nz.net.chaosanddarkness.chores.asana.AsanaUpdater;

@RequiredArgsConstructor
public class ResetDailyChores {
	private final AsanaReader asanaReader;
	private final AsanaUpdater asanaUpdater;

	public void resetSection(String sectionId) {
		asanaReader.getTasksBySection(sectionId).stream()
				.map(this::getDetails)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(this::isComplete)
				.map(this::getId)
				.forEach(this::dueToday);
	}

	private Optional<Task> getDetails(Task task) {
		return asanaReader.getTask(task.id);
	}

	private boolean isComplete(Task task) {
		return task.completed;
	}

	private String getId(Task task) {
		return task.id;
	}

	private void dueToday(String taskId) {
		asanaUpdater.refreshTask(taskId, new DateTime(true, new Date().getTime(), 0));
	}
}
