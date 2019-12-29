package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import junit.framework.Assert;

public class ClientTest {

	private HttpServer server;
	private Client client;

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
		client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/");
		String response = target.path("carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new XStream().fromXML(response);
		Assert.assertTrue(carrinho.getRua().equals("Rua Vergueiro 3185, 8 andar"));
	}
	
	@Test
	public void testaQueSuportaNovosCarrinhos() {
		client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/");
		
		Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        
        String xml = new XStream().toXML(carrinho);
        
        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
        
        Response response = target.path("carrinhos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
        
        String location = response.getHeaderString("Location");
        String content = client.target(location).request().get(String.class);
        Assert.assertTrue(content.contains("Tablet"));
	}

}
