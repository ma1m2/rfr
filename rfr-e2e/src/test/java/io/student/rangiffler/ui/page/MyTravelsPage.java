package io.student.rangiffler.ui.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyTravelsPage {

  private final Page page;
  private final Locator profileLink;

  public MyTravelsPage(Page page) {
    this.page = page;
    this.profileLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Profile")).first();
  }

  @Step("Open profile from the cabinet")
  public ProfilePage openProfile() {
    page.waitForURL("**/my-travels");
    assertThat(profileLink).isVisible();
    profileLink.click();
    return new ProfilePage(page);
  }
}
