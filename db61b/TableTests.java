package db61b;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.ByteArrayOutputStream;

/**
 * Table.java Tests. Run with make tcheck.
 * @author Fangzhou Chen.
 */
public class TableTests {
    Table t = new Table(new String[]{"name", "age", "gender"});
    Row r = new Row(new String[]{"Jackie", "20", "Male"});
    Row r2 = new Row(new String[]{"Evan", "15", "Male"});

    @Test
    public void testColumn() {
        assertEquals("age", t.getTitle(1));
        assertEquals(3, t.columns());
    }

    @Test
    public void testFindColumn() {
        assertEquals(0, t.findColumn("name"));
    }

    @Test
    public void testAdd() {
        t.add(r);
        assertEquals(1, t.size());
        boolean bt = t.add(r2);
        assertEquals(true, bt);
        assertEquals(2, t.size());
        t.add(r);
        assertEquals(false, t.add(r));
        assertEquals(2, t.size());
    }

    @Test
    public void testPrint() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        t.add(r);
        t.add(r2);
        t.print();
        assertEquals(" Jackie 20 Male\n Evan 15 Male\n", output.toString());

    }

    @Test
    public void testWriteHelper() {
        t.add(r);
        String output = t.writeHelper(r);
        assertEquals("Jackie,20,Male", output);
    }

    @Test
    public void testWriteReadTable() {
        t.add(r);
        t.writeTable("BasicInfo");
        Table bt = Table.readTable("BasicInfo");
        assertEquals(1, bt.size());
    }

    @Test
    public void testHowToUseIterator() {
        t.add(r);
        t.add(r2);
        ArrayList<String> nameList = new ArrayList<>();
        for (Row row: t) {
            nameList.add(row.get(0));
        }
        assertEquals("Jackie", nameList.get(0));
        assertEquals("Evan", nameList.get(1));
    }

    @Test
    public void testCommonHeader() {
        Table t2 = new Table(new String[]{"name", "SID", "age"});
        Table t3 = new Table(new String[]{"not", "a", "word"});
        String[] commonHeader1 = Table.findCommonHeaders(t, t2);
        String[] commonHeader2 = Table.findCommonHeaders(t2, t3);

        HashSet<String> test1 = new HashSet<>();
        HashSet<String> test2 = new HashSet<>();

        for (String s : commonHeader1) {
            test1.add(s);
        }

        for (String s : commonHeader2) {
            test2.add(s);
        }

        assertTrue(test1.contains("name"));
        assertTrue(test1.contains("age"));
        assertFalse(test2.contains("not"));
        assertFalse(test2.contains("a"));

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(TableTests.class));
    }

}
