package com.service.authorization.utils;

public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static String generateGender(String gender) {
        if (gender.startsWith("P")) {
            return "girl";
        }
        return "boy";
    }

    public static String defaultImage(String gender, String name) {
        String url = "https://avatar.iran.liara.run/public/";

        String transformedName = name;
        if(name.contains(" ")) {
            transformedName = name.split(" ")[0];
        }

        return url + generateGender(gender) + "/" + transformedName.toLowerCase();
    }

    public static String getNickname(String fullName) {
        if(fullName.contains(" ")) {
            String nickName = fullName.split(" ")[0];
            String firstLetter = nickName.substring(0, 1).toUpperCase();
            String rest = nickName.substring(1).toLowerCase();

            return firstLetter + rest;
        }
        String firstLetter = fullName.substring(0, 1).toUpperCase();
        String rest = fullName.substring(1).toLowerCase();

        return firstLetter + rest;
    }
}
