package com.amr.e2e.pages;

import com.amr.e2e.core.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private final By emailInput = By.cssSelector("input[type='email'], input[name*=email i]");
    private final By passwordInput = By.cssSelector("input[type='password'], input[name*=password i]");
    private final By submitBtn = By.xpath("//button[contains(., 'Login') or contains(., 'Log in') or contains(., 'Sign in')]");

    public HomePage login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        safeClick(submitBtn);
        maybeDismissCookieOrPopup();
        return new HomePage();
    }
}