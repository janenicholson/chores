package nz.net.chaosanddarkness.chores.asana;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import com.asana.models.Project;
import com.asana.models.Section;
import com.asana.models.Task;
import com.asana.models.Workspace;

public class AsanaReaderTest {

	private AsanaReader asana;

	@Before
	public void setup() {
		asana = new AsanaReader(System.getProperty("ASANA_TOKEN"));
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
		Collection<Project> projects = asana.getProjects(asana.getWorkspace("Chaos and Darkness").get());
		assertThat(projects, not(emptyCollectionOf(Project.class)));
	}

	@Test
	public void retrieve_project() {
		Workspace workspace = asana.getWorkspace("Chaos and Darkness").get();
		Optional<Project> project = asana.getProject(workspace, "Chores");
		assertThat(project, isPresent());
		assertThat(project.get().name, is("Chores"));
		assertThat(project.get().workspace, notNullValue());
		assertThat(project.get().workspace.id, is(workspace.id));
	}

	@Test
	public void retrieve_sections() {
		Optional<Project> project = asana.getProject(asana.getWorkspace("Chaos and Darkness").get(), "Chores");
		Collection<Section> section = asana.getSections(project.get());
		assertThat(section, not(emptyCollectionOf(Section.class)));
	}

	@Test
	public void retrieve_section() {
		Optional<Project> project = asana.getProject(asana.getWorkspace("Chaos and Darkness").get(), "Chores");
		Optional<Section> section = asana.getSection(project.get(), "Daily");
		assertThat(section, isPresent());
		assertThat(section.get().name, is("Daily"));
		assertThat(section.get().projects, contains(projectWithId(project.get().id)));
	}

	private Matcher<Project> projectWithId(String id) {
		return new TypeSafeMatcher<Project>() {

			@Override
			public void describeTo(Description description) {
				description.appendText("project with id " + id);
			}

			@Override
			protected boolean matchesSafely(Project project) {
				return id.equals(project.id);
			}
		};
	}

	@Test
	public void retrieve_tasks_for_project() throws IOException {
		Collection<Task> tasks = asana.getTasks(asana.getProject(asana.getWorkspace("Chaos and Darkness").get(), "Chores").get());
		assertThat(tasks, not(emptyCollectionOf(Task.class)));
	}

	@Test
	public void retrieve_tasks_for_section() throws IOException {
		Optional<Project> project = asana.getProject(asana.getWorkspace("Chaos and Darkness").get(), "Chores");
		Optional<Section> section = asana.getSection(project.get(), "Daily");
		Collection<Task> tasks = asana.getTasks(section.get());
		assertThat(tasks, not(emptyCollectionOf(Task.class)));
	}

	@Test
	public void retrieve_tasks_for_section_project_and_workspace() {
		Collection<Task> tasks = asana.getTasks("Chaos and Darkness", "Chores", "Daily");
		assertThat(tasks.size(), is(5));
	}

	@Test
	public void retrieve_task_detail_by_id() {
		Optional<Task> task = asana.getTask("303028668279780");
		assertThat(task, isPresent());
		assertThat(task.get().name, is("Water plants"));
	}
}
