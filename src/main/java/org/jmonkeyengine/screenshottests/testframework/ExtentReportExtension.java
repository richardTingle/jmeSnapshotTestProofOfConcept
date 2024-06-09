package org.jmonkeyengine.screenshottests.testframework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.output.TeeOutputStream;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;


public class ExtentReportExtension implements BeforeAllCallback, AfterAllCallback, TestWatcher, BeforeTestExecutionCallback{
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void beforeAll(ExtensionContext context) {
        if(extent==null){
            ExtentSparkReporter spark = new ExtentSparkReporter("build/reports/ExtentReport.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Test Report");
            spark.config().setReportName("JUnit Test Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            PrintStream originalOut = System.out;

            // Initialize the ByteArrayOutputStream
            FileOutputStream outputStreamCaptor = null;
            try{
                outputStreamCaptor = new FileOutputStream("build/reports/rawOutput.txt");
            } catch(FileNotFoundException e){
                throw new RuntimeException(e);
            }

            // Create a TeeOutputStream to write to both the original System.out and the output stream captor
            TeeOutputStream teeOut = new TeeOutputStream(originalOut, outputStreamCaptor);
            System.setOut(new PrintStream(teeOut));

            PrintStream originalErr = System.err;

            // Initialize the ByteArrayOutputStream
            FileOutputStream errorStreamCaptor = null;
            try{
                errorStreamCaptor = new FileOutputStream("build/reports/rawError.txt");
            } catch(FileNotFoundException e){
                throw new RuntimeException(e);
            }

            // Create a TeeOutputStream to write to both the original System.err and the error stream captor
            TeeOutputStream teeErr = new TeeOutputStream(originalErr, errorStreamCaptor);
            System.setErr(new PrintStream(teeErr));

            System.out.println("Test");
            System.err.println("Error");
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        extent.flush();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        getCurrentTest().pass("Test passed");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        getCurrentTest().fail(cause);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        getCurrentTest().skip("Test aborted " + cause.toString());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        getCurrentTest().skip("Test disabled: " + reason.orElse("No reason"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String testName = context.getDisplayName();
        test.set(extent.createTest(testName));
    }

    public static ExtentTest getCurrentTest() {
        return test.get();
    }
}