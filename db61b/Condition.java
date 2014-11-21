package db61b;

import java.util.List;

/** Represents a single 'where' condition in a 'select' command.
 *  @author Fangzhou Chen */
class Condition {

    /** A Condition representing COL1 RELATION COL2, where COL1 and COL2
     *  are column designators. and RELATION is one of the
     *  strings "<", ">", "<=", ">=", "=", or "!=". */
    Condition(Column col1, String relation, Column col2) {
        _col1 = col1;
        _col2 = col2;
        _relation = relation;
        _val2 = null;
    }


    /** A Condition representing COL1 RELATION 'VAL2', where COL1 is
     *  a column designator, VAL2 is a literal value (without the
     *  quotes), and RELATION is one of the strings "<", ">", "<=",
     *  ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, String val2) {
        this(col1, relation, (Column) null);
        _val2 = val2;
    }

    /** Assuming that ROWS are rows from the respective tables from which
     *  my columns are selected, returns the result of performing the test I
     *  denote.
     *  The result is a negative integer if this String object
     *  lexicographically precedes the argument string. The result is a
     *  positive integer if this String object lexicographically
     *  follows the argument string.
     *  */
    boolean test(Row... rows) {
        String val1 = _col1.getFrom(rows);
        int compareResult;
        if (_val2 != null) {
            compareResult = val1.compareTo(_val2);
        } else {
            String val2 = _col2.getFrom(rows);
            compareResult = val1.compareTo(val2);
        }

        switch (_relation) {
        case "<":
            return compareResult < 0;
        case ">":
            return compareResult > 0;
        case "<=":
            return compareResult <= 0;
        case ">=":
            return compareResult >= 0;
        case "=":
            return compareResult == 0;
        case "!=":
            return compareResult != 0;
        default:
            return false;
        }
    }

    /** Return true iff ROWS satisfies all CONDITIONS. */
    static boolean test(List<Condition> conditions, Row... rows) {
        for (Condition cond : conditions) {
            if (!cond.test(rows)) {
                return false;
            }
        }
        return true;
    }

    /** The operands of this condition.  _col2 is null if the second operand
     *  is a literal. */
    private Column _col1, _col2;
    /** Second operand, if literal (otherwise null). */
    private String _val2;
    /** Represents relation.*/
    private String _relation;
}
