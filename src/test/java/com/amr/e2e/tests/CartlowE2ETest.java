package com.amr.e2e.tests;

import com.amr.e2e.core.BasePage;
import com.amr.e2e.pages.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class CartlowE2ETest {

    @BeforeClass
    public void setup() {
        // Start Chrome driver with optimized options
        BasePage.start();
    }

    @AfterClass
    public void teardown() {
        // Close browser
        BasePage.stop();
    }

    @Test
    public void fullE2EFlow() {
        // === Step 1: Open homepage ===
        String baseUrl = "https://cartlow.com/uae/en";
        String email = System.getenv("CARTLOW_EMAIL");
        String password = System.getenv("CARTLOW_PASSWORD");

        // Open homepage
        HomePage homePage = new HomePage().open(baseUrl);

        // Dismiss cookie or popups — call via subclass
        homePage.dismissPopupsIfVisible();

        // === Step 2: Login ===
        homePage.goToLogin().login(email, password);

        // === Step 3: Go to Laptops tab ===
        CategoryPage laptopsPage = new HomePage().openLaptops();

        // === Step 4: Select Dell Latitude 7490 ===
        ProductPage dellLaptop = laptopsPage.openProductByName(
                "Dell Latitude 7490 Intel Core i7-8650U 14\" FHD Display, 16GB RAM, 512GB SSD, Windows 10 Pro"
        );

        // === Step 5: Add 1 laptop to the cart ===
        dellLaptop.setQuantity(1).addToCart();

        // === Step 6: Go to Smartwatches tab ===
        CategoryPage watchesPage = new HomePage().openSmartwatches();

        // === Step 7: Select Apple Watch Series 6 ===
        ProductPage appleWatch = watchesPage.openProductByName(
                "Apple Watch Series 6 (40mm, GPS + Cellular) Gold Aluminum Case with Pink Sand Sport Band"
        );

        // === Step 8: Select options & quantity ===
        appleWatch
                .chooseOption("Connectivity", "GPS and Cellular")
                .chooseOption("Color", "Silver")
                .chooseOption("Size", "44mm")
                .setQuantity(2)
                .addToCart();

        // === Step 9: Open cart → remove laptop ===
        new CartPage().removeLineByName("Dell Latitude 7490");

        // === Step 10: Proceed to checkout ===
        CheckoutPage checkout = new CartPage().proceedToCheckout();

        // === Step 11: Validate checkout reached ===
        Assert.assertTrue(checkout.onCheckout(), "[INDICATOR] Checkout page not reached.");
    }
}
