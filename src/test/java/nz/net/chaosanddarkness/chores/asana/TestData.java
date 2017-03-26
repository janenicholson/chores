package nz.net.chaosanddarkness.chores.asana;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import com.asana.models.Task;
import com.google.common.collect.Lists;

public class TestData {

	public static Task TASK1;
	public static Task TASK1_DETAIL;
	public static Task TASK2;
	public static Task TASK2_DETAIL;
	public static Task TASK3;
	public static Task TASK3_DETAIL;
	public static Task TASK4;
	public static Task TASK4_DETAIL;
	public static Task TASK5;
	public static Task TASK5_DETAIL;
	public static Collection<Task> TASKS;
	public static final String SECTION_ID = "idofsomekind";

	public static void setUp() {
		TASK1 = new Task(); TASK1.id = "303028623664505"; TASK1_DETAIL = new Task(); TASK1_DETAIL.id = "303028623664505";
		TASK2 = new Task(); TASK2.id = "303028668279780"; TASK2_DETAIL = new Task(); TASK2_DETAIL.id = "303028668279780"; TASK2_DETAIL.completed = true;
		TASK3 = new Task(); TASK3.id = "169102700015593"; TASK3_DETAIL = new Task(); TASK3_DETAIL.id = "169102700015593"; TASK3_DETAIL.completed = true;
		TASK4 = new Task(); TASK4.id = "169102700015590"; TASK4_DETAIL = new Task(); TASK4_DETAIL.id = "169102700015590";
		TASK5 = new Task(); TASK5.id = "169102700015584"; TASK5_DETAIL = new Task(); TASK5_DETAIL.id = "169102700015584"; TASK5_DETAIL.completed = true;
		TASKS = Lists.newArrayList(TASK1, TASK2, TASK3, TASK4, TASK5);
	}

	public void setup(AsanaReader asanaReader, AsanaUpdater asanaUpdater) throws IOException {
		when(asanaReader.getTasksBySection(SECTION_ID)).thenReturn(TASKS);
		when(asanaReader.getTask(TASK1.id)).thenReturn(Optional.of(TASK1_DETAIL));
		when(asanaReader.getTask(TASK2.id)).thenReturn(Optional.of(TASK2_DETAIL));
		when(asanaReader.getTask(TASK3.id)).thenReturn(Optional.of(TASK3_DETAIL));
		when(asanaReader.getTask(TASK4.id)).thenReturn(Optional.of(TASK4_DETAIL));
		when(asanaReader.getTask(TASK5.id)).thenReturn(Optional.of(TASK5_DETAIL));
	}

}
