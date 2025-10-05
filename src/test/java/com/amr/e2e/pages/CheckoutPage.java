package com.amr.e2e.pages;

import com.amr.e2e.core.BasePage;

public class CheckoutPage extends BasePage {
    public boolean onCheckout() {
        return driver().getCurrentUrl().toLowerCase().contains("checkout");
    }
}