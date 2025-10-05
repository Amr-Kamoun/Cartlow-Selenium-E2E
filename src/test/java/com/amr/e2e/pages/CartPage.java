package com.amr.e2e.pages;

import com.amr.e2e.core.BasePage;
import org.openqa.selenium.By;

public class CartPage extends BasePage {
    public CartPage removeLineByName(String namePart) {
        String needle = namePart.toLowerCase();
        try {
            // find the product row and click its remove button
            By remove = By.xpath("//a[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" + needle + "')]/ancestor::*[self::tr or contains(@class,'cart')]//button[contains(.,'Remove') or contains(@aria-label,'remove') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'delete')]");
            if (!clickIfPresent(remove)) {
                System.out.println("[INDICATOR] Cart line not found to remove: " + namePart);
            }
        } catch (Exception e) {
            System.out.println("[INDICATOR] Cart line not found to remove: " + namePart);
        }
        return this;
    }

    public CheckoutPage proceedToCheckout() {
        if (!clickIfPresent(By.xpath("//a[contains(.,'Checkout') or contains(.,'Proceed')]"))
         && !clickIfPresent(By.xpath("//button[contains(.,'Checkout') or contains(.,'Proceed')]"))) {
            System.out.println("[INDICATOR] Checkout button not found.");
        }
        return new CheckoutPage();
    }
}