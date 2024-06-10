package com.example.jhta_3team_finalproject.config.trip;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.siot.IamportRestClient.IamportClient;



@Configuration
public class IamportConfig {

    private IamportClient iamportClient;

    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    public IamportClient getIamportClient() {
        return iamportClient;
    }
}