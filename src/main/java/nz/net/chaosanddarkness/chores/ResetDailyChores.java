package nz.net.chaosanddarkness.chores;

import java.util.Optional;

import com.asana.models.Task;

import lombok.RequiredArgsConstructor;
import nz.net.chaosanddarkness.chores.asana.AsanaConnection;

@RequiredArgsConstructor
public class ResetDailyChores {
	private final AsanaConnection asana;
	private final String WORKSPACE = "Chaos and Darkness";
	private final String PROJECT = "Chores";

	public void getSectionId(String string) {
		asana.getTasks(WORKSPACE, PROJECT, "Daily").stream()
				.map(this::getDetails)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(this::isComplete)
				.map(this::getId)
				.forEach(asana::refreshTask);
	}

	private Optional<Task> getDetails(Task task) {
		return asana.getTask(task.id);
	}

	private boolean isComplete(Task task) {
		return task.completed;
	}

	private String getId(Task task) {
		return task.id;
	}

	public void resetSection(String sectionId) {
		asana.getTasksBySection(sectionId).stream()
				.map(this::getDetails)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(this::isComplete)
				.map(this::getId)
				.forEach(asana::refreshTask);
	}
}
