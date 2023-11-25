package com.astra.project.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import com.astra.project.dto.CreateProjectDTO;

public class FileUtils {

	public static void deleteDir(String dirPath) throws IOException {

		Path diretorioPath = Paths.get(dirPath);
		if (Files.exists(diretorioPath)) {
			Files.walk(diretorioPath).sorted(java.util.Comparator.reverseOrder()).map(Path::toFile)
					.forEach(File::delete);
		}
	}

	public static void copyDirectories(String source, String dest) throws IOException {

		var pathSource = Paths.get(source);
		var pathDestination = Paths.get(dest);

		final String[] pastasExcluidas = { ".git", ".settings" };

		Files.walkFileTree(pathSource, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				if (isPastaExcluida(dir, pastasExcluidas)) {
					return FileVisitResult.SKIP_SUBTREE;
				}
				Path destinoDir = pathDestination.resolve(pathSource.relativize(dir));
				Files.createDirectories(destinoDir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path destinoFile = pathDestination.resolve(pathSource.relativize(file));
				Files.copy(file, destinoFile, StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});

	}

	public static void substituirStringEmArquivos(String diretorio, CreateProjectDTO oCreateProjectDTO)
			throws IOException {
		Path diretorioPath = Paths.get(diretorio);

		// EnumSet para indicar que a busca será recursiva
		EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

		Files.walkFileTree(diretorioPath, options, Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// Ler o conteúdo do arquivo
				String conteudo = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

				// Substituir a string antiga pela nova
				conteudo = conteudo.replace("com.astra.template", oCreateProjectDTO.getGroupId());
				conteudo = conteudo.replace("Example", oCreateProjectDTO.getName());

				if (file.getFileName().toString().contains("pom.xml")) {
					conteudo = conteudo.replace("<groupId>com.astra.template</groupId>",
							"<groupId>" + oCreateProjectDTO.getGroupId() + "</groupId>");

					conteudo = conteudo.replace("<artifactId>astra-template-java</artifactId>", "<artifactId>"
							+ oCreateProjectDTO.getSigla().toLowerCase() + "-" + oCreateProjectDTO.getArtifactId() + "</artifactId>");
					conteudo = conteudo.replace("<name>AstraTemplate</name>",
							"<name>" + oCreateProjectDTO.getName() + "</name>");

				}

				Files.write(file, conteudo.getBytes(StandardCharsets.UTF_8));
				if (file.getFileName().toString().contains("Example")) {
					var newFileName = file.toString().replace("Example", oCreateProjectDTO.getName());
					File newFile = file.toFile();
					newFile.renameTo(new File(newFileName));
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static String createGroupIdFolder(String localPath, String newRepositoryName, String groupId,
			boolean isMainFolder) {

		var pathGroupId = groupId.replaceAll("\\.", "\\\\");

		String folder = isMainFolder ? "\\src\\main\\java\\" : "\\src\\test\\java\\";
		pathGroupId = localPath + newRepositoryName + folder + pathGroupId;

		new File(pathGroupId).mkdirs();

		return pathGroupId;
	}

	private static boolean isPastaExcluida(Path path, String[] pastasExcluidas) {
		for (String pasta : pastasExcluidas) {
			if (path.endsWith(pasta)) {
				return true;
			}
		}
		return false;
	}

	public static void createNewPackagesGroupId(String localPath, String newRepositoryName,
			CreateProjectDTO oCreateProjectDTO) throws IOException {
		var pathGroupId = FileUtils.createGroupIdFolder(localPath, newRepositoryName, oCreateProjectDTO.getGroupId(),
				true);

		FileUtils.copyDirectories(localPath + newRepositoryName + "\\src\\main\\java\\", localPath + "temp");

		FileUtils.deleteDir(localPath + newRepositoryName + "\\src\\main\\java\\com");

		FileUtils.copyDirectories(localPath + "temp\\com\\astra\\template", pathGroupId);

		FileUtils.deleteDir(localPath + "temp");

	}

}
