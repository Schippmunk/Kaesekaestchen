package entity;

public class Map {

	private int lines;
	private int columns;
	private Edge[] edges;
	private Field[] fields;

	public Map(int lines, int columns) {

		this.lines = lines;
		this.columns = columns;

		this.makeEdges();
		this.makeFields();
	}

	/**
	 * This initializes the fields array
	 */
	private void makeFields() {
		int nOfFields = lines * columns;
		fields = new Field[nOfFields];
		for (int i = 0; i < nOfFields; i++) {
			fields[i] = new Field(i);
		}
	}

	/**
	 * This initializes the edges array
	 */
	private void makeEdges() {
		int nOfEdges = lines + columns + 2 * lines * columns;
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
	 * This marks an edge of a given ID if it is not marked yet.
	 * 
	 * @param edgeID
	 * @return INVALID = Edge was already marked
	 * @return MARKED = Edge has been marked without surrounding a field
	 * @return ONE = The Edge has been marked, one Field got owned.
	 * @return TWO = The two surrounding Fields of the Edge got owned.
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
	 * Basic print of the map of entities to the console
	 */
	public void plot() {
		StringBuilder sb = new StringBuilder();
		int edgep = 0;
		int fieldp = 0;

		for (int linep = 0; linep < lines * 2 + 1; linep++) {
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
	 * This maps the edgeID to an array of two integers that mark the matching
	 * field entities, where -1 means there is none for this neighbor
	 * 
	 * @param edgeID
	 * @return
	 */
	private int[] hashFunction(int edgeID) {
		int[] result = new int[2];
		int edgesPerLine = this.columns * 2 + 1;

		// applying 1st function
		if ((edgeID + columns + 1) % edgesPerLine == 0) {
			result[0] = -1;
		} else {
			result[0] = edgeID - Math.floorDiv(edgeID + columns + 1, edgesPerLine) * (columns + 1);
		}

		// applying 2nd function
		if ((edgeID + 1) % edgesPerLine == 0) {
			result[1] = -1;
		} else {
			result[1] = edgeID - columns - Math.floorDiv(edgeID + 1, edgesPerLine) * (columns + 1);
		}

		// removing fields out of boundaries
		for (int i = 0; i < 2; i++) {
			if (result[i] < -1 || result[i] >= columns * lines) {
				result[i] = -1;
			}
		}

		return result;
	}
}
