package com.application.soundcloud.services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;

@Service
public class GeoIpService {
    private final DatabaseReader databaseReader;

    public GeoIpService(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public String getCountryNameByIp(String ipAddress) throws IOException, GeoIp2Exception {
        InetAddress ip = InetAddress.getByName(ipAddress);
        CountryResponse response = databaseReader.country(ip);

        String countryCode = response.getCountry().getIsoCode();

        Locale locale = new Locale("", countryCode);
        String countryName = locale.getDisplayCountry();

        return countryName;
    }
}
