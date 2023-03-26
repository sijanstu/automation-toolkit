package com.sijanstu.playwrighthamroapi.playwright;


import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Chromium {
    static Playwright playwright;

    public Page getNormalPage() {
        Page page = getPage(getUserDataDir());
        page.navigate("chrome://version");
        page.waitForSelector("#profile_path");
        Document doc = Jsoup.parse(page.innerHTML("#profile_path"));
        String profilePath = doc.text();
        profilePath = profilePath.substring(0, profilePath.lastIndexOf("User Data") + 9);
        Path userDataDir = Paths.get(profilePath);
        Page page1 = getPage(userDataDir);
        page.close();
        return page1;
    }

    public Page getPage(Path userDataDir) {
        try {
            Playwright.CreateOptions options = new Playwright.CreateOptions();
            options.setEnv(new HashMap<>(System.getenv()));
            options.env.put("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");
            playwright = Playwright.create(options);
            BrowserType.LaunchPersistentContextOptions launchOptions = new BrowserType.LaunchPersistentContextOptions();
            launchOptions.setHeadless(false);
            launchOptions.setChannel("chrome");
            return playwright.chromium().launchPersistentContext(userDataDir, launchOptions).pages().get(0);
        } catch (PlaywrightException e) {
            System.out.println("Failed to launch browser: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Chromium chromium = new Chromium();
        Page page = chromium.getNormalPage();
        page.navigate("https://chat.openai.com/chat");
        page.waitForLoadState();
        page.waitForSelector("text=New chat");
        page.type("#__next > div.overflow-hidden.w-full.h-full.relative > div.flex.h-full.flex-1.flex-col.md\\:pl-\\[260px\\] > main > div.absolute.bottom-0.left-0.w-full.border-t.md\\:border-t-0.dark\\:border-white\\/20.md\\:border-transparent.md\\:dark\\:border-transparent.md\\:bg-vert-light-gradient.bg-white.dark\\:bg-gray-800.md\\:\\!bg-transparent.dark\\:md\\:bg-vert-dark-gradient.pt-2 > form > div > div.flex.flex-col.w-full.py-2.flex-grow.md\\:py-3.md\\:pl-4.relative.border.border-black\\/10.bg-white.dark\\:border-gray-900\\/50.dark\\:text-white.dark\\:bg-gray-700.rounded-md.shadow-\\[0_0_10px_rgba\\(0\\,0\\,0\\,0\\.10\\)\\].dark\\:shadow-\\[0_0_15px_rgba\\(0\\,0\\,0\\,0\\.10\\)\\] > textarea", "Hello");
        page.keyboard().press("Enter");
    }

    public static Path getUserDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");
        if (os.contains("win")) {
            return Paths.get(home, "AppData", "Local", "Google", "Chrome", "User Data", "Default");
        } else if (os.contains("mac")) {
            return Paths.get(home, "Library", "Application Support", "Google", "Chrome", "Default", "User Data");
        } else {
            Path path = Paths.get(home, ".config", "google-chrome");
            if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                return path;
            } else if (os.contains("sunos")) {
                return path;
            } else {
                throw new RuntimeException("Unsupported operating system");
            }
        }
    }

}
