package dev.semkoksharov.vibeshub2.utils;

import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TinyURL {

    public static  String shortURL(String longUrl) throws IOException{
        String requestUrl = "http://tinyurl.com/api-create.php?url=" + longUrl;
        HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
        connection.setRequestMethod(RequestMethod.GET.toString());

        try(Scanner scanner = new Scanner(connection.getInputStream())){
            return scanner.nextLine();
        }
    }
}
