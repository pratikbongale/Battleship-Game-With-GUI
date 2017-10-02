/*
 * Point.java
 *
 * Version:
 *      $Id Point.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.io.*;

/**
 * Point represents a 2 dimensional point with its x co ordinate stored as row and y co ordinate stored as col.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

import java.io.*;

class Point implements Serializable {
	
	private int row;
	private int col;

	Point(int r, int c) {
		row = r;
		col = c;
	}

	int getRow() {
		return row;
	}

	int getCol() {
		return col;
	}

	void setRow( int r ) {
		row = r;
	}

	void setCol( int c ) {
		col = c;
	}
}