package com.astra.project.delegate;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.astra.project.dto.CreateProjectDTO;
import com.astra.project.utils.FileUtils;
import com.astra.project.utils.GitUtils;
import com.astra.project.utils.ZipUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectDelegate {

	@Value("${git.template.project}")
	private String templateProject;

	@Value("${git.local.path}")
	private String localPath;

	private final GitUtils gitUtils;

	private final Delete delete;
	public ResponseEntity<ByteArrayResource> createNewProject(CreateProjectDTO oCreateProjectDTO)
			throws ClientProtocolException, IOException, GitAPIException, URISyntaxException {

		ResponseEntity<ByteArrayResource> response = null;
		String projectName = oCreateProjectDTO.getSigla().toLowerCase() + "-" + oCreateProjectDTO.getArtifactId();

		try {
			
			gitUtils.cloneProject(templateProject);
		
			gitUtils.createRepository(projectName);

			gitUtils.cloneProject(projectName);

			FileUtils.copyDirectories(localPath + templateProject, localPath + projectName);
			FileUtils.createNewPackagesGroupId(localPath, projectName, oCreateProjectDTO);

			FileUtils.substituirStringEmArquivos(localPath + projectName, oCreateProjectDTO);

			gitUtils.copyRepository(projectName, oCreateProjectDTO);

			response = ZipUtils.downloadZip(localPath + projectName, projectName);

		} catch (Exception e) {
			System.err.println("Falha ao criar novo projeto.: " + e.getMessage());
			throw e;
		} finally {
			FileUtils.deleteDir(localPath + templateProject);
			FileUtils.deleteDir(localPath + projectName);
			FileUtils.deleteDir(localPath + "temp");

		}

		return response;

	}

}
