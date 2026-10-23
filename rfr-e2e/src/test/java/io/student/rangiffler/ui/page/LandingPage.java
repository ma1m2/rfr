package io.student.rangiffler.ui.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import io.student.rangiffler.config.TestConfig;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.student.rangiffler.config.TestConfig.frontUrl;

public class LandingPage {

  private final Page page;
  private final Locator loginButton;
  private final Locator registerLink;

  public LandingPage(Page page) {
    this.page = page;
    this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    this.registerLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Register"));
  }

  @Step("Open landing page")
  public LandingPage open() {
    page.navigate(frontUrl());
    return waitOpened();
  }

  @Step("Wait until landing page is visible")
  public LandingPage waitOpened() {
    assertThat(loginButton).isVisible();
    return this;
  }

  @Step("Open registration from landing")
  public RegisterPage clickRegister() {
    registerLink.click();
    return new RegisterPage(page);
  }

  @Step("Start OAuth login from landing")
  public LoginPage clickLogin() {
    loginButton.click();
    return new LoginPage(page);
  }
}
