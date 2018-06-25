package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SnakeAndLadderTest {
	
BoardTest boardTest;
	
	@BeforeMethod
	public void createBoard() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		boardTest = new BoardTest();
	}
	
    @Test(priority = 1) 
    public void player_Created_Successfully() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
    	boardTest.createSinglePlayer("Shefali");
    }
    
    @Test(priority = 2)
    public void player_Count_Reaches_Maximum_Should_Throw_MaxPlayersReachedException() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
    	boardTest.createMoreThanFourPlayers("Shefali");
    }
    @Test(priority = 3)
    public void player_With_Same_Name_Should_Throw_PlayerExistsException() throws FileNotFoundException, UnsupportedEncodingException, GameInProgressException, MaxPlayersReachedExeption, IOException {
    	boardTest.createTwoPlayersWithSameName("Shefali");
    }
    
    @Test(priority = 4)
    public void new_Player_Tries_To_Enter_When_Game_In_Progress_Should_Throw_GameInProgressException() throws FileNotFoundException, UnsupportedEncodingException, GameInProgressException, MaxPlayersReachedExeption, IOException, PlayerExistsException, InvalidTurnException {
    	boardTest.whenPlayerTriesToEnterWhileGameInProgress("Shefali");
    }
    
    @Test(priority = 5)
    public void delete_Player_With_Invalid_UUID_Should_Throw_NoUserWithSuchUUIDException() throws IOException {
    	boardTest.deletingPlayerWithInvalidUUID();
    }
    
    @Test(priority = 6)
    public void delete_Player_Successfully() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, NoUserWithSuchUUIDException {
    	boardTest.deletePlayerSuccessfully("Shefali");
    }
    
    @Test(priority = 7, expectedExceptions = {JSONException.class}) 
    public void invalid_Player_Plays_Turn_Should_Throw_Exception() throws FileNotFoundException, UnsupportedEncodingException, IOException, InvalidTurnException {
    	boardTest.rollDiceForInvalidPlayer();
    }
    
    @Test(priority = 8)
    public void player_Plays_Invalid_Turn_Should_Throw_InvalidTurnException() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException {
    	boardTest.rollDiceForPlayerWithInvalidTurn("Shefali");
    }
    
    @Test(priority = 9)
    public void player_Rolls_Dice_Successfully_With_Valid_Dice_Number() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException {
    	boardTest.rolledDiceSuccessfullyAndVerifyingDiceNumber("Shefali");
    }
    
    @Test(priority = 10)
    public void verifying_Position_Of_Player_After_Rolling_Dice() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException {
    	boardTest.rolledDiceSuccessfullyAndVerifyPositionOfPlayer("Shefali");
    }
}
