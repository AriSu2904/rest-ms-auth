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
        if(name.contains(" ")) {
            name = name.split(" ")[0];
        }

        return url + generateGender(gender) + "/" + name;
    }
}
