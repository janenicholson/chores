package nz.net.chaosanddarkness.chores.asana;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.asana.models.Task;

public class AsanaUpdaterTest {

	private AsanaReader reader;
	private AsanaUpdater updater;

	@Before
	public void setup() {
		reader = new AsanaReader(System.getProperty("ASANA_TOKEN"));
		updater = new AsanaUpdater(System.getProperty("ASANA_TOKEN"));
	}

	@Test @Ignore // can't do this because it's modifying a real project in use. idiot
	public void refresh_task() throws IOException {
		updater.refreshTask("303089274983564", LocalDate.now());
		Optional<Task> task = reader.getTask("303089274983564");
		assertThat(task, isPresent());
		assertThat(task.get().name, is("Test task"));
		assertThat(task.get().completed, is(false));
	}

}
