package com.teamtreehouse;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Teams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;

public class TeamMachine{
  private Player[] mPlayers;
  private List<Teams> mTeams;
  private BufferedReader mReader;
  private Map<String, String> mMenu;
  private List<Player> mAvailablePlayers = new ArrayList<Player>();
  private int mMaxTeams;
  
  public TeamMachine(Player[] players){
    mPlayers = players;
    for (Player player : players){
      mAvailablePlayers.add(player);
    }
    mTeams = new ArrayList<Teams>();
    mMaxTeams = mAvailablePlayers.size()/11;
    mReader = new BufferedReader(new InputStreamReader(System.in));
    mMenu = new HashMap<String, String>();
    mMenu.put("1", "Add a team to the season");
    mMenu.put("2", "Add a remaining player to an existing team");
    mMenu.put("3", "Remove a player from a team");
    mMenu.put("4", "Get a team report");
    mMenu.put("5", "League Balance Report");
    mMenu.put("6", "Finish Season/Exit Program");
  }
    
  public void run(){
  String choice = "";
   do{
     try{
       choice = promptAction();
       switch(choice){
        case "1" :
         if(mTeams.size() == mMaxTeams){
          System.out.printf("No more teams are needed.%n");
          break;
         }
         mTeams.add(promptTeam());
         break;
        case "2" :
          addToTeam();
          break;
        case "3" :
          removeFromTeam();
          break;
        case "4" :
          report();
          break;
        case "5" :
         leagueBalanceReport(); 
         break;
        case "6" :
          break;
        default:
          System.out.printf("%nChoice not valid: '%s'. %n", choice);       
       }
      }catch(IOException ioe){
        System.out.printf("Choice not valid: '%s'. %n", choice);
        ioe.printStackTrace();
      }
     }while(!choice.equals("6"));
     finalReport();
  }
  
  private String promptAction() throws IOException{
    System.out.printf("%n%nPlease choose an item from: %n");
    for(Map.Entry<String, String> option : mMenu.entrySet()){
      System.out.printf("%s. %s %n",
                        option.getKey(),
                        option.getValue());
    }
    System.out.printf("Your choice: ");
    String choice = mReader.readLine();
    return choice.trim().toLowerCase();
  }
  
  private Teams promptTeam()throws IOException{
    System.out.printf("%nEnter the new team's name: ");
    String teamName = mReader.readLine();
    System.out.printf("Enter the coach's name: ");
    String coach = mReader.readLine();
    Teams teams = new Teams(teamName, coach);
    System.out.printf("%s team added to league.%n", teamName);  
    return teams;
  }
  
  private void addToTeam() throws IOException{
    if(mTeams.size() == 0){
      System.out.printf("%nThere are no teams yet. Add a team first.%n");
      return;
    }
    if(mAvailablePlayers.size() == 0){
      System.out.printf("%nThere are no players left.%n");
      return;
    }
    System.out.printf("%nPick a player to add to a team.%n");
    Player playerChoice = playerChoice(mAvailablePlayers);
    System.out.printf("%nAdding %s %s.%n", 
                      playerChoice.getFirstName(), 
                      playerChoice.getLastName());
    System.out.printf("Pick a team to add player: ");
    Teams teamChoice = teamChoice();
    if(teamChoice.addPlayer(playerChoice) == false){
      System.out.printf("%nChosen team is already full.%n");
      return;
    }
    mAvailablePlayers.remove(playerChoice);
    System.out.printf("%n%s %s added to team %s.%n",
                      playerChoice.getFirstName(), 
                      playerChoice.getLastName(),
                      teamChoice.getTeamName());
  }
  
  private Teams teamChoice() throws IOException{
    Collections.sort(mTeams);
    List<String> teamNames = new ArrayList<>();
    for(Teams teams : mTeams){
      teamNames.add(teams.getTeamName());
    }
    return mTeams.get(promptInteger(teamNames));
  }
  
