package org.jmonkeyengine;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.junit.jupiter.api.extension.*;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;


public class ExtentReportExtension implements BeforeAllCallback, AfterAllCallback, TestWatcher, BeforeTestExecutionCallback {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private Collection<String> expectedErrors = Set.of(TestDriver.IMAGES_ARE_DIFFERENT_SIZES, TestDriver.IMAGES_ARE_DIFFERENT);

    @Override
    public void beforeAll(ExtensionContext context) {
        if(extent==null){
            ExtentSparkReporter spark = new ExtentSparkReporter("build/reports/ExtentReport.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Test Report");
            spark.config().setReportName("JUnit Test Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            /*ExtentPDFReporter pdf = new ExtentPDFReporter("build/reports/ExtentReport.pdf");
            extent.attachReporter(pdf);*/
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
        if(expectedErrors.contains(TestDriver.IMAGES_ARE_DIFFERENT)){
            getCurrentTest().fail(cause.getMessage());
        }else{
            getCurrentTest().fail(cause);
        }

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