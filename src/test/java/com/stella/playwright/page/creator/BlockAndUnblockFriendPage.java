package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class BlockAndUnblockFriendPage extends ManageFriendsPage {

    private final Locator unBlockBtn;

    public BlockAndUnblockFriendPage(Page page, String domain) {
        super(page, domain);
        this.unBlockBtn = page.locator(".btn.rounded-pill.text-dark.btn-dark-grey.btn-lg");
    }

    public void unblockFriend() {
        blockFriend();
        unBlockBtn.first().click();
        page.waitForTimeout(2000);
        confirmBtn.click();
        page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
        String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
        assertEquals(alertText.trim(), "Saved successfully", "Alert text does not match.");
        log.info("Alert text verification successful.");
        log.info("Friend unblocked successfully");
        navigate();
        String memberText = initializeInActiveMembers();
        if (memberText == null) {
            log.warn("No Inactive members to Unblock.");
            return;
        }
        page.waitForTimeout(2000);
        String unblockFriend = page.locator("#IG-0").textContent();
        assertEquals(unblockFriend, memberText, "UnBlock user name does not match.");
        log.info("UnBlock member name verification successful.");
    }
}

