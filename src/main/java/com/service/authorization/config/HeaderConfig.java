package com.service.authorization.config;

import org.springframework.stereotype.Component;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

@Component
public class HeaderConfig {

    @Value("${dikti.headers.accept}")
    private String accept;

    @Value("${dikti.headers.accept-encoding}")
    private String acceptEncoding;

    @Value("${dikti.headers.accept-language}")
    private String acceptLanguage;

    @Value("${dikti.headers.connection}")
    private String connection;

    @Value("${dikti.headers.host}")
    private String host;

    @Value("${dikti.headers.origin}")
    private String origin;

    @Value("${dikti.headers.referer}")
    private String referer;

    @Value("${dikti.headers.sec-ch-ua}")
    private String secChUa;

    @Value("${dikti.headers.sec-ch-ua-mobile}")
    private String secChUaMobile;

    @Value("${dikti.headers.sec-ch-ua-platform}")
    private String secChUaPlatform;

    @Value("${dikti.headers.sec-fetch-dest}")
    private String secFetchDest;

    @Value("${dikti.headers.sec-fetch-mode}")
    private String secFetchMode;

    @Value("${dikti.headers.sec-fetch-site}")
    private String secFetchSite;

    @Value("${dikti.headers.user-agent}")
    private String userAgent;

    @Value("${dikti.headers.x-user-ip}")
    private String xUserIp;

    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", accept);
        httpHeaders.set("Accept-Encoding", acceptEncoding);
        httpHeaders.set("Accept-Language", acceptLanguage);
        httpHeaders.set("Connection", connection);
        httpHeaders.set("Host", host);
        httpHeaders.set("Origin", origin);
        httpHeaders.set("Referer", referer);
        httpHeaders.set("Sec-CH-UA", secChUa);
        httpHeaders.set("Sec-CH-UA-Mobile", secChUaMobile);
        httpHeaders.set("Sec-CH-UA-Platform", secChUaPlatform);
        httpHeaders.set("Sec-Fetch-Dest", secFetchDest);
        httpHeaders.set("Sec-Fetch-Mode", secFetchMode);
        httpHeaders.set("Sec-Fetch-Site", secFetchSite);
        httpHeaders.set("User-Agent", userAgent);
        httpHeaders.set("X-User-IP", xUserIp);
        return httpHeaders;
    }
}
