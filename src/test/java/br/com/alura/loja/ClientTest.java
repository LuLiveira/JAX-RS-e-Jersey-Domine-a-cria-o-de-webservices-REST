package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import junit.framework.Assert;

public class ClientTest {

	private HttpServer server;

	@Before
	public void startServer() {
		this.server = Servidor.inicializaServer();
	}

	@After
	public void stopServer() {
		this.server.stop();
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/");
		String response = target.path("carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new XStream().fromXML(response);
		Assert.assertTrue(carrinho.getRua().equals("Rua Vergueiro 3185, 8 andar"));
	}

}
