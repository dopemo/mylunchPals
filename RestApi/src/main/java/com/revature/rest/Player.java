package com.revature.rest;

public class Player {
	int player_id;
	String firstname;
	String lastname;
	String position;
	String r_team;
	String f_team;
	double fps;
	public int getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getR_team() {
		return r_team;
	}
	public void setR_team(String r_team) {
		this.r_team = r_team;
	}
	public String getF_team() {
		return f_team;
	}
	public void setF_team(String f_team) {
		this.f_team = f_team;
	}
	public double getFps() {
		return fps;
	}
	public void setFps(double fps) {
		this.fps = fps;
	}
	@Override
	public String toString() {
		return "Player [player_id=" + player_id + ", firstname=" + firstname + ", lastname=" + lastname + ", position="
				+ position + ", r_team=" + r_team + ", f_team=" + f_team + ", fps=" + fps + "]";
	}
	
	

}
