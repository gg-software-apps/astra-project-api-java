package com.astra.project.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.astra.project.dto.CreateProjectDTO;

@Component
public class GitUtils {

	@Value("${git.username}")
	private String gitUsername;

	@Value("${git.password}")
	private String gitPassword;

	@Value("${git.token}")
	private String gitToken;

	@Value("${git.api.url}")
	private String gitApiUrl;

	@Value("${git.url.base}")
	private String gitUrlBase;

	

	
	@Value("${git.local.path}")
	private String localPath;
	
	public void createRepository(String projectName) throws ClientProtocolException, IOException {

		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(gitApiUrl);

		httpPost.addHeader("Authorization", "token " + gitToken);

		String jsonBody = "{\"name\": \"" + projectName + "\"}";
		StringEntity entity = new StringEntity(jsonBody);
		httpPost.setEntity(entity);
		httpPost.addHeader("Accept", "application/json");

		HttpResponse response = client.execute(httpPost);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 201) {
			throw new RuntimeException("Falha ao criar o repositório. Código de resposta: " + statusCode);
		}

	}


	public void cloneProject(String projectName) {

		try {
			var cloneCommand = Git.cloneRepository().setURI(gitUrlBase + projectName + ".git")
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUsername, gitPassword))
					.setDirectory(new File(localPath + projectName)).setCloneAllBranches(false).setBranch("main");
		
			var git = 	cloneCommand.call();
			
			git.close();
			
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void copyRepository(String newRepositoryName, CreateProjectDTO oCreateProjectDTO)
			throws IOException, GitAPIException, URISyntaxException {

	
		Git git = Git.open(new File(localPath + newRepositoryName));
		CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("token", gitToken);

		// Adicionar todos os arquivos
		git.add().addFilepattern(".").call();

		// Fazer commit dos arquivos
		git.commit().setMessage("Primeiro commit").call();

		// Configurar o repositório remoto
		git.remoteAdd().setName("origin").setUri(new URIish(gitUrlBase + newRepositoryName + ".git")).call();

		// Fazer push para o repositório remoto
		git.push().setRemote("origin").setCredentialsProvider(credentialsProvider).call();
		
		git.close();

	}
	
	
}
