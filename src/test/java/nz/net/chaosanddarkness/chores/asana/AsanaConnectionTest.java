package nz.net.chaosanddarkness.chores.asana;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.asana.models.Project;
import com.asana.models.Task;
import com.asana.models.Workspace;

public class AsanaConnectionTest {

	AsanaConnection asana;
	@Before
	public void setup() {
		asana = new AsanaConnection();
	}

	@Test
	public void retrieve_user() throws IOException {
		assertThat(asana.getUser(), notNullValue());
	}

	@Test
	public void retrieve_workspaces() throws IOException {
		assertThat(asana.getWorkspaces(), not(emptyCollectionOf(Workspace.class)));
	}

	@Test
	public void retrieve_workspace() {
		Optional<Workspace> workspace = asana.getWorkspace("Chaos and Darkness");
		assertThat(workspace, isPresent());
		assertThat(workspace.get().name, is("Chaos and Darkness"));
	}

	@Test
	public void retrieve_projects() {
		Collection<Project> tasks = asana.getProjects(asana.getWorkspace("Chaos and Darkness").get());
		assertThat(tasks, not(emptyCollectionOf(Project.class)));
	}

	@Test
	public void retrieve_project() {
		Optional<Project> project = asana.getProject(asana.getWorkspace("Chaos and Darkness").get(), "Chores");
		assertThat(project, isPresent());
		assertThat(project.get().name, is("Chores"));
	}

	@Test
	public void retrieve_tasks() throws IOException {
		Collection<Task> tasks = asana.getTasks(asana.getProject(asana.getWorkspace("Chaos and Darkness").get(), "Chores").get());
		assertThat(tasks, notNullValue());
	}
}
