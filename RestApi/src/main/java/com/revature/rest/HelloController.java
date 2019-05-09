package com.revature.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@Path("/fantasy")
public class HelloController {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		return "Hello World!";
	}
	
	@GET
	@Path("/{id}")
	@Produces("text/html")
	public String sayHelloLoop(@PathParam("id") int id) {
		String output = "";
		for (int i = 0; i < id; i++) {
			output += "Hi";
		}
		return output;
	}
	
	@GET
	@Path("/individual")
	@Produces(MediaType.APPLICATION_JSON)
	public Player sayHelloName(@QueryParam("name") String name) {
		
		PlayerImp playersImp=new PlayerImp();
		List<Player> players=new ArrayList<Player>();
		Player p=new Player();
		players=playersImp.getAllPlayers();
		for(Player player:players){
			if(player.getFirstname().equals(name)){
				p=player;
			}
		}
		return p;
	}
	
	@GET
	@Path("/player")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getPrince() {
		PlayerImp playersImp=new PlayerImp();
		List<Player> players=new ArrayList<Player>();
		players=playersImp.getAllPlayers();

	
		return players;
		
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/artist")
	public Artist postArtist(Artist input) {
		return input;
	}
}
