package com.sijanstu.playwrighthamroapi.automation;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.sijanstu.playwrighthamroapi.playwright.Chromium;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Automate {
    Page page;

    public Automate() {
        Chromium chromium = new Chromium();
        BrowserContext browserContext = chromium.getNormalPage(true);
        page = browserContext.pages().get(0);
    }

    public boolean sendMessage(User user) {
        page.navigate("https://web.whatsapp.com/");
        page.waitForLoadState();
        page.waitForSelector("#side > div._3gYev > div > div > div._2vDPL > div > div > p > span");
        page.type("#side > div._3gYev > div > div > div._2vDPL > div > div > p > span", user.getPhoneNumber());
        return true;
    }

    public static void main(String[] args) {
        Automate automate = new Automate();
        User user = new User();
        user.setPhoneNumber("9840000000");
        automate.sendMessage(user);
    }

    //read nth column of csv file and store in arraylist
    public ArrayList<String> readCSV(String path, int column,boolean header) {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader br=new BufferedReader(new FileReader(path));
            String line;
            int i=0;
            while ((line=br.readLine())!=null){
                if(header && i==0){
                    i++;
                    continue;
                }
                String[] values=line.split(",");
                list.add(values[column]);
            }
            return list;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading csv file, please check the file and try again");
        }
        return null;
    }
}
