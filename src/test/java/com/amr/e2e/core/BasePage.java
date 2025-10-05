package com.amr.e2e.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasePage {
    protected static WebDriver driver;
    protected static WebDriverWait wait;

    public static void start() {
        WebDriverManager.chromedriver().setup();

        // Use a clean, isolated user-data-dir to avoid Defender/Reset tab and extensions/policies.
        String userDataDir = Paths.get(System.getProperty("java.io.tmpdir"), "selenium-profile").toString();

        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--start-maximized");
        opts.addArguments("--no-first-run");
        opts.addArguments("--no-default-browser-check");
        opts.addArguments("--disable-popup-blocking");
        opts.addArguments("--disable-notifications");
        opts.addArguments("--disable-translate");
        opts.addArguments("--remote-allow-origins=*");
        opts.addArguments("user-data-dir=" + userDataDir);

        // Reduce the “Chrome is being controlled by automated test software” friction a bit
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        opts.setExperimentalOption("prefs", prefs);
        opts.setExperimentalOption("useAutomationExtension", false);
        opts.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        driver = new ChromeDriver(opts);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // If a start URL is provided, open it immediately (preempts reset/settings pages).
        String startUrl = System.getProperty("baseUrl", System.getenv().getOrDefault("BASE_URL", ""));
        if (startUrl != null && !startUrl.isBlank()) {
            driver.get(startUrl);
            waitForPageReady();
        }
    }

    public static void stop() {
        if (driver != null) driver.quit();
    }

    /** Convenience navigation with DOM ready wait. */
    protected static void openUrl(String url) {
        driver.get(url);
        waitForPageReady();
    }

    /** Wait until document.readyState == 'complete'. */
    protected static void waitForPageReady() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(d ->
                "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState"))
            );
        } catch (Exception ignored) {}
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean clickIfPresent(By locator) {
        try {
            safeClick(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Robust click: scroll into view, try Selenium click, fallback to JS click if intercepted. */
    protected void safeClick(By locator) {
        WebElement el = waitClickable(locator);
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementClickInterceptedException | JavascriptException e) {
            jsClick(el);
        }
    }

    protected void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        scrollIntoView(el);
        el.clear();
        el.sendKeys(text);
    }

    /** Try to close common cookie banners or modal dismiss buttons. */
    protected void maybeDismissCookieOrPopup() {
        for (String t : new String[]{"Accept","I Agree","Got it","OK","Allow all","Close"}) {
            List<WebElement> btns = driver.findElements(By.xpath("//button[normalize-space()='"+t+"']"));
            if (!btns.isEmpty()) { try { btns.get(0).click(); } catch (Exception ignored) {} }
        }
        for (By b : new By[]{ By.cssSelector("button[aria-label='Close']"), By.cssSelector(".btn-close,.close") }) {
            List<WebElement> xs = driver.findElements(b);
            if (!xs.isEmpty()) { try { xs.get(0).click(); } catch (Exception ignored) {} }
        }
    }

    /** Public wrapper so tests can call popup dismissal without accessing a protected method. */
    public void dismissPopupsIfVisible() {
        maybeDismissCookieOrPopup();
    }

    protected void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
        } catch (Exception ignored) {}
    }

    protected void jsClick(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception ignored) {}
    }

    public WebDriver driver(){ return driver; }
}
