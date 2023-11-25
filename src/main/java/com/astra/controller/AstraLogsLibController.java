package com.astra.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astra.dto.AstraLogsLibDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AstraLogsLibController {

	@GetMapping("/")
	public ResponseEntity<AstraLogsLibDTO> index() throws IOException {

		AstraLogsLibDTO oAstraLogsLibDTO = new AstraLogsLibDTO();
		oAstraLogsLibDTO.setData("Hello Word");
		return ResponseEntity.ok(oAstraLogsLibDTO);
	}

}