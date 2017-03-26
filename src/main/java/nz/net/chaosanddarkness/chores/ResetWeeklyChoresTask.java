package nz.net.chaosanddarkness.chores;

import static java.time.DayOfWeek.MONDAY;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import nz.net.chaosanddarkness.chores.asana.AsanaReader;
import nz.net.chaosanddarkness.chores.asana.AsanaUpdater;

public class ResetWeeklyChoresTask extends ChoresTask {
	private final AsanaReader asanaReader;
	private final AsanaUpdater asanaUpdater;
	private final LocalDate date;

	public ResetWeeklyChoresTask(AsanaReader asanaReader, AsanaUpdater asanaUpdater, LocalDate date) {
		super(asanaReader);
		this.asanaReader = asanaReader;
		this.asanaUpdater = asanaUpdater;
		this.date = date;
	}

	public boolean isMonday() {
		return date.getDayOfWeek().equals(MONDAY);
	}

	public void resetSection(String weeklySectionId) throws IOException {
		asanaReader.getTasksBySection(weeklySectionId).stream()
				.map(this::getDetails)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(this::isComplete)
				.map(this::getId)
				.forEach(this::dueThisSunday);
	}

	private void dueThisSunday(String taskId) {
		asanaUpdater.refreshTask(taskId, date.plusDays(6));
	}
}
