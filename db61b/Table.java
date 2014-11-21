package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain duplicate names. */
    Table(String[] columnTitles) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }
        _header = new Row(columnTitles, true);
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return _header.size();
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        return _header.get(k);
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
        for (int i = 0; i < _header.size(); i += 1) {
            if (_header.get(i).equals(title)) {
                return i;
            }
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    public int size() {
        return _rows.size();
    }

    /** Returns an iterator that returns my rows in an unspecified order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    public boolean add(Row row) {
        if (!(_rows.contains(row))) {
            if (row.size() != _header.size()) {
                throw error("inserted row has wrong length");
            }
            _rows.add(row);
            return true;
        }
        return false;
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(columnNames);
            for (String content = input.readLine();
                 content != null; content = input.readLine()) {
                String[] contentArray = content.split(",");
                Row contentRow = new Row(contentArray);
                table.add(contentRow);
            }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = "";
            output = new PrintStream(name + ".db");
            output.println(writeHelper(_header));
            for (Row row: _rows) {
                output.println(writeHelper(row));
            }

        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }


    /** added: return the string representation of a row R for writeTable.*/
    String writeHelper(Row r) {
        String result = "";
        for (int i = 0; i < r.size() - 1; i += 1) {
            result = result + r.get(i) + ",";
        }
        result = result + r.get(r.size() - 1);
        return result;
    }

    /** Print my contents on the standard output. */
    void print() {
        for (Row r : _rows) {
            String result = "";
            for (int i = 0; i < r.size(); i += 1) {
                result = result + " " + r.get(i);
            }
            System.out.println(result);
        }
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);
        ArrayList<Column> columns = new ArrayList<>();
        for (String columnName : columnNames) {
            columns.add(new Column(columnName, this));
        }
        for (Row r : this) {
            if (Condition.test(conditions, r)) {
                result.add(new Row(columns, r));
            }
        }
        return result;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from TABLE2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    Table select(Table table2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);
        ArrayList<Column> columns = new ArrayList<>();
        for (String columnName : columnNames) {
            columns.add(new Column(columnName, this, table2));
        }
        String[] commonHeaders = Table.findCommonHeaders(this, table2);

        ArrayList<Column> thisColumns = new ArrayList<>();
        ArrayList<Column> table2Columns = new ArrayList<>();

        for (String s : commonHeaders) {
            thisColumns.add(new Column(s, this));
            table2Columns.add(new Column(s, table2));
        }

        for (Row thisRow : this) {
            for (Row t2Row : table2) {
                if (Table.equijoin(thisColumns, table2Columns,
                        thisRow, t2Row)) {
                    if (Condition.test(conditions, thisRow, t2Row)) {
                        result.add(new Row(columns, thisRow, t2Row));
                    }
                }
            }
        }
        return result;
    }

    /** This helper function return the common headers
     *  of the two tables of T1 and T2.
     */
    static String[] findCommonHeaders(Table t1, Table t2) {
        String[] result;
        String[] table1Header = t1._header.getHeader();
        String[] table2Header = t2._header.getHeader();
        Set<String> s1 = new HashSet<String>(Arrays.asList(table1Header));
        Set<String> s2 = new HashSet<String>(Arrays.asList(table2Header));
        s1.retainAll(s2);
        result = s1.toArray(new String[s1.size()]);
        return result;
    }

    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
     *  ROW2 all have identical values.  Assumes that COMMON1 and
     *  COMMON2 have the same number of elements and the same names,
     *  that the columns in COMMON1 apply to this table, those in
     *  COMMON2 to another, and that ROW1 and ROW2 come, respectively,
     *  from those tables. */
    private static boolean equijoin(List<Column> common1, List<Column> common2,
                                    Row row1, Row row2) {
        if (common1.size() == 0 && common2.size() == 0) {
            return true;
        }
        for (Column c1 : common1) {
            for (Column c2 : common2) {
                if (c1.getName().equals(c2.getName())) {
                    String s1 = c1.getFrom(row1);
                    String s2 = c2.getFrom(row2);
                    if (!s1.equals(s2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /** My rows.*/
    private HashSet<Row> _rows = new HashSet<>();
    /** Header Row, aka columnTitles. */
    private Row _header;
}

