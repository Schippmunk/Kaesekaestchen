package de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex12.game.achievement;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex12.entity.Player;
import de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex12.game.Game;
import de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex12.game.Map;
import de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex12.game.achievement.NotifyMessage;

public class SurvivorAchievement implements Observer {

	/**
	 * At every index this array contains the amount of edges the player owned when he caused an action.
	 */
	private int[] edgesBefore;
	/**
	 * The id of the player that caused the action.
	 */
	private int actionPlayerId;
	/**
	 * The amount of players.
	 */
	private int size;
	/**
	 * Whether or not the player lost more Edges than the others.
	 */
	private boolean survivor = false;
	
	private boolean fancyActionStarted = false;

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Map && arg != null) {
			if (arg.equals(NotifyMessage.FANCY_ACTION_START)) {
				// when an edge causing a fancy action has been selected, this message gets pushed.
				// this object then saves the amount of edges each player owned at this point in time
				// and the id of the currently active player
				fancyActionStarted = true;
				this.size = Game.players.getSize();
				edgesBefore = new int[size];
				for (int i = 0; i < size; i++) {
					edgesBefore[i] = Game.players.get(i).getSelectedEdges();
				}
				actionPlayerId = Game.players.getActive().getId();
			} else if (fancyActionStarted && arg.equals(NotifyMessage.FANCY_ACTION_END)) {
				// this is called when the fancy action has ended (before the edge causing the action has been marked)
				// it calculates whether the player lost more edges than each other player
				survivor = true;
				int lostEdges = edgesBefore[actionPlayerId] - Game.players.get(actionPlayerId).getSelectedEdges();
				if (lostEdges > 0) {
					for (int i = 0; i < size; i++) {
						if (i != actionPlayerId && edgesBefore[i] - Game.players.get(i).getSelectedEdges() > lostEdges) {
							survivor = false;
						}
					}
				} else {
					survivor = false;
				}
				
			} else if (arg.equals(NotifyMessage.GAME_END)) {
				// when the game has ended, there is a unique winner and it was the one who caused the action,
				// he unlocks the survivor achievement
				Map map = (Map) o;
				LinkedList<Player> winners = map.getWinner();
				Player p = winners.getFirst();
				if(survivor && p.isHuman() && winners.size() == 1 && p.getId() == actionPlayerId){
					System.out.println("Congratulations, " + p + ", you unlocked the Survivor Achievement!");
				}
			}
		}

	}

}
