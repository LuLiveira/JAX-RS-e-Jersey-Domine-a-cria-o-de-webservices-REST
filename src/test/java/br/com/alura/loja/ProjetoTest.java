package br.com.alura.loja;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ProjetoTest {
	
	private HttpServer server;
	
	@Before
	public void startServer() {
		this.server = Servidor.inicializaServer();
		message("Server running...");
	}
	
	@After
	public void stopServer() {
		message("Server stop!");
		this.server.stop();
	}
	
	@Test
	public void testaConexaoComOProjetoResource() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/");
		String response = target.path("projetos").request().get(String.class);
		Projeto result = (Projeto) new XStream().fromXML(response);
		Assert.assertTrue(result.getId() == 1);
	}
	
	private static void message(String s) {
		System.out.println(s);
	}
}