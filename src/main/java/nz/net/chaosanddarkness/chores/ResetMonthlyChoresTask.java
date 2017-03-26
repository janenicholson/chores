package nz.net.chaosanddarkness.chores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import nz.net.chaosanddarkness.chores.asana.AsanaReader;
import nz.net.chaosanddarkness.chores.asana.AsanaUpdater;

public class ResetMonthlyChoresTask extends ChoresTask {

	private final AsanaReader asanaReader;
	private final AsanaUpdater asanaUpdater;
	private final LocalDate date;

	public ResetMonthlyChoresTask(AsanaReader asanaReader, AsanaUpdater asanaUpdater, LocalDate date) {
		super(asanaReader);
		this.asanaReader = asanaReader;
		this.asanaUpdater = asanaUpdater;
		this.date = date;
	}

	public boolean isFirstDayOfMonth() {
		return date.isEqual(firstDayOfMonth());
	}

	private LocalDate firstDayOfMonth() {
		return date.withDayOfMonth(1);
	}

	public void resetSection(String sectionId) throws IOException {
		asanaReader.getTasksBySection(sectionId).stream()
		.map(this::getDetails)
		.filter(Optional::isPresent)
		.map(Optional::get)
		.filter(this::isComplete)
		.map(this::getId)
		.forEach(this::dueLastDayOfThisMonth);
	}

	private void dueLastDayOfThisMonth(String taskId) {
		asanaUpdater.refreshTask(taskId, lastDayOfMonth());
	}

	private LocalDate lastDayOfMonth() {
		return date.withDayOfMonth(date.lengthOfMonth());
	}
}
