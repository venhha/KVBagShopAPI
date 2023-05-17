package com.example.kvbagshopapi.utils;

import java.util.Random;

public class MyUtils {
    public static String getRandomName() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // Sinh số ngẫu nhiên từ 1000 đến 9999
        String name = "User" + randomNumber;
        return name;
    }

}