package nz.net.chaosanddarkness.chores;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Optional;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.asana.models.Section;
import com.asana.models.Task;
import com.google.common.collect.Lists;

import nz.net.chaosanddarkness.chores.asana.AsanaConnection;

public class ResetDailyChoresTest {

	private static Task TASK1, TASK1_DETAIL;
	private static Task TASK2, TASK2_DETAIL;
	private static Task TASK3, TASK3_DETAIL;
	private static Task TASK4, TASK4_DETAIL;
	private static Task TASK5, TASK5_DETAIL;
	private static Collection<Task> TASKS;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock private AsanaConnection asana;

	private final String WORKSPACE = "Chaos and Darkness";
	private final String PROJECT = "Chores";
	private final String SECTION = "Daily";

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
	public void setup() {
		when(asana.getTasks(anyString(), anyString(), anyString())).thenReturn(TASKS);
		when(asana.getTask(TASK1.id)).thenReturn(Optional.of(TASK1_DETAIL));
		when(asana.getTask(TASK2.id)).thenReturn(Optional.of(TASK2_DETAIL));
		when(asana.getTask(TASK3.id)).thenReturn(Optional.of(TASK3_DETAIL));
		when(asana.getTask(TASK4.id)).thenReturn(Optional.of(TASK4_DETAIL));
		when(asana.getTask(TASK5.id)).thenReturn(Optional.of(TASK5_DETAIL));
	}

	@Test
	public void ask_asana_for_daily_tasks() {
		ResetDailyChores chores = new ResetDailyChores(asana);
		chores.reset("Daily");
		verify(asana).getTasks(WORKSPACE, PROJECT, SECTION);
	}

	@Test
	public void retrieve_tasks_returned_by_asana() {
		ResetDailyChores chores = new ResetDailyChores(asana);
		chores.reset("Daily");
		verify(asana).getTask(TASK1.id);
		verify(asana).getTask(TASK2.id);
		verify(asana).getTask(TASK3.id);
		verify(asana).getTask(TASK4.id);
		verify(asana).getTask(TASK5.id);
	}

	@Test
	public void update_tasks_returned_by_asana_if_completed() {
		ResetDailyChores chores = new ResetDailyChores(asana);
		chores.reset("Daily");
		verify(asana).refreshTask(TASK2.id);
		verify(asana).refreshTask(TASK3.id);
		verify(asana).refreshTask(TASK5.id);
	}
}
