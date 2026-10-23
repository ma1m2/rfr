package io.student.rangiffler.ui.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProfilePage {

  private final Page page;
  private final Locator heading;
  private final Locator usernameInput;

  public ProfilePage(Page page) {
    this.page = page;
    this.heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("My profile"));
    this.usernameInput = page.locator("#username");
  }

  @Step("Verify profile username is {username}")
  public ProfilePage verifyUsername(String username) {
    page.waitForURL("**/profile");
    assertThat(heading).isVisible();
    assertThat(usernameInput).hasValue(username);
    return this;
  }
}
