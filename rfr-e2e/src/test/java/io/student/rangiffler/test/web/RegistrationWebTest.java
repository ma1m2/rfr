package io.student.rangiffler.test.web;

import com.microsoft.playwright.Page;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.student.rangiffler.jupiter.BrowserExtension;
import io.student.rangiffler.page.LandingPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

@Epic("Web")
@Feature("Auth")
@Story("Registration")
@Tag("web")
@ExtendWith(BrowserExtension.class)
class RegistrationWebTest {

  @Test
  @DisplayName("User can register and then authorize into the cabinet")
  void shouldAuthorizeAfterRegistration(Page page) {
    String username = "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    String password = "Passw0rd1";

    new LandingPage(page)
        .open()
        .clickRegister()
        .register(username, password)
        .verifySuccess()
        .clickSignIn()
        .clickLogin()
        .signIn(username, password)
        .openProfile()
        .verifyUsername(username);
  }
}
