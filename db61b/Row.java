package db61b;

import java.util.Arrays;
import java.util.List;
import static db61b.Utils.*;


/** A single row of a database.
 *  @author Fangzhou Chen
 */
class Row {
    /** A Row whose column values are DATA.  The array DATA must not be altered
     *  subsequently. */
    Row(String[] data) {
        _data = data;
    }

    /** A special implementation when storing header as a row, DATA are
     *  column values, ISHEADER boolean changes to true if it is header. */
    Row(String[] data, boolean isHeader) {
        _data = data;
        _isHeader = isHeader;
    }

    /** Given M COLUMNS that were created from a sequence of Tables
     *  [t0,...,tn] as well as ROWS [r0,...,rn] that were drawn from those
     *  same tables [t0,...,tn], constructs a new Row containing M values,
     *  where the ith value of this new Row is taken from the location given
     *  by the ith COLUMN (for each i from 0 to M-1).
     *
     *  More specifically, if _table is the Table number corresponding to
     *  COLUMN i, then the ith value of the newly created Row should come from
     *  ROWS[_table].
     *
     *  Even more specifically, the ith value of the newly created Row should
     *  be equal to ROWS[_table].get(_column), where _column is the column
     *  number in ROWS[_table] corresponding to COLUMN i.
     *
     *  There is a method in the Column class that you'll need to use, see
     *  {@link db61b.Column#getFrom}).  you're wondering why this looks like a
     *  potentially clickable link it is! Just not in source. You might
     *  consider converting this spec to HTML using the Javadoc command.
     */
    Row(List<Column> columns, Row... rows) {
        String[] result = new String[columns.size()];
        for (int i = 0; i < columns.size(); i += 1) {
            result[i] = columns.get(i).getFrom(rows);
        }

        _data = result;
    }

    /** Return my number of columns. */
    int size() {
        return _data.length;
    }

    /** Return the raw data if the Row is a header. */
    String[] getHeader() {
        if (_isHeader) {
            return _data;
        } else {
            throw error("Not a header!");
        }
    }

    /** Return the value of my Kth column.  Requires that 0 <= K < size(). */
    String get(int k) {
        if ((k < 0) || (k > size())) {
            throw error("Out of Bounds");
        } else {
            return _data[k];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Row)) {
            return false;
        }
        Row other = (Row) obj;
        return Arrays.deepEquals(this._data, other._data);
    }

    /* NOTE: Whenever you override the .equals() method for a class, you
     * should also override hashCode so as to insure that if
     * two objects are supposed to be equal, they also return the same
     * .hashCode() value (the converse need not be true: unequal objects MAY
     * also return the same .hashCode()).  The hash code is used by certain
     * Java library classes to expedite searches (see Chapter 7 of Data
     * Structures (Into Java)). */

    @Override
    public int hashCode() {
        return Arrays.hashCode(_data);
    }

    /** Contents of this row. */
    private String[] _data;
    /**  If this row is header. */
    private boolean _isHeader = false;
}
