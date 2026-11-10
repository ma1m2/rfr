package io.student.rangiffler.test.travels;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.student.rangiffler.data.TestUser;
import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.test.web.BaseWebTest;
import io.student.rangiffler.ui.page.LandingPage;
import org.junit.jupiter.api.Test;

public class LikeFromPageProbeTest extends BaseWebTest {

  @Test
  void likeFromPageIsNotUnique(@User TestUser user, Page page) {
    new LandingPage(page)
        .open()
        .clickLogin()
        .fillLoginForm(user.username(), user.password())
        .clickSignIn();
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("like").setExact(true)).click();
  }
}
