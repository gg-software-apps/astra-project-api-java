package com.astra.project.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astra.project.delegate.ProjectDelegate;
import com.astra.project.dto.CreateProjectDTO;
import com.astra.project.dto.TesteResponse;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("v1/project")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectDelegate projectDelegate;

	@GetMapping("/")
	public ResponseEntity<TesteResponse> index() throws IOException {

		TesteResponse oTesteResponse = new TesteResponse();
		oTesteResponse.setData("Hello Word");
		return ResponseEntity.ok(oTesteResponse);
	}

	@PostMapping("/create")
	public ResponseEntity<ByteArrayResource> createProject(@RequestBody CreateProjectDTO input) throws ClientProtocolException, IOException, GitAPIException, URISyntaxException {

		return projectDelegate.createNewProject(input);
	}

}