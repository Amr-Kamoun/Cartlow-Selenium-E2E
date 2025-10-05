package com.amr.e2e.pages;

import com.amr.e2e.core.BasePage;
import org.openqa.selenium.By;

public class ProductPage extends BasePage {
    private final String name;
    public ProductPage(String name){ this.name = name; }

    private final By qtyPlus = By.xpath("//button[contains(@aria-label,'increase') or contains(.,'+') or contains(@class,'plus')]");
    private final By addToCart = By.xpath("//button[contains(., 'Add to cart') or contains(., 'Add to Cart') or contains(., 'Add')]");

    public ProductPage chooseOption(String label, String value) {
        boolean picked =
            clickIfPresent(By.xpath("//select/option[normalize-space(.)='"+value+"']/..")) ||
            clickIfPresent(By.xpath("//*[self::button or self::label or self::span][contains(.,'"+value+"')]"));
        if (!picked) {
            System.out.println("[INDICATOR] Option not found (" + label + "): " + value + " on product: " + name);
        }
        return this;
    }

    public ProductPage setQuantity(int qty) {
        for (int i = 1; i < qty; i++) { if (!clickIfPresent(qtyPlus)) break; }
        return this;
    }

    public CartPage addToCart() {
        clickIfPresent(addToCart);
        maybeDismissCookieOrPopup();
        clickIfPresent(By.xpath("//a[contains(.,'View cart') or contains(.,'View Cart') or contains(.,'Cart')]"));
        return new CartPage();
    }
}