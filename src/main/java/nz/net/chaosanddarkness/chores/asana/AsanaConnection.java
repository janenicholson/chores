package nz.net.chaosanddarkness.chores.asana;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import com.asana.Client;
import com.asana.models.Project;
import com.asana.models.Section;
import com.asana.models.Task;
import com.asana.models.User;
import com.asana.models.Workspace;
import com.google.api.client.util.DateTime;

public class AsanaConnection {
	private final Client asana;

	public AsanaConnection(String token) {
		asana = Client.accessToken(token);
	}

	public Collection<Workspace> getWorkspaces() {
		try {
			return asana.workspaces.findAll().execute();
		} catch (IOException e) {
		}
		return emptyList();
	}

	public Optional<Workspace> getWorkspace(String teamName) {
		Optional<Workspace> workspace = getWorkspaces().stream().filter((team)->workspaceNamed(team, teamName)).findFirst();
		if (workspace.isPresent())
			try {
				return Optional.ofNullable(asana.workspaces.findById(workspace.get().id).execute());
			} catch (IOException e) {
			}
		return empty();
	}

	private boolean workspaceNamed(Workspace team, String name) {
		return name.equalsIgnoreCase(team.name);
	}

	public Collection<Project> getProjects(Workspace workspace) {
		try {
			return asana.projects.findByWorkspace(workspace.id).execute();
		} catch (IOException e) {
		}
		return emptyList();
	}

	public Collection<Task> getTasks(Project project) {
		try {
			return asana.tasks.findByProject(project.id).execute();
		} catch (IOException e) {
		}
		return emptyList();
	}

	public Optional<Project> getProject(Workspace workspace, String name) {
		Optional<Project> project = getProjects(workspace).stream().filter((p)->projectNamed(p, name)).findFirst();
		try {
			return Optional.of(asana.projects.findById(project.get().id).execute());
		} catch (IOException e) {
		}
		return empty();
	}

	private boolean projectNamed(Project project, String name) {
		return name.equalsIgnoreCase(project.name);
	}

	public User getUser() throws IOException {
		return asana.users.findAll().execute().stream().filter(AsanaConnection::itMe).findFirst().get();
	}
	private static boolean itMe(User user) {
		return "Jane Nicholson".equalsIgnoreCase(user.name);
	}

	public Collection<Section> getSections(Project project) {
		try {
			return asana.sections.findByProject(project.id).execute();
		} catch (IOException e) {
		}
		return emptyList();
	}

	public Optional<Section> getSection(Project project, String name) {
		Optional<Section> section = getSections(project).stream().filter((s)->sectionName(s, name)).findFirst();
		try {
			return Optional.of(asana.sections.findById(section.get().id).execute());
		} catch (IOException e) {
		}
		return empty();
	}

	private boolean sectionName(Section section, String name) {
		return name.equalsIgnoreCase(section.name);
	}

	public Collection<Task> getTasksBySection(String sectionId) {
		try {
			return asana.tasks.findBySection(sectionId).execute();
		} catch (IOException e) {
		}
		return emptyList();
	}

	public Collection<Task> getTasks(Section section) {
		return getTasksBySection(section.id);
	}

	public Collection<Task> getTasks(String workspaceName, String projectName, String sectionName) {
		Optional<Workspace> workspace = getWorkspace(workspaceName);
		if (workspace.isPresent()) {
			Optional<Project> project = getProject(workspace.get(), projectName);
			if (project.isPresent()) {
				Optional<Section> section = getSection(project.get(), sectionName);
				if (section.isPresent()) {
					return getTasks(section.get());
				}
			}
		}
		return emptyList();
	}

	public Optional<Task> getTask(String taskId) {
		try {
			return Optional.of(asana.tasks.findById(taskId).execute());
		} catch (IOException e) {
		}
		return empty();
	}

	public void refreshTask(String taskid) {
		String dueOn = new DateTime(true, new Date().getTime(), 0).toStringRfc3339();
		try {
			asana.tasks.update(String.format("%s?completed=false&due_on=%s", taskid, dueOn)).execute();
		} catch (IOException e) {
		}
	}
}
