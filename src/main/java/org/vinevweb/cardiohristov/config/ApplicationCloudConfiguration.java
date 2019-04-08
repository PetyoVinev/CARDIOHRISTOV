package org.vinevweb.cardiohristov.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class ApplicationCloudConfiguration {

    public static final String CLOUD_NAME = "cloud_name";
    public static final String API_KEY = "api_key";
    public static final String API_SECRET = "api_secret";

    @Value("${cloudinary.cloud-name}")
    private String cloudApiName;

    @Value("${cloudinary.api-key}")
    private String cloudApiKey;

    @Value("${cloudinary.api-secret}")
    private String cloudApiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(new HashMap<String, Object>(){{
            put(CLOUD_NAME, cloudApiName);
            put(API_KEY, cloudApiKey);
            put(API_SECRET, cloudApiSecret);
        }});
    }
}
