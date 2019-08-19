package PageObjects;

import io.appium.java_client.MobileElement;

public interface ContactSearchPage {

    void search(String name);
    MobileElement getSearchField();
    MobileElement getFirstSearchResult();


}
