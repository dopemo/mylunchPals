package com.revature.rest;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class PlayerImp implements PostgresImp {
	public List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<Player>();
		Connection connection = null;
		Statement stmt = null;

		try {
			connection = Postgres.getConnection();

			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery( "SELECT * FROM players;" );
	         while ( rs.next() ) {
	             
	             Player player= new Player();
	             player.setPlayer_id(rs.getInt("player_id"));
	             player.setFirstname(rs.getString("firstname"));
	             player.setLastname(rs.getString("lastname"));
	             player.setPosition(rs.getString("position"));
	             player.setR_team(rs.getString("rteam"));
	             player.setF_team(rs.getString("fteam"));
	             player.setFps(rs.getDouble("avg_fps"));
	             players.add(player);
	             
	            
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return players;
	}

	
	
	



	

}
