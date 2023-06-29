package com.example.full.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Config {

    public static String GetMac(){
        String macAddress = "";
        try {
            Process process = Runtime.getRuntime().exec("getmac /fo csv /nh");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                macAddress = values[0].replace("\"", "");
            }
            System.out.println("SDf");


            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }
}

