package io.student.rangiffler.ui.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterPage {

  private final Page page;
  private final Locator usernameInput;
  private final Locator passwordInput;
  private final Locator confirmPasswordInput;
  private final Locator signUpButton;
  private final Locator successMessage;
  private final Locator signInLink;

  public RegisterPage(Page page) {
    this.page = page;
    this.usernameInput = page.locator("#username");
    this.passwordInput = page.locator("#password");
    this.confirmPasswordInput = page.locator("#passwordSubmit");
    this.signUpButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign up"));
    this.successMessage = page.locator(".form__paragraph_success");
    this.signInLink = page.locator("a.form_sign-in");
  }

  @Step("Wait until registration form is opened")
  public RegisterPage waitOpened() {
    page.waitForURL("**/register**");
    assertThat(usernameInput).isVisible();
    return this;
  }

  @Step("Register user {username}")
  public RegisterPage register(String username, String password) {
    waitOpened();
    usernameInput.fill(username);
    passwordInput.fill(password);
    confirmPasswordInput.fill(password);
    signUpButton.click();
    return this;
  }

  @Step("Check successful registration message")
  public RegisterPage verifySuccess() {
    assertThat(successMessage).hasText("Congratulations! You've registered!");
    return this;
  }

  @Step("Go to frontend from registration success")
  public LandingPage clickSignIn() {
    signInLink.click();
    return new LandingPage(page).waitOpened();
  }
}
