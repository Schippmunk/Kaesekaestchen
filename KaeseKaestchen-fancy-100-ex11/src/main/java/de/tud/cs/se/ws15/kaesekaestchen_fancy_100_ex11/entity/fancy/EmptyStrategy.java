package de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex11.entity.fancy;

import de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex11.entity.Map;
import de.tud.cs.se.ws15.kaesekaestchen_fancy_100_ex11.entity.Player;

/**
 * 	Creates an empty strategy doing absolutely nothing when action is called. Mainly used for testing.
 * 
 *
 */
public class EmptyStrategy implements FancyHandle {

	@Override
	public int action(Map gameMap, Player markingPlayer) { return 0; }

}
