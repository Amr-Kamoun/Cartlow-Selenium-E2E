package com.amr.e2e.pages;

import com.amr.e2e.core.BasePage;
import org.openqa.selenium.By;

public class CategoryPage extends BasePage {
    private final String label;
    public CategoryPage(String label){ this.label = label; }

    public ProductPage openProductByName(String namePart) {
        String needle = namePart.toLowerCase();
        By productLink = By.xpath("(//a[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" + needle + "')])[1]");
        clickIfPresent(productLink);
        return new ProductPage(namePart);
    }
}