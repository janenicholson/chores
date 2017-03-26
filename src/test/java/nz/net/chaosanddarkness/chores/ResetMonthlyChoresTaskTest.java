package nz.net.chaosanddarkness.chores;

import static nz.net.chaosanddarkness.chores.asana.TestData.SECTION_ID;
import static nz.net.chaosanddarkness.chores.asana.TestData.TASK2;
import static nz.net.chaosanddarkness.chores.asana.TestData.TASK3;
import static nz.net.chaosanddarkness.chores.asana.TestData.TASK5;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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

public class ResetMonthlyChoresTaskTest {

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock private AsanaReader asanaReader;
	@Mock private AsanaUpdater asanaUpdater;

	private ResetMonthlyChoresTask chores;

	private TestData testData = new TestData();

	@BeforeClass
	public static void setUp() {
		TestData.setUp();
	}

	@Before
	public void setup() throws IOException {
		testData.setup(asanaReader, asanaUpdater);
	}

	@Test
	public void march_30_is_not_first_day_of_month() {
		given_date_is(LocalDate.of(2017, 03, 30));
		assertThat(chores.isFirstDayOfMonth(), is(false));
	}

	@Test
	public void march_31_is_not_first_day_of_month() {
		given_date_is(LocalDate.of(2017, 03, 31));
		assertThat(chores.isFirstDayOfMonth(), is(false));
	}

	@Test
	public void april_1_is_first_day_of_month() {
		given_date_is(LocalDate.of(2017, 04, 1));
		assertThat(chores.isFirstDayOfMonth(), is(true));
	}

	@Test
	public void retrieve_tasks_for_section_from_asana() throws IOException {
		given_date_is(LocalDate.of(2017, 04, 1));
		chores.resetSection(SECTION_ID);

		verify(asanaReader).getTasksBySection(SECTION_ID);
	}

	@Test
	public void set_completed_tasks_to_due_april_30_2017_on_april_1_2017() throws IOException {
		given_date_is(LocalDate.of(2017, 04, 1));
		LocalDate dueDate = LocalDate.of(2017, 04, 30);

		chores.resetSection(SECTION_ID);

		verify(asanaUpdater).refreshTask(TASK2.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK3.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK5.id, dueDate);
		verifyNoMoreInteractions(asanaUpdater);
	}

	@Test
	public void set_completed_tasks_to_due_feb_28_2017_on_feb_1_2017() throws IOException {
		given_date_is(LocalDate.of(2017, 02, 1));
		LocalDate dueDate = LocalDate.of(2017, 02, 28);

		chores.resetSection(SECTION_ID);

		verify(asanaUpdater).refreshTask(TASK2.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK3.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK5.id, dueDate);
		verifyNoMoreInteractions(asanaUpdater);
	}

	@Test
	public void set_completed_tasks_to_due_dec_31_2017_on_dec_1_2017() throws IOException {
		given_date_is(LocalDate.of(2017, 12, 1));
		LocalDate dueDate = LocalDate.of(2017, 12, 31);

		chores.resetSection(SECTION_ID);

		verify(asanaUpdater).refreshTask(TASK2.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK3.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK5.id, dueDate);
		verifyNoMoreInteractions(asanaUpdater);
	}

	@Test
	public void set_completed_tasks_to_due_feb_29_2012_on_feb_1_2012() throws IOException {
		given_date_is(LocalDate.of(2012, 2, 1));
		LocalDate dueDate = LocalDate.of(2012, 2, 29);

		chores.resetSection(SECTION_ID);

		verify(asanaUpdater).refreshTask(TASK2.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK3.id, dueDate);
		verify(asanaUpdater).refreshTask(TASK5.id, dueDate);
		verifyNoMoreInteractions(asanaUpdater);
	}

	private void given_date_is(LocalDate date) {
		chores = new ResetMonthlyChoresTask(asanaReader, asanaUpdater, date);
	}
}
