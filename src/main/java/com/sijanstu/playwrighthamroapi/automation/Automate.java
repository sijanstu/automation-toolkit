package com.sijanstu.playwrighthamroapi.automation;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.sijanstu.playwrighthamroapi.playwright.Chromium;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Automate {

    Page page;

    public Automate() {

    }

    public void start(String csvFile, int index, String message, boolean exclude) {
        Chromium chromium = new Chromium();
        BrowserContext browserContext = chromium.getNormalPage(true);
        page = browserContext.pages().get(0);
        ArrayList<String> list = readCSV(csvFile, index, exclude);
        HashMap<String, Boolean> status = new HashMap<>();
        initialize(page);
        for (String s : list) {
            User user = new User(s, message);
            status.put(s, sendMessage(user, page));
        }
        generateReport(status);
    }

    private void generateReport(HashMap<String, Boolean> status) {
        //create a csv file
        //write status of each user
        File file = new File("report" + System.currentTimeMillis() + ".csv");
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("Phone Number,Status");
            for (String s : status.keySet()) {
                writer.write(s + "," + status.get(s));
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            Logger.getLogger("Error creating file");
        }


    }

    private void initialize(Page page) {
        page.navigate("https://my.openphone.com/inbox");
        page.waitForLoadState();
    }

    private boolean sendMessage(User user, Page page) {
        try {
            page.waitForSelector("#message-button");
            page.click("#message-button");
            page.waitForSelector("//*[@id=\"main-container\"]/div[2]/div[2]/div[2]/div[1]/div[1]/div/div[2]/div[1]/input");
            page.fill("//*[@id=\"main-container\"]/div[2]/div[2]/div[2]/div[1]/div[1]/div/div[2]/div[1]/input", user.getPhoneNumber());
            page.waitForSelector("//*[@id=\"main-container\"]/div[2]/div[2]/div[2]/div[1]/div[3]/div[2]/div[1]/div[1]/div/div/div/p/span/span/span");
            page.click("//*[@id=\"main-container\"]/div[2]/div[2]/div[2]/div[1]/div[3]/div[2]/div[1]/div[1]/div/div/div/p/span/span/span");
            page.fill("//*[@id=\"main-container\"]/div[2]/div[2]/div[2]/div[1]/div[3]/div[2]/div[1]/div[1]/div/div/div/p/span/span/span", user.getMessage());
            page.waitForSelector("//*[@id=\"main-container\"]/div[2]/div[2]/div[2]/div[1]/div[3]/div[2]/div[3]/div/span[2]/button");
            page.click("//*[@id=\"main-container\"]/div[2]/div[2]/div[2]/div[1]/div[3]/div[2]/div[3]/div/span[2]/button");
            //wait for message to be sent
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //read nth column of csv file and store in arraylist
    public ArrayList<String> readCSV(String path, int column, boolean header) {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (header && i == 0) {
                    i++;
                    continue;
                }
                String[] values = line.split(",");
                if (values[column] == null || values[column].isEmpty()) {
                    continue;
                }
                list.add(values[column]);
            }
            return list;
        } catch (Exception e) {
            JOptionPane optionPane = new JOptionPane("Error reading csv file", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        }
        return null;
    }
}
