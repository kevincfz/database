package db61b;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;

/**
 * Test the Command Interpreter.
 * run with make cicheck
 *
 * @author Fangzhou Chen.
 *
 * This test was intended to test the commandIntepreter Class,
 * but I figured it would be better to write test.in/test.out file instead,
 * so this test check is minimal and was functioanlly replaced by the test.in/
 * test.out files, which are run by the "make check command"
 *
 */
public class CITests {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Test
    public void testLoad() {
        System.setOut(new PrintStream(output));
        Scanner input = new Scanner("load students;");
        CommandInterpreter interpreter = new CommandInterpreter(input,
                                                                System.out);
        interpreter.statement();
        assertEquals("> Loaded students.db\n", output.toString());
    }



    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(CITests.class));

    }

}
