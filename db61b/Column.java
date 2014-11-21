package db61b;

import static db61b.Utils.*;

/** A Column is effectively an index of a specific, named column
 *  in a list of Rows.  Given a sequence of [t0,...,tn] of Tables,
 *  and a column name, c, a Column can retrieve the value of that column of
 *  the first ti that contains it from an array of rows [r0,...,rn],
 *  where each ri comes from ti.
 *  @author P. N. Hilfinger
*/
class Column {
    /** Selects column named NAME from a row of one of the given TABLES. */
    Column(String name, Table... tables) {
        _name = name;
        for (_table = 0; _table < tables.length; _table += 1) {
            _column = tables[_table].findColumn(name);
            if (_column != -1) {
                return;
            }
        }
        throw error("unknown column: %s", name);
    }

    /** Return my name. */
    String getName() {
        return _name;
    }


    /** Returns the value of this Column from ROWS[_table]. Assumes that
     *  ROWS[_table] is from the same table that was provided to the
     *  constructor of this Column. More generally, this method is intended
     *  such that ROWS[k] corresponds to the kth table that was supplied to
     *  the constructor for this method.
     *
     *  Despite the fact that many rows are passed to this function, this
     *  function returns only one value.
     */
    String getFrom(Row... rows) {
        return rows[_table].get(_column);
    }

    /** Column name denoted by THIS. */
    private String _name;
    /** Index of the table and column from which to extract a value. */
    private int _table, _column;
}
