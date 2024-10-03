package com.stella.playwright.page.fan;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.testng.Assert.assertTrue;

@Slf4j
public class FanChatPage {
    private final Page page;
    private final String domain;
    private final Locator messageField;
    private final Locator editMessageBtn;
    private final Locator sendButton;
    private final Locator addNewCard;
    private final Locator sendMessageBtn;
    private final Locator closeFileBtn;
    private final Locator unlockBtn;

    public FanChatPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.messageField = page.locator("div[placeholder='Type your message']");
        this.editMessageBtn = page.locator("//button[normalize-space()='Edit Message']");
        this.sendButton = page.locator(".mt-2.pl-2 > img");
        this.addNewCard = page.locator("//a[normalize-space()='Add new card']");
        this.sendMessageBtn = page.locator("button:has-text('Send Message')");
        this.unlockBtn = page.locator("//button[normalize-space()=\"Unlock\"]");
        this.closeFileBtn = page.locator("div[id=\"image_view\"] button[type=\"button\"]");
    }

    public void navigate(){
        String expectedUrl = domain + "/customerDirectMessage/cu";
        page.navigate(expectedUrl);
        page.waitForLoadState();
        assertTrue(page.url().startsWith(expectedUrl), "URL does not match the expected pattern");
    }
    public void sendMessageByFan(String userName, String message, String cardNumber, String cvv) {
        Locator member = findMemberByName(userName);
        if (member != null) {
            page.waitForTimeout(2000);
            member.click();
            messageField.fill("");
            page.waitForTimeout(1000);
            messageField.fill(message);
            sendButton.click();
            if (isCardAdded()) {
                page.click(".dropdown.col-5.padding-class > button");
                addNewCard.click();
                log.info("Adding new card");
                page.waitForTimeout(2000);
                AddCardPage cardPage = new AddCardPage(page,domain);
                cardPage.setValue(cardNumber,cvv);
                page.waitForTimeout(2000);
                sendButton.click();
                page.waitForTimeout(1000);
                sendMessageBtn.click();
            }else {
                page.waitForTimeout(1000);
                sendMessageBtn.click();
            }
            assertTrue(isMessageSent(message), "Message was not sent");
        } else {
            log.warn("User not found: {}", userName);
        }
    }

    public void payAndView() {
        List<ElementHandle> messageList = page.locator(".mb-5.chat-content-customer > div > span").elementHandles();
        boolean unlockMessageFound = false;

        for (ElementHandle message : messageList) {
            String messageText = message.innerText();
            if (messageText.contains("Unlock for $")) {
                unlockMessageFound = true;
                page.waitForTimeout(2000);
                message.click();
                unlockBtn.click();
                log.info("Unlock file successfully!!");

                page.waitForTimeout(2000);
                page.locator(".rounded.mt-1.img-fluid.unlock-image").last().click();
                log.info("File is opened successfully!!");

                closeFileBtn.click();
                break;
            }
        }
        if (!unlockMessageFound) {
            log.warn("No unlock message found in the chat");
        }
    }


    private boolean isCardAdded() {
        page.waitForTimeout(2000);
        boolean isCardAdded=  page.locator("//button[text()=' Add new card ']").isVisible();
        log.info(String.valueOf(isCardAdded));
        return isCardAdded;
    }
    private Locator findMemberByName(String userName) {
        page.waitForTimeout(3000);
        Locator membersList = page.locator(".col-12.d-flex.flex-row.justify-content-between");
        Locator member = membersList.locator(String.format("text=%s", userName));
        return member;
    }
    private boolean isMessageSent(String message) {
        page.waitForTimeout(3000);
        Locator sentMessages = page.locator(".mb-5.chat-content-customer > div >span");
        for (int i = 0; i < sentMessages.count(); i++) {
            if (sentMessages.nth(i).textContent().contains(message)) {
                log.info(sentMessages.nth(i).textContent());
                return true;
            }
        }
        return false;
    }
}