package io.student.rangiffler.test.web;

import com.microsoft.playwright.Page;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.student.rangiffler.ui.page.LandingPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.student.rangiffler.utils.RandomDataUtils.randomUsername;

@Epic("Web")
@Feature("Auth")
@Story("Registration")
@Tag("web")
class RegistrationWebTest extends BaseWebTest {

  @Test
  @DisplayName("User can register and then authorize into the cabinet")
  void shouldAuthorizeAfterRegistration(Page page) {
    String username = randomUsername();
    String password = "Test123!";

    new LandingPage(page)
        .open()
        .clickRegister()
        .fillRegistrationForm(username, password)
        .checkThatSuccessMessage("Congratulations! You've registered!")
        .clickSignIn()
        .clickLogin()
        .fillLoginForm(username, password)
        .openProfile()
        .checkThatUsername(username);
  }
}
