package io.student.rangiffler.ui.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import io.student.rangiffler.config.TestConfig;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class PeoplePage {

  public static final String URL = TestConfig.frontUrl() + "/people";

  private final Page page;
  private final Locator friendsTab;
  private final Locator allPeopleTab;
  private final Locator outcomeInvitationsTab;
  private final Locator incomeInvitationsTab;
  private final Locator searchInput;
  private final Locator searchButton;
  private final Locator removeButtons;
  private final Locator acceptButtons;
  private final Locator emptyUsers;

  public PeoplePage(Page page) {
    this.page = page;
    this.friendsTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Friends").setExact(true));
    this.allPeopleTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("All People").setExact(true));
    this.outcomeInvitationsTab = page.getByRole(
        AriaRole.TAB,
        new Page.GetByRoleOptions().setName("Outcome invitations").setExact(true)
    );
    this.incomeInvitationsTab = page.getByRole(
        AriaRole.TAB,
        new Page.GetByRoleOptions().setName("Income invitations").setExact(true)
    );
    this.searchInput = page.getByRole(
        AriaRole.TEXTBOX,
        new Page.GetByRoleOptions().setName("search people").setExact(true)
    );
    this.searchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("search").setExact(true));
    this.removeButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove").setExact(true));
    this.acceptButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept").setExact(true));
    this.emptyUsers = page.getByRole(AriaRole.TABPANEL).getByRole(AriaRole.PARAGRAPH);
  }

  @Step("Open friends tab")
  public PeoplePage clickFriends() {
    waitForGraphql("GetFriends", friendsTab::click);
    return this;
  }

  @Step("Open all people tab")
  public PeoplePage clickAllPeople() {
    waitForGraphql("GetPeople", allPeopleTab::click);
    return this;
  }

  @Step("Open outcome invitations tab")
  public PeoplePage clickOutcomeInvitations() {
    waitForGraphql("GetOutcomeInvitations", outcomeInvitationsTab::click);
    return this;
  }

  @Step("Open income invitations tab")
  public PeoplePage clickIncomeInvitations() {
    waitForGraphql("GetInvitations", incomeInvitationsTab::click);
    return this;
  }

  @Step("Search people: {query}")
  public PeoplePage searchPeople(String query) {
    searchInput.fill(query);
    waitForGraphql("GetPeople", searchButton::click);
    return this;
  }

  @Step("Add person: {username}")
  public PeoplePage clickAdd(String username) {
    waitForGraphql(
        "FriendshipAction",
        () -> person(username)
            .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Add").setExact(true))
            .click()
    );
    return this;
  }

  @Step("Check that remove count is {count}")
  public PeoplePage checkThatRemoveCount(int count) {
    assertThat(removeButtons).hasCount(count);
    return this;
  }

  @Step("Check that accept count is {count}")
  public PeoplePage checkThatAcceptCount(int count) {
    assertThat(acceptButtons).hasCount(count);
    return this;
  }

  @Step("Check that username {username} is shown")
  public PeoplePage checkThatUsername(String username) {
    assertThat(person(username)).isVisible();
    return this;
  }

  @Step("Check that username {username} is absent")
  public PeoplePage checkThatUsernameAbsent(String username) {
    assertThat(person(username)).hasCount(0);
    return this;
  }

  @Step("Check that Remove is shown for {username}")
  public PeoplePage checkThatRemove(String username) {
    assertThat(button(username, "Remove")).isVisible();
    return this;
  }

  @Step("Check that Accept is shown for {username}")
  public PeoplePage checkThatAccept(String username) {
    assertThat(button(username, "Accept")).isVisible();
    return this;
  }

  @Step("Check that Decline is shown for {username}")
  public PeoplePage checkThatDecline(String username) {
    assertThat(button(username, "Decline")).isVisible();
    return this;
  }

  @Step("Check that Cancel is shown for {username}")
  public PeoplePage checkThatCancel(String username) {
    assertThat(button(username, "Cancel")).isVisible();
    return this;
  }

  @Step("Check that invitation to {username} is waiting")
  public PeoplePage checkThatInvitationWaiting(String username) {
    assertThat(person(username).getByText("Waiting...", new Locator.GetByTextOptions().setExact(true))).isVisible();
    return this;
  }

  @Step("Check that people list is empty: {message}")
  public PeoplePage checkThatNoUsers(String message) {
    assertThat(emptyUsers).hasText(message);
    return this;
  }

  private Locator person(String username) {
    return page.getByRole(AriaRole.ROW)
        .filter(new Locator.FilterOptions().setHasText(username));
  }

  private Locator button(String username, String name) {
    return person(username).getByRole(
        AriaRole.BUTTON,
        new Locator.GetByRoleOptions().setName(name).setExact(true)
    );
  }

  private void waitForGraphql(String operation, Runnable click) {
    page.waitForResponse(
        response -> response.url().contains("/graphql")
            && response.request().postData() != null
            && response.request().postData().contains(operation),
        click
    );
  }
}
