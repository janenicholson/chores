package nz.net.chaosanddarkness.chores;

import static nz.net.chaosanddarkness.chores.asana.TestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import nz.net.chaosanddarkness.chores.asana.AsanaReader;
import nz.net.chaosanddarkness.chores.asana.AsanaUpdater;
import nz.net.chaosanddarkness.chores.asana.TestData;

public class ResetWeeklyChoresTaskTest {

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock private AsanaReader asanaReader;
	@Mock private AsanaUpdater asanaUpdater;
	private TestData testData = new TestData();

	private ResetWeeklyChoresTask resetWeeklyChoresTask;

	@BeforeClass
	public static void setUp() {
		TestData.setUp();
	}

	@Before
	public void setup() throws IOException {
		testData.setup(asanaReader, asanaUpdater);
	}

	@Test
	public void march_26_2017_is_not_a_monday() {
		given_date_is(LocalDate.of(2017, 03, 26));
		assertThat(resetWeeklyChoresTask.isMonday(), is(false));
	}

	@Test
	public void march_27_2017_is_a_monday() {
		given_date_is(LocalDate.of(2017, 03, 27));
		assertThat(resetWeeklyChoresTask.isMonday(), is(true));
	}

	@Test
	public void retrieve_tasks_for_section_from_asana() throws IOException {
		given_date_is(LocalDate.of(2017, 03, 27));
		resetWeeklyChoresTask.resetSection(SECTION_ID);

		verify(asanaReader).getTasksBySection(SECTION_ID);
	}

	@Test
	public void set_completed_tasks_to_due_april_2_2017_on_march_27_2017() throws IOException {
		given_date_is(LocalDate.of(2017, 03, 27));
		LocalDate dueDate = LocalDate.of(2017, 04, 02);

		resetWeeklyChoresTask.resetSection(SECTION_ID);

		verify(asanaUpdater).refreshTask(TASK2.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK3.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK5.id, dueDate);
		verifyNoMoreInteractions(asanaUpdater);
	}

	private void given_date_is(LocalDate date) {
		resetWeeklyChoresTask = new ResetWeeklyChoresTask(asanaReader, asanaUpdater, date);
	}
}
