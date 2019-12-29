package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ProjetoTest {

	private HttpServer server;
	private Client client;

	@Before
	public void startServer() {
		this.server = Servidor.inicializaServer();
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());

		client = ClientBuilder.newClient(config);
		message("Server running...");
	}

	@After
	public void stopServer() {
		message("Server stop!");
		this.server.stop();
	}

	@Test
	public void testaConexaoComOProjetoResource() {
		WebTarget target = client.target("http://localhost:8080/");
		String response = target.path("projetos/1").request().get(String.class);
		Projeto result = (Projeto) new XStream().fromXML(response);
		Assert.assertTrue(result.getId() == 1);
	}

	@Test
	public void testaQueSuportaNovosProjetos() {

		WebTarget target = client.target("http://localhost:8080/");

		Projeto projeto = new Projeto(3l, "Status code e a Interface Uniforme", 2019);

		String xml = new XStream().toXML(projeto);

		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

		Response response = target.path("projetos").request().post(entity);
		Assert.assertEquals(201, response.getStatus());

		String location = response.getHeaderString("Location");
		String content = client.target(location).request().get(String.class);
		Assert.assertTrue(content.contains("Status code e a Interface Uniforme"));

	}

	private static void message(String s) {
		System.out.println(s);
	}
}