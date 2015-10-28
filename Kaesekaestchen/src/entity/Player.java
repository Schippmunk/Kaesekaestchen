package entity;

/**
 * This class is used to model both players who play the game.
 *
 */
public class Player extends Entity {

	/**
	 * The name of the player. Will be displayed in console messages.
	 */
	private String name;
	/**
	 * The amount of fields the player owns.
	 */
	private int ownedFields = 0;

	/**
	 * Create a new player with a name and an id (inherited from Entity)
	 */
	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public int getOwnedFields() {
		return ownedFields;
	}

	public String getName() {
		return name;
	}

	/**
	 * This method is used to make it transparent which field belongs to whom on
	 * the map. This is necessary because the entered names of the players are
	 * to long to be displayed in the console and prefixes of the names of the
	 * players might be equal. "P1" is the Player who entered his name first,
	 * the other one will be represented by "P2".
	 * 
	 * @return "P" + id
	 */
	String getStrId() {
		return "P" + id;
	}

	/**
	 * Increases the amount of fields the player receives.
	 * @param n the amount of fields
	 * @throws IllegalArgumentException if n is less than 1 or greater than 2
	 */
	public void increaseOwnedFields(int n) throws IllegalArgumentException {
		if (0 < n && n < 3) {
			ownedFields = ownedFields + n;
		} else
			throw new IllegalArgumentException();
	}

}
