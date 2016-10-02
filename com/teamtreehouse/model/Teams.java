package com.teamtreehouse.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Teams implements Comparable<Teams>{
  public static final int MAX_PLAYERS = 11;
  private String mTeamName;
  private String mCoach;
  private Set<Player> mPlayers;
  private int[] mExperience;
  
  public Teams(String teamName, String coach){
    mTeamName = teamName;
    mCoach = coach;
    mPlayers = new TreeSet<Player>();
    mExperience = new int[] {0, 0};
  }
  
  @Override
  public int compareTo(Teams other){
    if(equals(other)){
      return 0;
    }
    return mTeamName.compareTo(other.getTeamName());
  }
  
  public String getTeamName(){
    return mTeamName;
  }
  
  public String getCoachName(){
    return mCoach;
  }
  
  public int[] getExperience(){
    return mExperience;
  }
  
  public List<Player> getPlayerList(){
    List<Player> playerList =  new ArrayList();
    playerList.addAll(mPlayers);
    return playerList;
  }
  
  
  
  public boolean addPlayer(Player player){
    if(mPlayers.size() == MAX_PLAYERS){
      return false;
    }
    if(player.isPreviousExperience()){
      mExperience[0]++;
    }else{
      mExperience[1]++;
    }
    return mPlayers.add(player);
  }
  
  public boolean removePlayer(Player player){
    if(player.isPreviousExperience()){
      mExperience[0]--;
    }else{
      mExperience[1]--;
    }
    return mPlayers.remove(player);
  }
  
  public Map<Integer, List<String>> heightMap(){
    Map<Integer, List<String>> playersByHeight = new TreeMap();
    
    for(Player player : getPlayerList()){
      int height = player.getHeightInInches();
      if(!playersByHeight.containsKey(height)){
        List<String> players = new ArrayList();
        playersByHeight.put(height, players);        
      }
      playersByHeight.get(height).add(player.getLastName() + ", " +
                                      player.getFirstName());
    }
    return playersByHeight;
  }
  
  public double experiencePercent(){
    double result = 0;
    if(mExperience[0] == 0){
      result = 0;
    }else{
      result = mExperience[0] + mExperience[1];
      result = mExperience[0] / result;
      result = result * 100;
    }
    return result;
  }
   
}