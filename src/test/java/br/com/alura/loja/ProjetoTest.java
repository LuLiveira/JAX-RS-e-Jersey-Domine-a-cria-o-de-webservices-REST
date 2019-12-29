package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;

import junit.framework.Assert;

public class ProjetoTest {
	
	@Test
	public void testaConexaoComOProjetoResource() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/");
		String response = target.path("projetos").request().get(String.class);
		Assert.assertTrue(response.contains("loja"));
	}
}