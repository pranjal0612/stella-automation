package com.stella.playwright.page.creator;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.stella.playwright.base.BasePage;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
public class AnnouncementPage extends BasePage {

    private final Locator announcementIcon;
    private final Locator newFilterGroup;
    private final Locator filterName;
    private final Locator autoRenewToggle;
    private final Locator tippedToggle;
    private final Locator tippedAmount;
    private final Locator spentOverToggle;
    private final Locator spentAmount;
    private final Locator subscribedToggle;
    private final Locator subscribedCount;
    private final Locator subscribedTime;
    private final Locator inActiveOverToggle;
    private final Locator inActiveCount;
    private final Locator inActiveTime;
    private final Locator saveChangesBtn;
    private final Locator closeBtn;
    private final Locator uploadIcon;
    private final Locator sendBtn;
    private final Locator messageText;
    private final Locator uploadFiles;
    private final Locator uploadMessage;
    private final Locator unlockPrice;
    private final Locator uploadBtn;
    private final Locator threeDots;
    private final Locator editFilter;
    private final Locator deleteFilter;

    public AnnouncementPage(Page page, String domain) {
        super(page, domain);
        this.announcementIcon = page.locator(".ml-md-3.col-4.d-flex.flex-row.justify-content-start.widht-class > h5");
        this.newFilterGroup = page.locator("//button[normalize-space()='+New Filter']");
        this.filterName = page.locator("(//input[@placeholder=\"What's this called?\"])[1]");
        this.autoRenewToggle = page.locator("(//span[@class='slider round'])[2]");
        this.tippedToggle = page.locator("(//span[@class='slider round'])[3]");
        this.tippedAmount = page.locator("(//input[@placeholder='Amount'])[1]");
        this.spentOverToggle = page.locator("(//span[@class='slider round'])[4]");
        this.spentAmount = page.locator("(//input[@placeholder='Amount'])[2]");
        this.subscribedToggle = page.locator("(//span[@class='slider round'])[5]");
        this.subscribedCount = page.locator("(//input[@placeholder='Number of'])[1]");
        this.subscribedTime = page.locator("(//button[@type='button'])[3]");
        this.inActiveOverToggle = page.locator("(//span[@class='slider round'])[6]");
        this.inActiveCount = page.locator("(//input[@placeholder='Number of'])[2]");
        this.inActiveTime = page.locator("(//button[@type='button'])[4]");
        this.saveChangesBtn = page.locator("button[class='btn button-class']");
        this.closeBtn = page.locator(".btn.btn-outline-secondary");
        this.uploadIcon = page.locator(".mt-2.px-2.mr-2 > img");
        this.sendBtn = page.locator(".mt-2.ml-2 > img");
        this.messageText = page.locator("div[placeholder='Type your message']");
        this.uploadFiles = page.locator("//input[@id='fileInput']");
        this.uploadMessage = page.locator("input[placeholder='Enter message']");
        this.unlockPrice = page.locator(".col-sm-12.mt-2 > input");
        this.uploadBtn = page.locator(".modal-footer > button.btn.button-class.font-weight-600");
        this.threeDots = page.locator("img[class='dropdown-toggle']");
        this.editFilter = page.locator("div[class='dropdown dropleft show'] a:nth-child(1)");
        this.deleteFilter = page.locator("div[class='dropdown dropleft show'] a:nth-child(2)");
    }

    public void navigate() {
        String expectedUrl = domain + "/creatorMessageDashboard/in";
        page.navigate(expectedUrl);
        page.waitForLoadState();
        assertTrue(page.url().startsWith(expectedUrl), "URL does not match the expected pattern");
    }

    public void createAnnouncement(String filterName, String tippedAmount, String spentAmount, String subscribedOverNumber, String inActiveNumber) {
        try {
            newFilterGroup.click();
            page.waitForTimeout(3000);
            this.filterName.fill(filterName);
            autoRenewToggle.click();
            tippedToggle.click();
            int tip = (int) Double.parseDouble(tippedAmount);
            this.tippedAmount.fill(String.valueOf(tip));
            spentOverToggle.click();
            int spent = (int) Double.parseDouble(spentAmount);
            this.spentAmount.fill(String.valueOf(spent));
            subscribedToggle.click();
            int subscribed = (int) Double.parseDouble(subscribedOverNumber);
            this.subscribedCount.fill(String.valueOf(subscribed));
            subscribedTime.click();
            page.waitForTimeout(3000);
            page.locator("div[class='dropdown show'] a:nth-child(1)").click();
            inActiveOverToggle.click();
            int inActive = (int) Double.parseDouble(inActiveNumber);
            this.inActiveCount.fill(String.valueOf(inActive));
            inActiveTime.click();
            page.locator("div[class='dropdown show'] a:nth-child(2)").click();
            saveChangesBtn.click();
            page.waitForTimeout(3000);
            List<ElementHandle> announcements = page.locator(".font-size-14.text-white.font-weight-600").elementHandles();
            assertEquals(filterName, announcements.get(announcements.size() - 1).textContent().trim(), "Announcement Name does not match.");
            log.info("Announcement created successfully and verified.");
        }
        catch (Exception e) {
            log.error("Error occurred while creating announcement: {}", e.getMessage());
            throw e;
        }
    }

