package com.stella.playwright.page.fan;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.stella.playwright.page.creator.TipPaymentPage;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

import java.util.List;

@Slf4j
public class FanBonusContentPage {
    private final Page page;
    private final Locator customRequest;
    private final Locator sendTip;
    private final Locator sendMessage;
    private final Locator payBtn;
    private final Locator cancelBtn;
    private final String domain;


    public FanBonusContentPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.customRequest = page.locator("text=Custom request");
        this.sendTip =page.locator("text=Send Tip");
        this.sendMessage = page.locator("text=Send message");
        this.payBtn = page.locator(".modal-content.closefriend-modal.m-4 > div > div >button > i");
        this.cancelBtn = page.locator("//button[normalize-space()='Cancel']");
    }
    public void navigate() {
        page.navigate(domain + "/customerBonusContentList/cu");
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/customerBonusContentList/cu");
        log.info("Bonus Content List Page is displayed");
    }

    public void setValue(String cardNumber, String cvv) {
        checkCardIsAdded(cardNumber, cvv);
        page.waitForTimeout(2000);
        List<ElementHandle> contentList = page.locator(".mb-3.mb-3.mx-1.mt-4 > span >button").elementHandles();
        if (!contentList.isEmpty()) {
            for (ElementHandle elementHandle : contentList) {
                if (elementHandle.isVisible()) {
                    contentList.get(0).click();
                    break;
                }
            }
            page.waitForTimeout(2000);
            page.locator(".modal-content.closefriend-modal.m-4 > div > div >button > i").click();
            page.waitForTimeout(2000);
            page.click("div[id='image_view'] button[type='button']");
            log.info("Bonus Content unlocked!!");
            
        } else {
            log.warn("No content available to unlock, cannot proceed.");
        }
        }

    private void checkCardIsAdded(String cardNumber, String cvv){
       AddCardPage card = new AddCardPage(page,domain);
       card.navigate();
       card.setValue(cardNumber,cvv);
        page.waitForNavigation( () ->{
            page.navigate(domain + "/customerBonusContentList/cu");
        });
    }

    public void sendTip(String tipAmount, String donationMsg) {
        sendTip.click();
        page.waitForTimeout(1000);
        TipPaymentPage tipPaymentPage = new TipPaymentPage(page,domain);
        tipPaymentPage.enterTipAmount(tipAmount, donationMsg);
        log.info("Successfully sent tip");
    }
    public void createCustomRequest(String title, String amount, String cardNumber, String expiryDate, String cvc, String cardHolder, String country){
        customRequest.click();
        page.waitForTimeout(1000);
        FanCustomRequestPage customRequestPage = new FanCustomRequestPage(page,domain);
        customRequestPage.setValue(title, amount, cardNumber, expiryDate, cvc, cardHolder, country);
        log.info("Successfully created custom request");
    }
    public void sendMessage(String userName,String message,String cardNumber,String cvv) {
        sendMessage.click();
        page.waitForTimeout(1000);
        FanChatPage chatPage = new FanChatPage(page,domain);
        chatPage.sendMessageByFan(userName, message,cardNumber,cvv);
    }

}
