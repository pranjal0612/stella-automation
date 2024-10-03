package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;

import static com.microsoft.playwright.Page.*;

@Slf4j
public class OnlineCheckoutStripePage {

    private final Page page;
    private final Locator cardNo;
    private final Locator cardExpiry;
    private final Locator cardCvc;
    private final Locator cardHolderName;
    private final Locator country;
    private final Locator payNowButton;
    private final String domain;

    public OnlineCheckoutStripePage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.cardNo = page.locator("#cardNumber");
        this.cardExpiry = page.locator("#cardExpiry");
        this.cardCvc = page.locator("#cardCvc");
        this.cardHolderName = page.locator("#billingName");
        this.country = page.locator("#billingCountry");
        this.payNowButton = page.locator(".SubmitButton-IconContainer");
    }

    public void verifyCheckoutPage() {
        try {
            page.waitForSelector(".PaymentHeader > div", new WaitForSelectorOptions()
                    .setTimeout(5000)
                    .setState(WaitForSelectorState.VISIBLE));
            log.info("Checkout page verified successfully.");
        } catch (PlaywrightException e) {
            log.error("Checkout page verification failed: 'Pay with card' element is not visible within the timeout.", e);
        }
    }


    public void enterCardDetails(String cardNumber, String expiryDate, String cvc, String cardHolder,String country) {
            this.cardNo.fill(cardNumber);
            this.cardExpiry.fill(expiryDate);
            this.cardCvc.fill(cvc);
            this.cardHolderName.fill(cardHolder);
//            this.country.selectOption(country);
//            this.country.selectOption(new String[]{"India"});
            page.waitForNavigation(() -> {
                this.payNowButton.click();
            });
            log.info("Card details entered successfully.");
        }
    }
