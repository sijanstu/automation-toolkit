package com.sijanstu.playwrighthamroapi.ui;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.sijanstu.playwrighthamroapi.playwright.Chromium;

public class Main {
    public static void main(String[] args) {
        Chromium chromium = new Chromium();
        BrowserContext context = chromium.getNormalPage(true);
        Page page = context.pages().get(0);
        page.navigate("https://www.gmail.com");
        System.out.println(page.url());
        page.waitForLoadState();
    }
}
