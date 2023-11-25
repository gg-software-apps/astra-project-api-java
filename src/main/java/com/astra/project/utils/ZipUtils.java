package com.astra.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ZipUtils {

	public static ResponseEntity<ByteArrayResource> downloadZip(String sourceDirectory, String fileName) throws IOException {

		String zipFileName = fileName + ".zip";

		// Caminho absoluto do diretório
		Path sourcePath = Paths.get(sourceDirectory).toAbsolutePath();

		// Cria o arquivo ZIP
		File zipFile = new File(sourceDirectory + ".zip");
		org.apache.commons.io.FileUtils.deleteQuietly(zipFile); // Exclui o arquivo ZIP se já existir
     
		createZip(sourcePath.toFile(), zipFile);

		  HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData(zipFileName, zipFileName);
	        headers.add("gitUrl", "https://github.com/gg-software-apps/" + fileName + ".git");

	        Path path = Paths.get(zipFile.getAbsolutePath());
	        
	      var fileBytes = Files.readAllBytes(path);
	        ByteArrayResource resource = new ByteArrayResource(fileBytes);

			org.apache.commons.io.FileUtils.deleteQuietly(zipFile); // Exclui o arquivo ZIP se já existir

	        // Retornar a resposta com o recurso e os headers configurados
	        return ResponseEntity.ok()
	                .headers(headers)
	               
	                .contentLength(fileBytes.length)
	                .body(resource);
	
	}

	private static void createZip(File sourceDirectory, File zipFile) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {

			zipFile(sourceDirectory, sourceDirectory.getName(), zos);
		}
	}

	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		try (FileInputStream fis = new FileInputStream(fileToZip)) {
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
		}
	}
}
