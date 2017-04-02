/*
 The code is commentet propaply and all the fucntion are working as it supouse to do
 */
package tic.tac.toe;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 *
 * @author EL-Torky
 */
public class MultiClients implements Constants{
    private boolean myTurn = false;
    private boolean doIStop = false;
  // Indicate the token for the player
  private char myToken = ' ';

  // Indicate the token for the other player
  private char otherToken = ' ';
  private boolean actionValid = true;
  private boolean wrongAction = true;

  // Create and initialize cells
  public static char[][] ticTacToe = new char[3][3];

  // Indicate selected row and column by the current move
  private int rowSelected;
  private int columnSelected;

  // Input and output streams from/to server
  private DataInputStream fromServer;
  private DataOutputStream toServer;

  // Continue to play?
  private boolean continueToPlay = true;

  // Wait for the player to mark a cell
  private boolean waiting = true;

  // Host name or ip
  private String host = "localhost";
  
  public void startClient() {
    // Print The Board
    for(int i = 0; i < 3; i++){
            StringJoiner sj = new StringJoiner(" | ");
            for(int j = 0; j < 3; j++){
                sj.add(String.format(i+""+j+"%c",ticTacToe[i][j]));
                
            }
            System.out.println(sj.toString());
            }
    // Connect to the server
    connectToServer();
  }
  private void connectToServer() {
    try {
      // Create a socket to connect to the server
      Socket socket = new Socket(host, 8000);

      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    Scanner input = new Scanner(System.in);
    // Control the game on a separate thread
      try {
        // Get notification from the server
        int player = fromServer.readInt();
  
        // Am I player 1 or 2?
        if (player == PLAYER1) {
          myToken = 'X';
          otherToken = 'O';
            System.out.println("Player 1 with token 'X'");
            System.out.println("Waiting for player 2 to join");

          // Receive startup notification from the server
          fromServer.readInt(); // Whatever read is ignored
  
          // The other player has joined
          System.out.println("Player 2 has joined. I start first");
  
          // It is my turn
          myTurn = true;
        }
        else if (player == PLAYER2) {
          myToken = 'O';
          otherToken = 'X';
          System.out.println("Player 2 with token 'O'");
          System.out.println("Waiting for player 1 to move");
        }
        int r,c;
        // Continue to play
        while (continueToPlay) {      
          if (player == PLAYER1) {
            //waitForPlayerAction(); // Wait for player 1 to move
            while(wrongAction){
            System.out.println("Enter The move");
            try {
            int action = input.nextInt();
            if (action == 00 && ticTacToe[0][0] != 'X' && ticTacToe[0][0] != 'O'){
                ticTacToe[0][0] = 'X';
                rowSelected = 0;
                columnSelected = 0;
                wrongAction = false;
            }
            else if (action == 01 && ticTacToe[0][1] != 'X' && ticTacToe[0][1] != 'O'){
                ticTacToe[0][1] = 'X';
                rowSelected = 0;
                columnSelected = 1;
                wrongAction = false;
            }
            else if (action == 02 && ticTacToe[0][2] != 'X' && ticTacToe[0][2] != 'O'){
                ticTacToe[0][2] = 'X';
                rowSelected = 0;
                columnSelected = 2;
                wrongAction = false;
            }
            else if (action == 10 && ticTacToe[1][0] != 'X' && ticTacToe[1][0] != 'O'){
                ticTacToe[1][0] = 'X';
                rowSelected = 1;
                columnSelected = 0;
                wrongAction = false;
            }
            else if (action == 11 && ticTacToe[1][1] != 'X' && ticTacToe[1][1] != 'O'){
                ticTacToe[1][1] = 'X';
                rowSelected = 1;
                columnSelected = 1;
                wrongAction = false;
            }
            else if (action == 12 && ticTacToe[1][2] != 'X' && ticTacToe[1][2] != 'O'){
                ticTacToe[1][2] = 'X';
                rowSelected = 1;
                columnSelected = 2;
                wrongAction = false;
            }
            else if (action == 20 && ticTacToe[2][0] != 'X' && ticTacToe[2][0] != 'O'){
                ticTacToe[2][0] = 'X';
                rowSelected = 2;
                columnSelected = 0;
                wrongAction = false;
            }
            else if (action == 21 && ticTacToe[2][1] != 'X' && ticTacToe[2][1] != 'O'){
                ticTacToe[2][1] = 'X';
                rowSelected = 2;
                columnSelected = 1;
                wrongAction = false;
            }
            else if (action == 22 && ticTacToe[2][2] != 'X' && ticTacToe[2][2] != 'O'){
                ticTacToe[2][2] = 'X';
                rowSelected = 2;
                columnSelected = 2;
                wrongAction = false;
            }
            else{
                System.out.println("You Have entered a wrong Action, please re-enter a vaild one");
            }
            }
            catch(Exception ex){
                System.out.println("You can't enter a nothing else, only numbers !");
                System.out.println("please re-enter a vaild action !");
                input.next();
            }
            }
            wrongAction = true;
            //Get the player move
            printMove();//Prints The board
            sendMove(); // Send the move to the server
            receiveInfoFromServer(); // Receive info from the server
            if(continueToPlay){
            System.out.println("Waiting for player 2 to move");
            }
          }
          else if (player == PLAYER2) {
            receiveInfoFromServer(); // Receive info from the server
            //waitForPlayerAction(); // Wait for player 2 to move
            if(!doIStop){
            while(wrongAction){
            System.out.println("Enter The move");
            try {
            int action = input.nextInt();
            if (action == 00 && ticTacToe[0][0] != 'X' && ticTacToe[0][0] != 'O'){
                ticTacToe[0][0] = 'O';
                rowSelected = 0;
                columnSelected = 0;
                wrongAction = false;
            }
            else if (action == 01 && ticTacToe[0][1] != 'X' && ticTacToe[0][1] != 'O'){
                ticTacToe[0][1] = 'O';
                rowSelected = 0;
                columnSelected = 1;
                wrongAction = false;
            }
            else if (action == 02 && ticTacToe[0][2] != 'X' && ticTacToe[0][2] != 'O'){
                ticTacToe[0][2] = 'O';
                rowSelected = 0;
                columnSelected = 2;
                wrongAction = false;
            }
            else if (action == 10 && ticTacToe[1][0] != 'X' && ticTacToe[1][0] != 'O'){
                ticTacToe[1][0] = 'O';
                rowSelected = 1;
                columnSelected = 0;
                wrongAction = false;
            }
            else if (action == 11 && ticTacToe[1][1] != 'X' && ticTacToe[1][1] != 'O'){
                ticTacToe[1][1] = 'O';
                rowSelected = 1;
                columnSelected = 1;
                wrongAction = false;
            }
            else if (action == 12 && ticTacToe[1][2] != 'X' && ticTacToe[1][2] != 'O'){
                ticTacToe[1][2] = 'O';
                rowSelected = 1;
                columnSelected = 2;
                wrongAction = false;
            }
            else if (action == 20 && ticTacToe[2][0] != 'X' && ticTacToe[2][0] != 'O'){
                ticTacToe[2][0] = 'O';
                rowSelected = 2;
                columnSelected = 0;
                wrongAction = false;
            }
            else if (action == 21 && ticTacToe[2][1] != 'X' && ticTacToe[2][1] != 'O'){
                ticTacToe[2][1] = 'O';
                rowSelected = 2;
                columnSelected = 1;
                wrongAction = false;

            }
            else if (action == 22 && ticTacToe[2][2] != 'X' && ticTacToe[2][2] != 'O'){
                ticTacToe[2][2] = 'O';
                rowSelected = 2;
                columnSelected = 2;
                wrongAction = false;
            }
            else{
                System.out.println("You Have entered a wrong Action, please re-enter a vaild one");
            }
            }
            catch(Exception ex){
                System.out.println("You can't enter a nothing else, only numbers !");
                System.out.println("please re-enter a vaild action !");
                input.next();
            }
            }
            printMove();
            }
            wrongAction = true;
            //Get the player move
            //Prints The board
            sendMove();
            // Send player 2's move to the server
            if(continueToPlay){
            System.out.println("Waiting for player 1 to move");
            }
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }
  private void waitForPlayerAction() throws InterruptedException {
    while (waiting) {
      Thread.sleep(100);
    }

    waiting = true;
  }
  private void sendMove() throws IOException {
    toServer.writeInt(rowSelected); // Send the selected row
    toServer.writeInt(columnSelected); // Send the selected column
  }
  private void printMove(){
      for(int i = 0; i < 3; i++){
            StringJoiner sj = new StringJoiner(" | ");
            for(int j = 0; j < 3; j++){
                sj.add(String.format("%c",ticTacToe[i][j]));
            }
            System.out.println(sj.toString());
            }
  }
//  private void sendMove() throws IOException {
//    
//  }
  private void receiveInfoFromServer() throws IOException {
    // Receive game status
    int status = fromServer.readInt();

    if (status == PLAYER1_WON) {
      // Player 1 won, stop playing
      continueToPlay = false;
      if (myToken == 'X') {
          System.out.println("I won! (X)");
          doIStop = true;
      }
      else if (myToken == 'O') {
        System.out.println("Player 1 (X) has won!");
        receiveMove();
        doIStop = true;
      }
    }
    else if (status == PLAYER2_WON) {
      // Player 2 won, stop playing
      continueToPlay = false;
      if (myToken == 'O') {
        System.out.println("I won! (O)");
        doIStop = false;
        wrongAction = false;
      }
      else if (myToken == 'X') {
        System.out.println("Player 2 (O) has won!");
        receiveMove();
        doIStop = true;
        wrongAction = false;
      }
    }
    else if (status == DRAW) {
      // No winner, game is over
      continueToPlay = false;
      System.out.println("Game is over, no winner!");
      wrongAction = false;

      if (myToken == 'O') {
        receiveMove();
      }
    }
    else {
      receiveMove();
      System.out.println("My Turn !");
      myTurn = true; // It is my turn
    }
  }
  private void receiveMove() throws IOException {
    // Get the other player's move
    int row = fromServer.readInt();
    int column = fromServer.readInt();
    if (myToken == 'X'){
    ticTacToe[row][column] = 'O';
    }
    else{
    ticTacToe[row][column] = 'X';
    }
    System.out.println("Other player Turn");
    printMove();
  }

  // An inner class for a cell
 
    
  public static void main(String[] args) {
    System.out.println("Client Joined The Session");
    MultiClients Client = new MultiClients();
    Client.startClient();
    
  }
}
