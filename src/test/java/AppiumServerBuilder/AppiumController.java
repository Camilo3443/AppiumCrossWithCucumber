package AppiumServerBuilder;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class AppiumController {

    public static String executionOS = System.getProperty("ANDROID");

    public static AppiumController instance = new AppiumController();
    public AppiumDriver driver;

    public void startAppiumServer ( ) {

        CommandLine command = new CommandLine("/usr/local/bin/node");
        command.addArgument("/usr/local/bin/appium", false);
        command.addArgument("--address", false);
        command.addArgument("0.0.0.0");
        command.addArgument("--port", false);
        command.addArgument("4723");
        command.addArgument("--full-reset", false);
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(1);
        try {
            executor.execute(command, resultHandler);
            Thread.sleep(5000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopAppiumServer ( ) {
        String[] command = {"/usr/bin/killall", "-KILL", "node"};
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            //Utility.Log.error(e.getStackTrace().toString());
        }
    }

    public void start ( ) throws MalformedURLException {
        if (driver != null) {
            return;
        }
        switch (executionOS) {
            case "ANDROID":
                File classpathRoot = new File(System.getProperty("user.dir"));
                File appDir = new File(classpathRoot, "/app/Android");
                File app = new File(appDir, "Contacts.apk");
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("deviceName", "emulator-5554");
                capabilities.setCapability("plataformName", "Android");
                capabilities.setCapability("automationName", "uiautomator2");
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("app", app.getAbsolutePath());
                capabilities.setCapability("fullReset", false);
                capabilities.setCapability("appPackage", "com.jayway.contacts");
                capabilities.setCapability("appActivity", "com.jayway.contacts.MainActivity");
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                break;
            case "IOS":
                classpathRoot = new File(System.getProperty("user.dir"));
                appDir = new File(classpathRoot, "/app/iOS/");
                app = new File(appDir, "ContactsSimulator.app");
                capabilities = new DesiredCapabilities();
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone X");
                capabilities.setCapability("platformVersion", "12.2");
                capabilities.setCapability("fullReset", "true");
                capabilities.setCapability("platformName", "IOS");
                capabilities.setCapability("udid", "FF53CCF8-1BE4-48AE-9E73-C2AF68B89999");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("app", app.getAbsolutePath());
                driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                break;
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    public void stop ( ) {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
