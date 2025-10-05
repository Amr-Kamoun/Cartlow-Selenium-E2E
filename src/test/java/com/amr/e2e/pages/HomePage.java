package com.amr.e2e.pages;

import com.amr.e2e.core.BasePage;
import org.openqa.selenium.By;

public class HomePage extends BasePage {
    private final By loginIcon = By.cssSelector("a[href*='login'], a[href*='signin'], a[href*='account']");
    private final By laptopsTab = By.xpath("//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'laptop')]");
    private final By smartwatchesTab = By.xpath("//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'smartwatch')]");

    public HomePage open(String baseUrl) {
        driver().get(baseUrl);
        maybeDismissCookieOrPopup();
        return this;
    }

    public LoginPage goToLogin() { clickIfPresent(loginIcon); return new LoginPage(); }
    public CategoryPage openLaptops() { clickIfPresent(laptopsTab); return new CategoryPage("laptops"); }
    public CategoryPage openSmartwatches() { clickIfPresent(smartwatchesTab); return new CategoryPage("smartwatches"); }
}