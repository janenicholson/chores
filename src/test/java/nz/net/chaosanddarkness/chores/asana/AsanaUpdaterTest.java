package nz.net.chaosanddarkness.chores.asana;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.asana.Client;
import com.asana.models.Task;
import com.asana.requests.ItemRequest;
import com.asana.resources.Tasks;

public class AsanaUpdaterTest {

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock Client asanaClient;
	@Mock Tasks tasks;
	@Mock private ItemRequest<Task> itemRequest;

	private AsanaUpdater updater;
	private LocalDate DUE_DATE = LocalDate.of(2013, 5, 16);

	@Before
	public void setup() {
		updater = new AsanaUpdater(asanaClient);
		asanaClient.tasks = tasks;
		when(tasks.update(anyString())).thenReturn(itemRequest);
	}

	@Test
	public void refresh_task() throws IOException {
		updater.refreshTask("303089274983564", DUE_DATE);
		verify(tasks).update("303089274983564?completed=false&due_on=2013-05-16");
	}
}
