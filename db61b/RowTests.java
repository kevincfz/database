package db61b;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Row.java Tests, run with make rcheck
 * @author Fangzhou Chen.
 */
public class RowTests {
    @Test
    public void testRow() {
        Row r = new Row(new String[]{"Kevin", "Fangzhou", "Chen"});
        assertEquals(r.size(), 3);
    }

    @Test
    public void testGet() {
        Row r = new Row(new String[]{"a", "b", "c"});
        assertEquals("a", r.get(0));
    }

    @Test
    public void testEquals() {
        Row r = new Row(new String[]{"a", "b", "c"});
        Row r2 = new Row(new String[]{"a", "b", "c"});
        Row r3 = new Row(new String[]{"a", "b", "d"});
        assertEquals(true, r.equals(r2));
        assertEquals(false, r.equals(r3));
    }

    @Test
    public void testRow2() {
        Table t = new Table(new String[]{"name", "age", "gender"});
        Row r = new Row(new String[]{"Kevin", "100", "Unisex"});
        Row r2 = new Row(new String[]{"Evan", "1", "Male"});
        Row r3 = new Row(new String[]{"Gina", "30", "Female"});
        t.add(r);
        t.add(r2);
        t.add(r3);

        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("name", t));
        columnList.add(new Column("age", t));
        columnList.add(new Column("name", t));
        Row newRow = new Row(columnList, r);
        assertEquals("Kevin", newRow.get(2));
    }



    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(RowTests.class));
    }

}
