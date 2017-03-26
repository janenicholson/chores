package nz.net.chaosanddarkness.chores;

import java.util.Optional;

import com.asana.models.Task;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nz.net.chaosanddarkness.chores.asana.AsanaReader;

@RequiredArgsConstructor
public class ChoresTask {
	private final AsanaReader asanaReader;

	@SneakyThrows
	protected Optional<Task> getDetails(Task task) {
		return asanaReader.getTask(task.id);
	}

	protected boolean isComplete(Task task) {
		return task.completed;
	}

	protected String getId(Task task) {
		return task.id;
	}

}
