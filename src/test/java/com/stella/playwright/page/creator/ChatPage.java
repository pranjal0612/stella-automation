package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

import static org.testng.Assert.assertTrue;

@Slf4j
public class ChatPage {
    private final Page page;
    private final String domain;
    private final Locator messageField;
    private final Locator uploadBtn;
    private final Locator uploadFile;
    private final Locator description;
    private final Locator unlockPrice;
    private final Locator uploadSubmit;
    private final Locator cancelBtn;
    private final Locator sendButton;

    public ChatPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.messageField = page.locator("div[placeholder='Type your message']");
        this.uploadBtn = page.locator(".mt-2.px-2.mr-2");
        this.uploadFile = page.locator("#fileInput");
        this.description = page.locator("input[placeholder='Enter message']");
        this.unlockPrice = page.locator("input[placeholder='Amount']");
        this.uploadSubmit = page.locator(".btn.button-class.font-weight-600");
        this.cancelBtn = page.locator(".btn.btn-outline-secondary.font-weight-600.text-white");
        this.sendButton = page.locator(".mt-2.ml-2");
    }

    public void navigate() {
        String expectedUrl = domain + "/creatorDirectMessage/in";
        page.navigate(expectedUrl);
        page.waitForLoadState();
        assertTrue(page.url().startsWith(expectedUrl), "URL does not match the expected pattern");
    }

    public void sendMessage(String userName, String message) {
        Locator member = findMemberByName(userName);

        if (member != null) {
            member.click();
            messageField.fill(message);
            sendButton.click();

            assertTrue(isMessageSent(message), "Message was not sent");
        } else {
            log.warn("User not found: {}", userName);
        }
    }

    public void uploadFile(String filePath, String descriptionText, String price) {
        uploadBtn.click();
        log.info("upload files in chat..");
        uploadFile.setInputFiles(Paths.get(filePath));
        page.waitForTimeout(3000);

        description.fill(descriptionText);
        unlockPrice.fill(price);
        uploadSubmit.click();

        log.info("File sent successfully");
    }

    public void cancelUpload() {
        cancelBtn.click();
        log.info("File sending canceled.");
    }
    private Locator findMemberByName(String userName) {
        Locator membersList = page.locator(".col-12.d-flex.flex-row.justify-content-between");
        Locator member = membersList.locator(String.format("text=%s", userName));

        return member;
    }
    private boolean isMessageSent(String message) {
        page.waitForTimeout(2000);
        Locator sentMessages = page.locator(".mb-5.chat-content-creator > div > span");
        for (int i = 0; i < sentMessages.count(); i++) {
            if (sentMessages.nth(i).textContent().contains(message)) {
                log.info(sentMessages.nth(i).textContent());
                return true;
            }
        }
        return false;
    }

}
