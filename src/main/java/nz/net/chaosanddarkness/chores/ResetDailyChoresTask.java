package nz.net.chaosanddarkness.chores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import nz.net.chaosanddarkness.chores.asana.AsanaReader;
import nz.net.chaosanddarkness.chores.asana.AsanaUpdater;

public class ResetDailyChoresTask extends ChoresTask {
	private final AsanaReader asanaReader;
	private final AsanaUpdater asanaUpdater;

	public ResetDailyChoresTask(AsanaReader asanaReader, AsanaUpdater asanaUpdater) {
		super(asanaReader);
		this.asanaReader = asanaReader;
		this.asanaUpdater = asanaUpdater;
	}

	public void resetSection(String sectionId) throws IOException {
		asanaReader.getTasksBySection(sectionId).stream()
				.map(this::getDetails)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(this::isComplete)
				.map(this::getId)
				.forEach(this::dueToday);
	}

	private void dueToday(String taskId) {
		asanaUpdater.refreshTask(taskId, LocalDate.now());
	}
}
