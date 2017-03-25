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
import com.asana.dispatcher.AccessTokenDispatcher;
import com.asana.models.Project;
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
		asana = new Client(new AccessTokenDispatcher(token));
	}

	public Collection<Workspace> getWorkspaces() {
		try {
			return asana.workspaces.findAll().execute();
		} catch (IOException e) {
		}
		return Collections.emptyList();
	}

	public Optional<Workspace> getWorkspace(String teamName) {
		return getWorkspaces().stream().filter((team)->workspaceNamed(team, teamName)).findFirst();
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
		return getProjects(workspace).stream().filter((project)->projectNamed(project, name)).findFirst();
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
}
