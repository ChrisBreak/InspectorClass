//Cristhian Sotelo-Plaza
//CPSC 501 FALL 2019
//Assignment 2

//To compile after all other classes have been compiled already
//use:  javac -cp .:hamcrest-all-1.3.jar:junit-4.13-beta-1.jar InspectorTests.java
//To run use:  java -cp .:hamcrest-all-1.3.jar:junit-4.13-beta-1.jar org.junit.runner.JUnitCore InspectorTests

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InspectorTests {
  private ByteArrayOutputStream testStream;
  private final PrintStream originalPrint = System.out;

  @Test
  public void testPrintConstructors() {
    testStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testStream));
    Inspector inspector = new Inspector();
    inspector.testPrintConstructors((new ClassA()).getClass());
    assertEquals("\nClassA constructors...", "CONSTRUCTORS: FROM: ClassA\n"
    + "\tCONSTRUCTOR NAME: ClassA FROM: ClassA\n"
    + "\tPARAMETERS: NONE\n"
    + "\tMODIFIER: public\n"
    + "\t-----------------\n"
    + "\tCONSTRUCTOR NAME: ClassA FROM: ClassA\n"
    + "\tPARAMETERS: (int)\n"
    + "\tMODIFIER: public\n"
    + "\t-----------------\n", testStream.toString());
    System.setOut(originalPrint);
  }

  @Test
  public void testPrintClassList() {
    testStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testStream));
    Inspector inspector = new Inspector();
    inspector.testPrintClassList((new ClassA()).getClass());
    assertEquals("\nClassA setVal() parameters...", "\tPARAMETERS: (int)\n", testStream.toString());
    System.setOut(originalPrint);
  }

  @Test
  public void testPrintInterfaces() {
    testStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testStream));
    Inspector inspector = new Inspector();
    try {
      inspector.testPrintInterfaces(new ClassB());
    }
    catch (Exception e) {
      System.out.println(e);
    }
    assertEquals("INTERFACES: FROM: ClassB\n"
  		+ "\tINTERFACE: java.lang.Runnable\n"
  		+	"\t\tSUPERINTERFACE: NONE\n"
  		+	"\t\tMETHODS: FROM: java.lang.Runnable\n"
  		+	"\t\t\tMETHOD NAME: run FROM java.lang.Runnable\n"
  		+	"\t\t\tEXCEPTIONS THROWN: NONE\n"
  		+	"\t\t\tPARAMETERS: NONE\n"
  		+	"\t\t\tRETURN TYPE: void\n"
  		+	"\t\t\tMODIFIER: public abstract\n"
  		+	"\t\t\t-----------------\n"
  		+	"\t\tFIELDS: FROM: java.lang.Runnable\n"
  		+ "\t\t\tNONE\n"
  		+ "\t======END OF INTERFACE java.lang.Runnable======\n", testStream.toString());
    System.setOut(originalPrint);
  }

  public static void main( String[] args ) {
      Result result = JUnitCore.runClasses( InspectorTests.class );
      for (Failure failure : result.getFailures()) {
          System.out.println(failure.toString());
      }
  }
}
