package nz.net.chaosanddarkness.chores.asana;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.asana.Client;
import com.asana.models.Project;
import com.asana.models.Section;
import com.asana.models.Task;
import com.asana.models.User;
import com.asana.models.Workspace;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AsanaConnection {
	private static final String ASANA_TASKS = "https://app.asana.com/api/1.0/tasks";
	private static final String ASANA_TOKEN_PROPERTY = "ASANA_TOKEN";
	private final String token;
	private final Client asana;

	public AsanaConnection() {
		this.token = System.getProperty(ASANA_TOKEN_PROPERTY);
		asana = Client.accessToken(token);
	}

	public Collection<Workspace> getWorkspaces() {
		try {
			return asana.workspaces.findAll().execute();
		} catch (IOException e) {
		}
		return Collections.emptyList();
	}

	public Optional<Workspace> getWorkspace(String teamName) {
		Optional<Workspace> workspace = getWorkspaces().stream().filter((team)->workspaceNamed(team, teamName)).findFirst();
		if (workspace.isPresent())
			try {
				return Optional.ofNullable(asana.workspaces.findById(workspace.get().id).execute());
			} catch (IOException e) {
			}
		return Optional.empty();
	}

	private boolean workspaceNamed(Workspace team, String name) {
		return name.equalsIgnoreCase(team.name);
	}

	public Collection<Project> getProjects(Workspace workspace) {
		try {
			return asana.projects.findByWorkspace(workspace.id).execute();
		} catch (IOException e) {
		}
		return Collections.emptyList();
	}

	public Collection<Task> getTasks(Project project) {
		try {
			return asana.tasks.findByProject(project.id).execute();
		} catch (IOException e) {
		}
		return Collections.emptyList();
	}

	public Optional<Project> getProject(Workspace workspace, String name) {
		Optional<Project> project = getProjects(workspace).stream().filter((p)->projectNamed(p, name)).findFirst();
		try {
			return Optional.of(asana.projects.findById(project.get().id).execute());
		} catch (IOException e) {
		}
		return Optional.empty();
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

	public Collection<Section> getSections(Optional<Project> project) {
		try {
			return asana.sections.findByProject(project.get().id).execute();
		} catch (IOException e) {
		}
		return Collections.emptyList();
	}

	public Optional<Section> getSection(Optional<Project> project, String name) {
		Optional<Section> section = getSections(project).stream().filter((s)->sectionName(s, name)).findFirst();
		try {
			return Optional.of(asana.sections.findById(section.get().id).execute());
		} catch (IOException e) {
		}
		return Optional.empty();
	}

	private boolean sectionName(Section section, String name) {
		return name.equalsIgnoreCase(section.name);
	}

	public Collection<Task> getTasks(Section section) {
		try {
			return asana.tasks.findBySection(section.id).execute();
		} catch (IOException e) {
		}
		return Collections.emptyList();
	}
}
