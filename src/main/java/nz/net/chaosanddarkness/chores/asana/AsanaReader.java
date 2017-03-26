package nz.net.chaosanddarkness.chores.asana;

import static java.util.Optional.empty;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import com.asana.Client;
import com.asana.models.Project;
import com.asana.models.Section;
import com.asana.models.Task;
import com.asana.models.Workspace;

public class AsanaReader {
	private final Client asana;

	public AsanaReader(String token) {
		asana = Client.accessToken(token);
	}

	public Collection<Workspace> getWorkspaces() throws IOException {
		return asana.workspaces.findAll().execute();
	}

	public Optional<Workspace> getWorkspace(String teamName) throws IOException {
		Optional<Workspace> workspace = getWorkspaces().stream().filter((team)->workspaceNamed(team, teamName)).findFirst();
		if (workspace.isPresent()) {
			return Optional.ofNullable(asana.workspaces.findById(workspace.get().id).execute());
		}
		return empty();
	}

	private boolean workspaceNamed(Workspace team, String name) {
		return name.equalsIgnoreCase(team.name);
	}

	public Collection<Project> getProjects(Workspace workspace) throws IOException {
		return asana.projects.findByWorkspace(workspace.id).execute();
	}

	public Optional<Project> getProject(Workspace workspace, String name) throws IOException {
		Optional<Project> project = getProjects(workspace).stream().filter((p)->projectNamed(p, name)).findFirst();
		if (project.isPresent()) {
			return Optional.ofNullable(asana.projects.findById(project.get().id).execute());
		}
		return empty();
	}

	private boolean projectNamed(Project project, String name) {
		return name.equalsIgnoreCase(project.name);
	}

	public Collection<Section> getSections(Project project) throws IOException {
		return asana.sections.findByProject(project.id).execute();
	}

	public Optional<Section> getSection(Project project, String name) throws IOException {
		Optional<Section> section = getSections(project).stream().filter((s)->sectionNamed(s, name)).findFirst();
		if (section.isPresent()) {
			return Optional.ofNullable(asana.sections.findById(section.get().id).execute());
		}
		return empty();
	}

	private boolean sectionNamed(Section section, String name) {
		return name.equalsIgnoreCase(section.name);
	}

	public Collection<Task> getTasksBySection(String sectionId) throws IOException {
		return asana.tasks.findBySection(sectionId).execute();
	}

	public Optional<Task> getTask(String taskId) throws IOException {
		return Optional.ofNullable(asana.tasks.findById(taskId).execute());
	}

	public String getSectionId(String workspaceName, String projectName, String sectionName) throws IOException {
		Optional<Workspace> workspace = getWorkspace(workspaceName);
		if (workspace.isPresent()) {
			Optional<Project> project = getProject(workspace.get(), projectName);
			if (project.isPresent()) {
				Optional<Section> section = getSection(project.get(), sectionName);
				if (section.isPresent()) {
					return section.get().id;
				}
			}
		}
		return null;
	}
}