    public void editAnnouncement(String editFilterName, String newFilterName) {
        navigate();
        page.waitForTimeout(3000);
        Locator announcements = page.locator(".font-size-14.text-white.font-weight-600");
        for (ElementHandle announcement : announcements.elementHandles()) {
            if (announcement.textContent().trim().equals(editFilterName)) {
                threeDots.last().click();
                editFilter.click();
                filterName.fill("");
                filterName.fill(newFilterName);
                saveChangesBtn.click();
                break;
            }
        }
        page.waitForTimeout(3000);
        List<ElementHandle> announcementEdited = page.locator(".font-size-14.text-white.font-weight-600").elementHandles();
        if (announcementEdited.isEmpty()) {
            log.error("No announcements found after editing. Edit operation may have failed.");
        }
        String editedAnnouncementName = announcementEdited.get(announcementEdited.size() - 1).textContent().trim();
        assertEquals(newFilterName, editedAnnouncementName, "Announcement Name does not match.");
        log.info("Announcement edited successfully and verified.");
    }


    public void sendMessageAnnouncement(String existsFilterName, String message, String filePath, String uploadMsg, String minPrice) {
        Locator announcements = findAnnouncementByName(existsFilterName);
        if (announcements != null) {
            page.locator("div[class=\"row d-flex flex-column justify-content-start outline-gradient-a\"] div[class=\"col-10 d-flex flex-column justify-content-between mt-2 py-3\"]").click();
            messageText.fill(message);
            sendBtn.click();
            page.waitForTimeout(2000);

            if (isMessageSent(message)) {
                log.info("Message sent successfully: {}", message);
                uploadFile(filePath, uploadMsg, minPrice);
            } else {
                log.warn("Message was not sent: {}", message);
            }
        } else {
            log.warn("Announcement not found or not visible: {}", existsFilterName);
        }
    }

    private Locator findAnnouncementByName(String existsFilterName) {
        announcementIcon.click();
        page.waitForTimeout(2000);
        Locator announcementContainer = page.locator("//*[@class='col-10 d-flex flex-column justify-content-between mt-2 py-3']");
        Locator filter = announcementContainer.locator(String.format("p:has-text('%s')", existsFilterName));

        if (filter.count() > 0) {
            log.info("Announcement found: {}", existsFilterName);
            return filter;
        } else {
            log.warn("Announcement not found: {}", existsFilterName);
            return null;
        }
    }

    private boolean isMessageSent(String message) {
        page.waitForTimeout(2000);
        Locator sentMessage = page.locator(".d-flex.flex-column.align-items-end > span");
        for (int i = 0; i < sentMessage.count(); i++) {
            if (sentMessage.nth(i).textContent().contains(message)) {
                log.info(sentMessage.nth(i).textContent());
                return true;
            }
        }
        return false;
    }

    private void uploadFile(String filePath, String uploadMsg, String minPrice) {
        page.waitForTimeout(3000);
        uploadIcon.click();
        uploadFiles.setInputFiles(Paths.get(filePath));
        uploadMessage.fill(uploadMsg);
        try {
            double minPriceValue = Double.parseDouble(minPrice);
            if (minPriceValue >= 25) {
                unlockPrice.fill(minPrice);
                page.waitForTimeout(2000);
                uploadBtn.click();
                log.info("File uploaded successfully with minimum price: " + minPrice);
            } else {
                log.warn("Minimum Price should be >= 25. Provided: " + minPrice);
            }
        } catch (NumberFormatException e) {
            log.error("Invalid minimum price format: " + minPrice);
        }
    }

    public void deleteAnnouncement(String deleteFilterName) {
        navigate();
        page.waitForTimeout(2000);
        Locator announcements = page.locator(".font-size-14.text-white.font-weight-600");
        for (ElementHandle filter : announcements.elementHandles()) {
            if (filter.textContent().trim().equals(deleteFilterName)) {
                threeDots.last().click();
                deleteFilter.click();
                page.waitForTimeout(2000);
                log.info("Announcement deleted successfully.");
                return;
            }
        }
        log.error("Filter not found.");
    }
}

