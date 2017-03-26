package nz.net.chaosanddarkness.chores;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.asana.models.Task;
import com.google.api.client.util.DateTime;
import com.google.common.collect.Lists;

import nz.net.chaosanddarkness.chores.asana.AsanaReader;
import nz.net.chaosanddarkness.chores.asana.AsanaUpdater;

public class ResetDailyChoresTaskTest {

	private static Task TASK1, TASK1_DETAIL;
	private static Task TASK2, TASK2_DETAIL;
	private static Task TASK3, TASK3_DETAIL;
	private static Task TASK4, TASK4_DETAIL;
	private static Task TASK5, TASK5_DETAIL;
	private static Collection<Task> TASKS;
	private static final String SECTION_ID = "idofsomekind";

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock private AsanaReader asana;
	@Mock private AsanaUpdater asanaUpdater;

	private ResetDailyChoresTask chores;

	@BeforeClass
	public static void setUp() {
		TASK1 = new Task(); TASK1.id = "303028623664505"; TASK1_DETAIL = new Task(); TASK1_DETAIL.id = "303028623664505";
		TASK2 = new Task(); TASK2.id = "303028668279780"; TASK2_DETAIL = new Task(); TASK2_DETAIL.id = "303028668279780"; TASK2_DETAIL.completed = true;
		TASK3 = new Task(); TASK3.id = "169102700015593"; TASK3_DETAIL = new Task(); TASK3_DETAIL.id = "169102700015593"; TASK3_DETAIL.completed = true;
		TASK4 = new Task(); TASK4.id = "169102700015590"; TASK4_DETAIL = new Task(); TASK4_DETAIL.id = "169102700015590";
		TASK5 = new Task(); TASK5.id = "169102700015584"; TASK5_DETAIL = new Task(); TASK5_DETAIL.id = "169102700015584"; TASK5_DETAIL.completed = true;
		TASKS = Lists.newArrayList(TASK1, TASK2, TASK3, TASK4, TASK5);
	}

	@Before
	public void setup() throws IOException {
		when(asana.getTasksBySection(SECTION_ID)).thenReturn(TASKS);
		when(asana.getTask(TASK1.id)).thenReturn(Optional.of(TASK1_DETAIL));
		when(asana.getTask(TASK2.id)).thenReturn(Optional.of(TASK2_DETAIL));
		when(asana.getTask(TASK3.id)).thenReturn(Optional.of(TASK3_DETAIL));
		when(asana.getTask(TASK4.id)).thenReturn(Optional.of(TASK4_DETAIL));
		when(asana.getTask(TASK5.id)).thenReturn(Optional.of(TASK5_DETAIL));

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
		verify(asanaUpdater).refreshTask(argThat(is(TASK2.id)), any(DateTime.class));
		verify(asanaUpdater).refreshTask(argThat(is(TASK3.id)), any(DateTime.class));
		verify(asanaUpdater).refreshTask(argThat(is(TASK5.id)), any(DateTime.class));
		verifyNoMoreInteractions(asanaUpdater);
	}
}
