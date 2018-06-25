package com.qainfotech.tap.training.snl.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class BoardTest {

Board board;
	
	public BoardTest() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		board = new Board();
	}
	
	public void createSinglePlayer(String name) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		JSONArray listOfPlayers = board.registerPlayer(name);
		JSONObject jsonObject = listOfPlayers.getJSONObject(0);
        assertTrue(jsonObject.get("name").equals("Shefali"));
	}
	
	public JSONArray createTwoPlayers(String name) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		board.registerPlayer(name+1);
		JSONArray array = new JSONArray(); 
		array = board.registerPlayer(name+2);
		return array;
	}
	
	public void createMoreThanFourPlayers(String name) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		int i;
		for(i=1;i<=4;i++)
	    	board.registerPlayer(name+i);
		try {
			board.registerPlayer(name+i);
		}
		catch(MaxPlayersReachedExeption e) {
		    assertThat(e).hasMessage("The board already has maximum allowed Player: " + 4);
		}
	}
	
	public void createTwoPlayersWithSameName(String name) throws FileNotFoundException, UnsupportedEncodingException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		try {
			for(int i=1;i<=2;i++)
				board.registerPlayer(name);
		}
		catch(PlayerExistsException e) {
			assertThat(e).hasMessage("Player '"+name+"' already exists on board");
		}
	}
	
	
	public void whenPlayerTriesToEnterWhileGameInProgress(String name) throws FileNotFoundException, UnsupportedEncodingException, GameInProgressException, MaxPlayersReachedExeption, IOException, PlayerExistsException, InvalidTurnException {
		JSONArray array = createTwoPlayers(name);
		for(int i=0;i<2;i++) {
			JSONObject player = array.getJSONObject(i);
			String id = (String)player.get("uuid"); 
			UUID playerUuid = UUID.fromString(id);
			board.rollDice(playerUuid);
		}
		try {
			board.registerPlayer(name+3);		
		}
		catch(GameInProgressException e) {
			assertThat(e).hasMessage("New player cannot join since the game has started");
		}
	}
	
	public void deletingPlayerWithInvalidUUID() throws FileNotFoundException, UnsupportedEncodingException {
		UUID uuid = UUID.randomUUID();
		try {
			board.deletePlayer(uuid);
		}
		catch(NoUserWithSuchUUIDException e) {
			assertThat(e).hasMessage("No Player with uuid '"+ uuid +"' on board");
		}
	}
	
	public void deletePlayerSuccessfully(String name) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException {
		JSONArray array = createTwoPlayers(name);
		JSONObject player = array.getJSONObject(0);
		String id = (String)player.get("uuid"); 
		UUID playerUuid = UUID.fromString(id);
		array = board.deletePlayer(playerUuid);
		assertThat(array.length()==1);
	}
	
	public void rollDiceForInvalidPlayer() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException {
		UUID uuid = UUID.randomUUID();
		board.rollDice(uuid);
	}
	
	public void rollDiceForPlayerWithInvalidTurn(String name) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException {
		board.registerPlayer(name);
		try {
			board.rollDice(board.getUUID());
		}
		catch(InvalidTurnException e) {
			assertThat(e).hasMessage("Player '"+board.getUUID().toString()+"' does not have the turn");
		}
	}
	
	public void rolledDiceSuccessfullyAndVerifyingDiceNumber(String name) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException {
		JSONArray array = createTwoPlayers(name);
		for(int i=0;i<2;i++) {
			JSONObject player = array.getJSONObject(i);
			String id = (String)player.get("uuid"); 
			UUID playerUuid = UUID.fromString(id);
			JSONObject object = board.rollDice(playerUuid);
			int value = object.getInt("dice");
			assertTrue(value>=1);
			assertTrue(value<=6);
		}
	}
	
	public void rolledDiceSuccessfullyAndVerifyPositionOfPlayer(String name) throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException {
		JSONArray array = createTwoPlayers(name);
		for(int i=0;i<2;i++) {
			JSONObject player = array.getJSONObject(i);
			String id = (String)player.get("uuid"); 
			UUID playerUuid = UUID.fromString(id);
			int currentPosition = (int)board.data.getJSONArray("players").getJSONObject(i).get("position");
			JSONObject response = board.rollDice(playerUuid);
			int diceValue = response.getInt("dice");
			int type = (int) board.data.getJSONArray("steps").getJSONObject(currentPosition+diceValue).get("type");
			int newPosition = (int)board.data.getJSONArray("players").getJSONObject(i).get("position");
			if(type==0)
				assertThat(response.get("message").equals("Player moved to " + newPosition));
			else if(type==1)
				assertThat(response.get("message").equals("Player was bit by a snake, moved back to " + newPosition));
			else if(type==2)
				assertThat(response.get("message").equals("Player climbed a ladder, moved to " + newPosition));
		}
	}
}
