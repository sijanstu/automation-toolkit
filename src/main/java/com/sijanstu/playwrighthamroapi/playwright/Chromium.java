package com.sijanstu.playwrighthamroapi.playwright;

import com.microsoft.playwright.*;

import static com.microsoft.playwright.Playwright.create;

public class Chromium {
    public static void getPage(boolean visible){
        Browser browser;
        try (Playwright playwright = create()) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(visible);
            launchOptions.slowMo = 65.5;
            browser = playwright.chromium().launch(launchOptions);
        }
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://meroshare.cdsc.com.np/#/login", new Page.NavigateOptions());
        page.waitForLoadState();
        String html = page.innerHTML("#selectBranch > select");
        System.out.println(html);

    }
}
