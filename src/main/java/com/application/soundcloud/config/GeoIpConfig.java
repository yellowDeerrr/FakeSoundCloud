package com.application.soundcloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.maxmind.geoip2.DatabaseReader;
import java.io.File;
import java.io.IOException;
import com.maxmind.geoip2.exception.GeoIp2Exception;

@Configuration
public class GeoIpConfig {
    @Value("${pathToGeoLite2DB}")
    private String pathToGeoLite2DB;
    @Bean
    public DatabaseReader databaseReader() throws IOException {
        File resource = new File(pathToGeoLite2DB + "GeoLite2-Country.mmdb");
        return new DatabaseReader.Builder(resource).build();
    }
}
