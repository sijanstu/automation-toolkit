package com.sijanstu.playwrighthamroapi.playwright;


import com.microsoft.playwright.*;

import javax.swing.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Chromium {
    static Playwright playwright;

    public BrowserContext getNormalPage(boolean visible) {
        BrowserContext context = getPage(Path.of(System.getProperty("user.dir"), "data"), !visible);
        if (context == null) {
                JOptionPane.showMessageDialog(null, "Please close all instances of chromium and click ok to continue");
                return getPage(Path.of(System.getProperty("user.dir"), "data"), !visible);
        }
        return context;
    }
        public BrowserContext getPage (Path userDataDir,boolean headless){
            try {
                Playwright.CreateOptions options = new Playwright.CreateOptions();
                options.setEnv(new HashMap<>(System.getenv()));
                options.env.put("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");
                if (playwright != null)
                    playwright.close();
                playwright = Playwright.create(options);
                BrowserType.LaunchPersistentContextOptions launchOptions = new BrowserType.LaunchPersistentContextOptions();
                launchOptions.setHeadless(headless);
                launchOptions.setChannel("chrome");
                launchOptions.setArgs(List.of("--disable-blink-features=AutomationControlled"));
                launchOptions.setIgnoreDefaultArgs(List.of("--enable-automation"));
                BrowserType browserType = playwright.chromium();
                return browserType.launchPersistentContext(userDataDir, launchOptions);
            } catch (PlaywrightException e) {
                System.out.println("Failed to launch browser: " + e.getMessage());
                return null;
            }
        }
    }
