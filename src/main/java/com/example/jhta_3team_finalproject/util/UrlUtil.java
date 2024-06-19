package com.example.jhta_3team_finalproject.util;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {

    //기본 URL을 추출 -> ex : http://localhost:9000/trip/updateMainTrip --> Output: http://localhost:9000/trip
    public static String getBaseUrl(String fullUrl) throws MalformedURLException {
        URL url = new URL(fullUrl);
        String scheme = url.getProtocol();         // http or https
        String serverName = url.getHost();         // hostname or IP
        int serverPort = url.getPort();            // port number, -1 if not set
        String contextPath = url.getPath();        // application context path

        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);
        if (serverPort != -1 && serverPort != 80 && serverPort != 443) {
            baseUrl.append(":").append(serverPort);
        }

        String trimmedContextPath = contextPath.substring(0, contextPath.indexOf("/", 1));
        baseUrl.append(trimmedContextPath);

        return baseUrl.toString();
    }
}
