package com.stella.playwright.page.creator;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.stella.playwright.base.BasePage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.testng.Assert.assertEquals;

@Slf4j
public class ManageFriendsPage extends BasePage {

    private final Locator toAdd;
    private final Locator toRemove;
    private final Locator addFriend;
    private final Locator active;
    private final Locator inActive;
    private final Locator search;
    private final Locator threeDots;
    private final Locator message;
    private final Locator block;
    private final Locator remove;
    public final Locator confirmBtn;
    private final Locator messageInput;
    private final Locator sendMessageBtn;

    private final Locator flagIcon;

    private final Locator flagBtn;

    public ManageFriendsPage(Page page, String domain) {
        super(page, domain);
        this.toAdd = page.locator(".me-md-3.active");
        this.toRemove = page.locator("li:nth-child(2)");
        this.active = page.locator("li:nth-child(3)").nth(0);
        this.inActive = page.locator("li:nth-child(4)").nth(0);
        this.search = page.locator("input[placeholder=\"Search\"]");
        this.threeDots = page.locator(".me-2.el-dropdown").nth(0);
        this.addFriend = page.locator("body > ul:nth-child(6) > li:nth-child(2)");
        this.message = page.locator("body > ul:nth-child(6) > li:nth-child(1)");
        this.block = page.locator("body > ul:nth-child(6) > li:nth-child(3)");
        this.remove = page.locator("body > ul:nth-child(6) > li:nth-child(2)");
        this.confirmBtn = page.locator("//button[normalize-space()=\"Confirm\"]");
        this.messageInput = page.locator(".modal-body.text-center > textarea");
        this.sendMessageBtn = page.locator("//button[normalize-space()=\"Send Message\"]");
        this.flagIcon = page.locator("(//img[@class='dashboard-action-img question-mark'])[1]");
        this.flagBtn = page.locator(".btn.w-100.mx-0.font-family-poppins.stella-modal-btn.text-dark.btn-danger");
    }

    public void navigate() {
        maximizeWindow(page);
        page.navigate(domain + "/creatorDashboardMobile/in");
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/creatorDashboardMobile/in");
        log.info("Navigating to Manage Friends page: {}", domain + "/creatorDashboardMobile/in");
    }

    public void searchFriend(String name) {
        search.fill(name);
        page.keyboard().press("Enter");
        page.waitForTimeout(2000);
        Locator searchResult = page.locator("#IG-0");
        if (searchResult.count() == 0) {
            throw new AssertionError("Search result was not found.");
        }
        String searchName = searchResult.textContent().trim();
        assertEquals(searchName, name, "Search result does not match.");
        log.info("Search successful..");
    }


    public void doFlag() {
        page.reload();
        page.waitForLoadState();
        active.click();
        flagIcon.click();
        page.waitForTimeout(2000);
        if(flagBtn.isVisible()) {
            flagBtn.click();
        }
        page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
        String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
        assertEquals(alertText.trim(), "Saved successfully", "Alert text does not match.");
        log.info("Alert text verification successful.");
        log.info("flag updated successfully");
    }

    public void blockFriend() {
        String memberText = initializeActiveMembers();
        if (memberText == null) {
            log.warn("No active members to block.");
            return;
        }
        log.info("Block Friends In progress");
        active.click();
        this.threeDots.click();
        block.click();
        confirmBtn.click();
        log.info("Friend blocked successfully");

        page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
        String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
        assertEquals(alertText.trim(), "Saved successfully", "Alert text does not match.");
        log.info("Alert text verification successful.");


        page.navigate(domain + "/blockedFriendsMobile/in");
        page.waitForTimeout(2000);

        String blockUserName = page.locator("body > div:nth-child(2) > section:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)").textContent();
        assertEquals(blockUserName,memberText.trim(), "Blocked user name does not match.");
        log.info("Blocked user verification successful.");
        page.locator(".btn.rounded-pill.text-dark.btn-dark-grey.btn-lg").nth(0).click();
        page.waitForTimeout(2000);
        confirmBtn.click();
        log.info("Friend unblocked successfully");
    }

    public void addMember(){
        String memberText = initializeInActiveMembers();
        if (memberText == null) {
            log.warn("No active members to remove.");
            return;
        }
        log.info("Adding member in progress");
        inActive.click();
        threeDots.click();
        addFriend.click();
        confirmBtn.click();
        active.click();
        page.waitForTimeout(3000);
        String addMemberName = page.locator("#IG-0").textContent();
        assertEquals(addMemberName, memberText, "Added user name does not match.");
        log.info("Added member verification successful.");
    }

    public void removeFriend() {
        page.waitForNavigation( () ->{
            page.navigate(domain + "/creatorDashboardMobile/in");
            page.waitForTimeout(3000);
            addMember();
        });
        String memberText = initializeActiveMembers();
        if (memberText == null) {
            log.warn("No active members to remove.");
            return;
        }
        log.info("Removing member in progress");
        active.click();
        this.threeDots.click();
        remove.click();
        confirmBtn.click();
        log.info("Friend removed successfully");

        page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
        String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
        assertEquals(alertText.trim(), "Saved successfully", "Alert text does not match.");
        log.info("Alert text verification successful.");

        inActive.click();
        page.waitForTimeout(2000);

        String removeMemberName = page.locator("#IG-0").textContent();
        assertEquals(removeMemberName, memberText, "Removed user name does not match.");
        log.info("Remove member name verification successful.");
    }


    public void sendMessage(String message) {
        page.waitForTimeout(1000);
        active.click();
        threeDots.click();
        this.message.click();
        messageInput.fill(message);
        page.waitForTimeout(2000);
        sendMessageBtn.click();
        log.info("Message sent successfully");
    }

    private String initializeActiveMembers() {
        active.click();
        page.waitForTimeout(2000);
        List<ElementHandle> listMembers = page.locator("#Active >div >span.me-1").elementHandles();
        if (!listMembers.isEmpty()) {
            for (int i = 0; i < listMembers.size(); i++) {
                ElementHandle member = listMembers.get(i);
                String memberText = member.textContent();
                if (i == 0) {
                    return memberText;
                }
            }
        } else {
            log.warn("No active members found.");
        }
        return null;
    }

    public String initializeInActiveMembers() {
        inActive.click();
        page.waitForTimeout(2000);
        List<ElementHandle> listMembers = page.locator("#Inactive >div >span.me-1").elementHandles();
        if (!listMembers.isEmpty()) {
            for (int i = 0; i < listMembers.size(); i++) {
                ElementHandle member = listMembers.get(i);
                String memberText = member.textContent();
                if (i == 0) {
                    return memberText;
                }
            }
        } else {
            log.warn("No inactive members found.");
        }
        return null;
    }
}