package com.astra.project.delegate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

@Service
public class Delete {

	public void execute() throws InvalidRemoteException, TransportException, GitAPIException {
		String username = "gg-software-apps"; // Substitua pelo seu nome de usuário do GitHub
		String password = "Fd861879."; // Substitua pela sua senha do GitHub
		String repositoryToKeep = "astra-template-java"; // Substitua pelo nome do repositório que você deseja manter

		// Caminho local onde os repositórios serão clonados

		deleteRepositoriesExceptOne(username, password, repositoryToKeep);
	}

	public  void deleteRepositoriesExceptOne(String username, String password, String repositoryToKeep)
			throws InvalidRemoteException, TransportException, GitAPIException {
	     List<String> listaSemRepeticao = Arrays.asList(
	             "ter-teste5", "string-cpfr-teste-gomes", "ipix-pagamentos-teste",
	             "acsp-guigomes-teste", "bkff-teste2536", "bkff-teste20", "acct-teste151",
	             "acsp-projet11", "ACSP-projet10", "BKFF-projeto17", "BKFF-projeto16",
	             "BKFF-projeto15", "BKFF-projeto10", "ACSP-projeto8", "ACSP-projeto7",
	             "ACSP-projeto6", "APIO-projeto5", "APIO-teste104", "null-null",
	             "gst-teste103", "gst-teste102", "gst-teste101", "gst-teste77",
	             "gst-teste75", "gst-teste74", "gst-teste73", "gst-teste72", "gst-teste71",
	             "gst-teste70", "gst-teste69", "gst-teste68", "gst-teste67", "gst-teste66",
	             "gst-teste65", "gst-teste64", "gst-teste63", "gst-teste62", "gst-teste61",
	             "gst-teste60", "pve-teste58", "pve-teste57", "pve-teste56", "pve-teste55",
	             "sigla-teste35", "sigla-teste34", "sigla-teste33", "sigla-teste32",
	             "pbff-teste30", "cdc-teste28", "cdc-teste27", "cdc-teste26", "cdc-teste25",
	             "cdc-teste24", "cdc-teste23", "cdc-teste22", "cdc-teste21", "cdc-teste20",
	             "cdc-string", "string-teste13", "string-teste12", "string-teste11",
	             "string-teste10", "ter-teste9", "ter-teste8", "ter-teste7", "ter-teste6",
	             "ter-teste4", "cdc-teste2", "ccc-teste", "cpfr-teste-gomes", "string",
	             "nome-do-repositorio", "astra-boilerplate"
	         );
		// Exclui cada repositório, exceto o especificado
		for (String repositoryName: listaSemRepeticao) {
			

			// Verifica se o repositório deve ser mantido
			if (!repositoryName.equals(repositoryToKeep)) {
				// Exclui o repositório
				try {
					// Configura as credenciais

					// Monta a URL para excluir o repositório
					String deleteUrl = "https://api.github.com/repos/" + username + "/" + repositoryName;

					// Exclui o repositório remoto usando a API do GitHub
					deleteRepository(username, password, deleteUrl);

					System.out.println("Remote repository deleted: " + repositoryName);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Skipping repository: " + repositoryName);
			}
		}
	}

	public void deleteRepository(String username, String password, String deleteUrl) throws IOException {
	    CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("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", "");

	    GitHub github = GitHub.connect();

	}

}