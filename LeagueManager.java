import com.teamtreehouse.TeamMachine;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Teams;

public class LeagueManager {

  public static void main(String[] args) {
    Player[] players = Players.load();
    System.out.printf("%nThere are currently %d registered players.%n", players.length);
    // Your code here!
    
    TeamMachine machine = new TeamMachine(players);
    machine.run();
  }

}
