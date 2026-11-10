package io.student.rangiffler.ui.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import io.student.rangiffler.config.TestConfig;
import io.student.rangiffler.ui.component.PeopleTable;

public final class PeoplePage {

  public static final String URL = TestConfig.frontUrl() + "/people";

  private final Page page;
  private final PeopleTable peopleTable;
  private final Locator friendsTab;
  private final Locator allPeopleTab;
  private final Locator outcomeInvitationsTab;
  private final Locator incomeInvitationsTab;

  public PeoplePage(Page page) {
    this.page = page;
    this.peopleTable = new PeopleTable(page);
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
    peopleTable.searchPeople(query);
    return this;
  }

  @Step("Add person: {username}")
  public PeoplePage clickAdd(String username) {
    peopleTable.row(username).clickAdd();
    return this;
  }

  @Step("Check that remove count is {count}")
  public PeoplePage checkThatRemoveCount(int count) {
    peopleTable.checkThatRemoveCount(count);
    return this;
  }

  @Step("Check that accept count is {count}")
  public PeoplePage checkThatAcceptCount(int count) {
    peopleTable.checkThatAcceptCount(count);
    return this;
  }

  @Step("Check that username {username} is shown")
  public PeoplePage checkThatUsername(String username) {
    peopleTable.row(username).checkThatUsername();
    return this;
  }

  @Step("Check that username {username} is absent")
  public PeoplePage checkThatUsernameAbsent(String username) {
    peopleTable.row(username).checkThatUsernameAbsent();
    return this;
  }

  @Step("Check that Remove is shown for {username}")
  public PeoplePage checkThatRemove(String username) {
    peopleTable.row(username).checkThatRemove();
    return this;
  }

  @Step("Check that Accept is shown for {username}")
  public PeoplePage checkThatAccept(String username) {
    peopleTable.row(username).checkThatAccept();
    return this;
  }

  @Step("Check that Decline is shown for {username}")
  public PeoplePage checkThatDecline(String username) {
    peopleTable.row(username).checkThatDecline();
    return this;
  }

  @Step("Check that Cancel is shown for {username}")
  public PeoplePage checkThatCancel(String username) {
    peopleTable.row(username).checkThatCancel();
    return this;
  }

  @Step("Check that invitation to {username} is waiting")
  public PeoplePage checkThatInvitationWaiting(String username) {
    peopleTable.row(username).checkThatInvitationWaiting();
    return this;
  }

  @Step("Check that people list is empty: {message}")
  public PeoplePage checkThatNoUsers(String message) {
    peopleTable.checkThatNoUsers(message);
    return this;
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
