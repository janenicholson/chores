package nz.net.chaosanddarkness.chores;

import static nz.net.chaosanddarkness.chores.asana.TestData.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
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

public class ResetDailyChoresTaskTest {

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock private AsanaReader asana;
	@Mock private AsanaUpdater asanaUpdater;

	private ResetDailyChoresTask chores;

	private TestData testData = new TestData();

	@BeforeClass
	public static void setUp() {
		TestData.setUp();
	}

	@Before
	public void setup() throws IOException {
		testData.setup(asana, asanaUpdater);

		chores = new ResetDailyChoresTask(asana, asanaUpdater);
	}

	@Test
	public void ask_asana_for_daily_tasks() throws IOException {
		chores.resetSection(SECTION_ID);
		verify(asana).getTasksBySection(SECTION_ID);
	}

	@Test
	public void retrieve_tasks_returned_by_asana() throws IOException {
		chores.resetSection(SECTION_ID);
		verify(asana).getTask(TASK1.id);
		verify(asana).getTask(TASK2.id);
		verify(asana).getTask(TASK3.id);
		verify(asana).getTask(TASK4.id);
		verify(asana).getTask(TASK5.id);
	}

	@Test
	public void update_tasks_returned_by_asana_if_completed() throws IOException {
		chores.resetSection(SECTION_ID);
		verify(asanaUpdater).refreshTask(argThat(is(TASK2.id)), argThat(is(LocalDate.now())));
		verify(asanaUpdater).refreshTask(argThat(is(TASK3.id)), argThat(is(LocalDate.now())));
		verify(asanaUpdater).refreshTask(argThat(is(TASK5.id)), argThat(is(LocalDate.now())));
		verifyNoMoreInteractions(asanaUpdater);
	}
}