  private Player playerChoice(List<Player> playerList) throws IOException{
    Collections.sort(playerList);
    List<String> playerNames = new ArrayList<>();
    for(Player player : playerList){
      playerNames.add(player.getStats());
    }
    return playerList.get(promptInteger(playerNames));
  }
  
  private int promptInteger(List<String> list) throws IOException{
    boolean isInteger = false;
    boolean isInRange = false;
    int choice = 0;
    
    while(!isInteger || !isInRange){
      System.out.printf("%nYour options are: %n");
      int index = 1;
      for(String item : list){
        System.out.printf("%d.) %s %n", 
                         index,
                         item);
        index++;
      }
      System.out.printf("Your choice: ");
      String choiceAsString = mReader.readLine();
      choiceAsString = choiceAsString.trim();
      try{
        choice = Integer.parseInt(choiceAsString.trim());
        isInteger = true;
      }catch(IllegalArgumentException iae){
        System.out.printf("%s. Please enter a number.%n", 
                          iae.getMessage());
      }
      if(1 <= choice && choice <= list.size()){
        isInRange = true;
      }else{
        System.out.printf("Please select an item on the list.");
      }
    }
    return choice - 1;
  }
  
  private void removeFromTeam() throws IOException{
    if(mTeams.size() == 0){
      System.out.printf("%nThere are no teams yet.%n" + 
                        "Please create a team and then add a player.%n");
      return;
    }
    
    System.out.println("Please select a team: ");
    Teams teamChoice = teamChoice();
    if(teamChoice.getPlayerList().size() == 0){
      System.out.println("The selected team does not have any players yet.");
      return;
    }
    
    System.out.println("Please select a player: ");
    Player playerChoice = playerChoice(teamChoice.getPlayerList());
    teamChoice.removePlayer(playerChoice);
    mAvailablePlayers.add(playerChoice);
  }
  
  private void report() throws IOException{
    if(mTeams.size() == 0){
      System.out.printf("%nNo teams have been created yet.%n" + 
                        "Please create teams and add players.%n");
      return;
    }
    System.out.printf("%nPlease select a team for the report: %n");
    Teams teams = teamChoice();
    Map<Integer, List<String>> playersByHeight = teams.heightMap();
    System.out.printf("%nReport for team %s: %n",
                      teams.getTeamName());
    System.out.printf("%.0f%% of players are experienced.%n%n",
                      teams.experiencePercent());
    System.out.printf("Players sorted by height: ");
    for(int height : playersByHeight.keySet()){
      System.out.printf("%n%d inches: ", height);
      for(String player: playersByHeight.get(height)){
        System.out.printf("%s; ", player);
      }
    }
  }
  
  private void leagueBalanceReport(){
    if(mTeams.size() == 0){
      System.out.printf("%nThere are no teams yet.%n" + 
                        "Please create a team and then add a player.%n");
      return;
    }
    System.out.printf("%nThe League Balance Report is as follows: %n");
    for(Teams teams : mTeams){
      System.out.printf("%nTeam %s %n%d experienced, %d not experienced.%n",
                        teams.getTeamName(),
                        teams.getExperience()[0],
                        teams.getExperience()[1]);
      Map<Integer, List<String>> playersByHeight = teams.heightMap();
      System.out.printf("Number of players by height: %n");
      for(int height : playersByHeight.keySet()){
        System.out.printf("%d player(s) at %d inches.%n",
                         playersByHeight.get(height).size(),
                         height);
      }
    }
  }
  
  private void finalReport(){
    if(mTeams.size() == 0){
      System.out.printf("%nNo teams were created or players added.%n");
      return;
    }
    System.out.printf("%nThe teams are:%n");
    for(Teams teams : mTeams){
      System.out.printf("%nTeam's Name: %s%nCoach's Name: %s%n",
                        teams.getTeamName(),
                        teams.getCoachName());
      for(Player player : teams.getPlayerList()){
        System.out.printf("  %s %s%n",
                          player.getFirstName(),
                          player.getLastName());
      }
    }
  }
}