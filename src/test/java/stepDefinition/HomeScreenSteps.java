package stepDefinition;

import AppiumServerBuilder.AppiumBaseClass;
import AppiumServerBuilder.AppiumController;
import cucumber.api.Scenario;
import io.appium.java_client.MobileElement;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.junit.Assert;
import PageObjects.*;
import org.openqa.selenium.OutputType;

import java.net.MalformedURLException;


public class HomeScreenSteps extends AppiumBaseClass{

    protected ContactSearchPage searchPage;
    protected ContactDetailPage detailPage;

    @Before
    public void setUp(Scenario scenario) throws Exception {
        AppiumController.instance.start();
        searchPage = new ContactSearchPageAndroid_IOS(driver());
        detailPage = new ContactDetailPageAndroid_IOS(driver());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = driver()
                        .getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/png");
            } catch (Exception e) {
                e.printStackTrace();
                //Utility.Log.error("Error occurred. ScreenShot Captured." + e.getMessage().toString());
            }
        }
    }

    @Given("^the app is running$")
    public void theAppIsRunning() throws MalformedURLException {
        AppiumController.instance.start();

    }

    @Given("^I am on the home screen$")
    public void iAmOnTheHomeScreen(){
        MobileElement SearchBox = searchPage.getSearchField();
        SearchBox.isDisplayed();

    }

    @When("^I search for (.*)$")
    public void iSearchForTestUser(String searchString){
        searchPage.search(searchString);

    }

    @Then("^I am able to see (.*) on results list$")
    public void iAmAbleToSeeTestUserOnResultsList(String fullName){
        //Verify result
        MobileElement searchResult = searchPage.getFirstSearchResult();
        Assert.assertEquals(fullName, searchResult.getText());

    }

    @When("^I tap (.*) to view contact details$")
    public void iTapTestUserToViewProfile(String fullName){
        //Navigate to detail page
        //searchResult.click();
        searchPage.getFirstSearchResult().click();

    }
    @Then("^I can view contact details of (.*)$")
    public void iCanViewContactDetails(String fullName){
        //Verify that correct page is displayed
        Assert.assertEquals(fullName, detailPage.getContactName());

    }
}
