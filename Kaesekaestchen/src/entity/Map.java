package entity;

/**
 * This Class contains the Entities needed for the game loop, manages their ID's
 * and marks them as owned or selected.
 * 
 * @author paddy
 *
 */
public class Map {

	private int rows;
	private int columns;
	private Edge[] edges;
	private Field[] fields;

	/**
	 * Creates a Map.
	 * 
	 * @param height
	 *            The number of rows of the Map
	 * @param width
	 *            The number of columns of the map
	 */
	public Map(int height, int width) {

		this.rows = height;
		this.columns = width;

		this.makeEdges();
		this.makeFields();
	}

	/**
	 * This initializes the fields array. The ID of a Field in this Map is equal
	 * to its position in the fields array of this class.
	 */
	private void makeFields() {
		int nOfFields = rows * columns;
		fields = new Field[nOfFields];
		for (int i = 0; i < nOfFields; i++) {
			fields[i] = new Field(i);
		}
	}

	/**
	 * This initializes the edges array. The ID of an Edge is equal to it's
	 * position in the edges array of this class. This method also sets if an
	 * edge is horizontal based on the Maps size.
	 */
	private void makeEdges() {
		int nOfEdges = rows + columns + 2 * rows * columns;
		edges = new Edge[nOfEdges];
		int c = 0;
		boolean vertical = false;
		for (int i = 0; i < nOfEdges; i++) {
			edges[i] = new Edge(i, vertical);
			if (!vertical && c == columns - 1) {
				vertical = true;
				c = 0;
			} else if (vertical && c == columns) {
				vertical = false;
				c = 0;
			} else {
				c++;
			}
		}
	}

	/**
	 * This marks an edge of a given ID if it is not marked yet and marks the
	 * surrounding Field-Entities if necessary. It returns the correspondent action that took place.
	 
	 * @param edgeID 
	 * The ID of the marked Edge.
	 * @param markingPlayer 
	 * The Player marking this edge.
	 * 
	 * @return FieldStates.INVALID - Edge already marked 
	 * @return MARKED - Edge has been marked
	 * @return ONE - Edge has been marked and markingPlayer achieved to own one Field
	 * @return TWO - Edge has been marked and markingPlayer achieved to own two Fields
	 */
	public FieldStates markEdge(int edgeID, Player markingPlayer) {
		if (edges[edgeID].isSelected()) {
			return FieldStates.INVALID;
		}

		edges[edgeID].setSelected();
		// counting marked Fields
		int c = 0;
		for (int fieldID : this.hashFunction(edgeID)) {
			if (fieldID != -1 && fields[fieldID].increment(markingPlayer)) {
				c++;
			}
		}

		if (c == 0) {
			return FieldStates.MARKED;
		} else if (c == 1) {
			return FieldStates.ONE;
		} else {
			return FieldStates.TWO;
		}
	}

	/**
	 * Basic print of this Map to the console.
	 */
	public void plot() {
		StringBuilder sb = new StringBuilder();
		int edgep = 0;
		int fieldp = 0;

		for (int linep = 0; linep < rows * 2 + 1; linep++) {
			for (int colp = 0; colp < columns * 2 + 1; colp++) {
				if (colp % 2 == 1 && linep % 2 == 1) {
					sb.append("\t");
					Field f = fields[fieldp];
					if (f.isOwned()) {
						sb.append(f.getOwner().getStrId());
					} else
						sb.append("[").append(fieldp).append("]");

					fieldp++;
				} else if (colp % 2 == 1 || linep % 2 == 1) {
					sb.append("\t");
					Edge e = edges[edgep];
					if (e.isSelected())
						sb.append(e.isVertical() ? "|" : "-");
					else
						sb.append(edgep);

					edgep++;
				} else {
					sb.append("\t *");
				}
			}

			sb.append(System.lineSeparator()).append(System.lineSeparator());
		}

		System.out.println(sb);

	}

	/**
	 * This maps the edgeID to an array of two integers that mark the surrounding
	 * field entities of that edge.
	 * 
	 * @param edgeID
	 * The Edge's ID of which the neighbors are sought
	 * @return An array of neighbor FieldIDs or -1 if there is none.
	 */
	private int[] hashFunction(int edgeID) {
		int[] result = new int[2];
		int edgesPerLine = this.columns * 2 + 1;

		// applying 1st function
		if ((edgeID + columns + 1) % edgesPerLine == 0) {
			result[0] = -1;
		} else {
			result[0] = edgeID
					- Math.floorDiv(edgeID + columns + 1, edgesPerLine)
					* (columns + 1);
		}

		// applying 2nd function
		if ((edgeID + 1) % edgesPerLine == 0) {
			result[1] = -1;
		} else {
			result[1] = edgeID - columns
					- Math.floorDiv(edgeID + 1, edgesPerLine) * (columns + 1);
		}

		// removing fields out of boundaries
		for (int i = 0; i < 2; i++) {
			if (result[i] < -1 || result[i] >= columns * rows) {
				result[i] = -1;
			}
		}

		return result;
	}
}
