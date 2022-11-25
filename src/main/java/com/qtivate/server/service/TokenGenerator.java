package com.qtivate.server.service;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

public class TokenGenerator {
    private static String preffix = "puc";

    private List<String> classId;

    private String key;

    public static String generateToken(String[] classes) {
        String ret = preffix + ":" + classes[0];
        for(int i = 1; i < classes.length; i++)
            ret += "," + classes[i];
        return ret + ":" + getAlphaNumericString(10);
    }

    public static String generateToken(String classes) {
        return preffix + ":" + classes + ":" + getAlphaNumericString(10);
    }


    public static String getAlphaNumericString(int n) {

        // length is bounded by 256 Character
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString
                = new String(array, Charset.forName("UTF-8"));

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer();

        // Append first 20 alphanumeric characters
        // from the generated random String into the result
        for (int k = 0; k < randomString.length(); k++) {

            char ch = randomString.charAt(k);

            if (((ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9'))
                    && (n > 0)) {

                r.append(ch);
                n--;
            }
        }

        // return the resultant string
        return r.toString();
    }
}
