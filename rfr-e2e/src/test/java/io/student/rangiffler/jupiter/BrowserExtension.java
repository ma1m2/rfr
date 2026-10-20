package io.student.rangiffler.jupiter;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.ByteArrayInputStream;

public class BrowserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  private static final ExtensionContext.Namespace NS =
      ExtensionContext.Namespace.create(BrowserExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) {
    Playwright playwright = Playwright.create();
    Browser browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions().setHeadless(true)
    );
    BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions()
        .setViewportSize(1280, 800)
        .setLocale("en-US"));
    Page page = browserContext.newPage();
    page.setDefaultTimeout(15_000);
    context.getStore(NS).put(Store.class, new Store(playwright, browser, browserContext, page));
  }

  @Override
  public void afterEach(ExtensionContext context) {
    Store store = context.getStore(NS).get(Store.class, Store.class);
    if (store == null) {
      return;
    }
    try {
      if (context.getExecutionException().isPresent()) {
        Allure.attachment("screenshot.png", new ByteArrayInputStream(store.page.screenshot()));
        Allure.attachment("page-url.txt", store.page.url());
      }
    } finally {
      store.close();
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return parameterContext.getParameter().getType() == Page.class;
  }

  @Override
  public Page resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return extensionContext.getStore(NS).get(Store.class, Store.class).page;
  }

  private record Store(Playwright playwright, Browser browser, BrowserContext context, Page page) {
    void close() {
      context.close();
      browser.close();
      playwright.close();
    }
  }
}
