package io.student.rangiffler.test.people;

import com.microsoft.playwright.Page;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.student.rangiffler.data.TestUser;
import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.test.web.BaseWebTest;
import io.student.rangiffler.ui.page.LandingPage;
import io.student.rangiffler.ui.page.PeoplePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("Web")
@Feature("People")
@Story("Friends")
@Tag("web")
class FriendsTabTest extends BaseWebTest {

  @Test
  @DisplayName("Friends tab shows the friend with Remove and hides the income invitation")
  void shouldShowFriendAndHideIncomeInvitation(
      @User(friends = 1, incomeInvitations = 1) TestUser user,
      Page page
  ) {
    TestUser friend = user.friends().getFirst();
    TestUser inviter = user.incomeInvitations().getFirst();
    openPeople(page, user)
        .checkThatUsername(friend.username())
        .checkThatRemove(friend.username())
        .checkThatUsernameAbsent(inviter.username());
  }

  @Test
  @DisplayName("Friends tab is empty without friends")
  void shouldShowEmptyFriends(@User TestUser user, Page page) {
    openPeople(page, user)
        .checkThatNoUsers("There are no users yet");
  }

  private static PeoplePage openPeople(Page page, TestUser user) {
    return new LandingPage(page)
        .open()
        .clickLogin()
        .fillLoginForm(user.username(), user.password())
        .clickSignIn()
        .openPeople();
  }
}
