package de.tud.cs.se.ws15.kaesekaestchen_team100.entity.fancy;

import de.tud.cs.se.ws15.kaesekaestchen_team100.entity.Map;

public interface FancyHandle {
	
	/**
	 * NOTE: Maybe it is enough to pass the map rather than the entire game
	 * @param game
	 */
	public void action(Map gameMap);

}
