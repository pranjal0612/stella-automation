package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.BlockAndUnblockFriendPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class BlockAndUnBlockFriendsTest extends BaseTest {
   @Test
    public void blockAndUnblockFriends() {
       try{
           String domain = getDomain();
           Page page = getPage();
           BlockAndUnblockFriendPage blockAndUnblockFriendsPage = new BlockAndUnblockFriendPage(page, domain);
//           blockAndUnblockFriendsPage.unblockFriend();
       }catch(PlaywrightException e){
           log.error("BlockAndUnBlockFriends Test error :{}",e.getMessage());
           assertFalse(true,e.getMessage());
       }catch (Exception e){
           log.error("BlockAndUnblockFriends Test error :{}",e.getMessage());
           assertFalse(true,e.getMessage());
       }
   }
}
