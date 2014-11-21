package db61b;


import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 *  Tests Select statement, run with make scheck.
 *  @author Fangzhou Chen.
 */
public class SelectTests {

    Table infoTable = new Table(new String[]{"SID", "Grade"});
    Row r1 = new Row(new String[]{"110", "A"});
    Row r2 = new Row(new String[]{"103", "B-"});
    Row r3 = new Row(new String[]{"113", "C"});
    Row r4 = new Row(new String[]{"108", "A-"});

    ArrayList<Condition> conditionList = new ArrayList<>();

    ArrayList<String> subInfo = new ArrayList<>();



    /** this test case tests if select successfully generates a new table.
     *  Since the newly generated table may be in different order, I used
     *  the "||" or case when iterating over it. */
    @Test
    public void testSelectWithoutCondition() {
        infoTable.add(r1);
        infoTable.add(r2);
        infoTable.add(r3);

        subInfo.add("SID");
        Table subInfoTable = infoTable.select(subInfo, conditionList);

        Row expected1 = new Row(new String[]{"110"});
        Row expected2 = new Row(new String[]{"103"});
        Row expected3 = new Row(new String[]{"113"});

        for (Row r : subInfoTable) {
            assertEquals(true, r.equals(expected1)
                               || r.equals(expected2)
                               || r.equals(expected3));
        }
    }

    @Test
    public void testCondition() {
        infoTable.add(r1);
        infoTable.add(r2);
        infoTable.add(r3);
        infoTable.add(r4);

        Column column1 = new Column("SID", infoTable);
        Column column2 = new Column("Grade", infoTable);

        Condition c1 = new Condition(column1, "<", "109");
        Condition c2 = new Condition(column2, ">", "B");


        conditionList.add(c1);
        conditionList.add(c2);

        assertFalse(Condition.test(conditionList, r1));

    }

    /** In lexicographical order, B is less than B-, so it is
     *  expected when we want to query for grades less than B,
     *  we use > B as condition.
     *  */
    @Test
    public void testSelectWithCondition() {
        infoTable.add(r1);
        infoTable.add(r2);
        infoTable.add(r3);
        infoTable.add(r4);

        Column column1 = new Column("SID", infoTable);
        Column column2 = new Column("Grade", infoTable);

        Condition c1 = new Condition(column1, "<", "109");
        Condition c2 = new Condition(column2, ">", "B");

        subInfo.add("SID");
        subInfo.add("Grade");

        conditionList.add(c1);
        conditionList.add(c2);

        Table subInfoTable2 = infoTable.select(subInfo, conditionList);
        Row expected = new Row(new String[]{"103", "B-"});
        for (Row r : subInfoTable2) {
            assertTrue(r.equals(expected));
        }

    }

    Table infoTable2 = new Table(new String[]{"Name", "SID"});
    Row rr1 = new Row(new String[] {"Alex", "110"});
    Row rr2 = new Row(new String[] {"Bob", "103"});
    Row rr3 = new Row(new String[] {"Chase", "113"});



    @Test
    public void testTwoTableNoCon() {
        infoTable2.add(rr1);
        infoTable2.add(rr2);
        infoTable2.add(rr3);

        infoTable.add(r1);
        infoTable.add(r2);
        infoTable.add(r3);
        infoTable.add(r4);

        subInfo.add("Name");
        subInfo.add("Grade");

        Table subInfoTable3 = infoTable.select(infoTable2, subInfo,
                              conditionList);
        Row expected1 = new Row(new String[]{"Alex", "A"});
        Row expected2 = new Row(new String[]{"Chase", "C"});
        Row expected3 = new Row(new String[]{"Bob", "B-"});

        for (Row r : subInfoTable3) {
            assertEquals(true, r.equals(expected1) || r.equals(expected2)
                              || r.equals(expected3));
        }
    }

    @Test
    public void testTwoTableWithCon() {
        infoTable2.add(rr1);
        infoTable2.add(rr2);
        infoTable2.add(rr3);

        infoTable.add(r1);
        infoTable.add(r2);
        infoTable.add(r3);
        infoTable.add(r4);

        subInfo.add("Name");
        subInfo.add("Grade");
        subInfo.add("SID");

        Column column1 = new Column("SID", infoTable);
        Column column2 = new Column("Grade", infoTable);

        Condition c1 = new Condition(column1, "<", "109");
        Condition c2 = new Condition(column2, ">", "B");

        conditionList.add(c1);
        conditionList.add(c2);


        Table subInfoTable4 = infoTable.select(infoTable2,
                                               subInfo, conditionList);

        Row expected = new Row(new String[]{"Bob", "B-", "103"});
        for (Row r : subInfoTable4) {
            assertEquals(true, r.equals(expected));
        }


    }



    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(SelectTests.class));
    }
}
