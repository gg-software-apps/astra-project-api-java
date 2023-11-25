package com.astra.project.dto;

import lombok.Data;

@Data
public class CreateProjectDTO {
	private String artifactId;

	private String groupId;
	private String name;
	private String sigla;
	private String tipoProjeto;

	
}
