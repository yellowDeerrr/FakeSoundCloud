package com.application.soundcloud.services.analytic;

import com.application.soundcloud.repositories.analytic.UserAgentRepository;
import com.application.soundcloud.services.GeoIpService;
import com.application.soundcloud.tables.UserEntity;
import com.application.soundcloud.tables.analytic.UserAgentEntity;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserAgentService {
    @Autowired
    private GeoIpService geoIpService;
    @Autowired
    private UserAgentRepository userAgentRepository;
    public void addUserAgentInDB(UserEntity userEntity, UserAgent userAgent, String remoteAddr) throws IOException, GeoIp2Exception {
        UserAgentEntity userAgentEntity = new UserAgentEntity();

        userAgentEntity.setBrowser(userAgent.getBrowser().getGroup().getName());
        userAgentEntity.setBrowseVersion(userAgent.getBrowserVersion().getVersion());
        userAgentEntity.setBrowseType(userAgent.getBrowser().getBrowserType().getName());

        userAgentEntity.setOperatingSystem(userAgent.getOperatingSystem().getName());
        userAgentEntity.setOperatingSystemGroup(userAgent.getOperatingSystem().getGroup().getName());

        userAgentEntity.setDeviceType(userAgent.getOperatingSystem().getDeviceType().getName());

        userAgentEntity.setUserId(userEntity);
//          userAgentEntity.setCountry(geoIpService.getCountryNameByIp(remoteAddr));
        userAgentEntity.setCountry("USA");
        userAgentEntity.setTime(LocalDateTime.now());

        userAgentRepository.save(userAgentEntity);
    }

    public Map<String, Double> getUsersByCountryPercentage() {
        List<UserAgentEntity> users = userAgentRepository.findAll();

        Map<String, Long> countryCounts = users.stream()
                .collect(Collectors.groupingBy(UserAgentEntity::getCountry, Collectors.counting()));

        long totalUsers = users.size();

        return countryCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (double) entry.getValue() / totalUsers * 100.0
                ));
    }

    public Map<String, Double> getUsersByPopularOperatingSystemsPercentage() {
        List<String> popularOperatingSystems = List.of("Windows", "macOS", "Linux", "iOS", "Android");
        List<UserAgentEntity> users = userAgentRepository.findAll();

        Map<String, Long> osCounts = users.stream()
                .collect(Collectors.groupingBy(UserAgentEntity::getOperatingSystemGroup, Collectors.counting()));

        long totalUsers = users.size();

        long otherCount = osCounts.entrySet().stream()
                .filter(entry -> !popularOperatingSystems.contains(entry.getKey()))
                .mapToLong(Map.Entry::getValue)
                .sum();

        osCounts.put("others", otherCount);

        Map<String, Long> popularOsCounts = osCounts.entrySet().stream()
                .filter(entry -> popularOperatingSystems.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Double> percentages = popularOsCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (double) entry.getValue() / totalUsers * 100.0
                ));

        percentages.put("others", (double) otherCount / totalUsers * 100.0);

        return percentages;
    }


    public Map<String, Double> getUsersByPopularBrowsersPercentage() {
        List<String> popularBrowsers = List.of("Chrome", "Firefox", "Safari", "Edge", "Opera");
        List<UserAgentEntity> users = userAgentRepository.findAll();

        Map<String, Long> browserCounts = users.stream()
                .collect(Collectors.groupingBy(UserAgentEntity::getBrowser, Collectors.counting()));

        long totalUsers = users.size();

        long otherCount = browserCounts.entrySet().stream()
                .filter(entry -> !popularBrowsers.contains(entry.getKey()))
                .mapToLong(Map.Entry::getValue)
                .sum();

        browserCounts.put("others", otherCount);

        Map<String, Long> popularBrowserCounts = browserCounts.entrySet().stream()
                .filter(entry -> popularBrowsers.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Double> percentages = popularBrowserCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (double) entry.getValue() / totalUsers * 100.0
                ));

        percentages.put("others", (double) otherCount / totalUsers * 100.0);

        return percentages;
    }

    public Map<String, Double> getUsersByPopularDeviceTypesPercentage() {
        List<String> popularDeviceTypes = List.of("Computer", "Desktop", "Mobile", "Tablet", "Smart TV");
        List<UserAgentEntity> users = userAgentRepository.findAll();

        Map<String, Long> deviceTypeCounts = users.stream()
                .collect(Collectors.groupingBy(UserAgentEntity::getDeviceType, Collectors.counting()));

        long totalUsers = users.size();

        // Об'єднуємо непопулярні типи пристроїв під "others"
        long otherCount = deviceTypeCounts.entrySet().stream()
                .filter(entry -> !popularDeviceTypes.contains(entry.getKey()))
                .mapToLong(Map.Entry::getValue)
                .sum();

        deviceTypeCounts.put("others", otherCount);

        // Фільтруємо популярні типи пристроїв
        Map<String, Long> popularDeviceTypeCounts = deviceTypeCounts.entrySet().stream()
                .filter(entry -> popularDeviceTypes.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Розраховуємо відсотки для кожного типу пристрою
        Map<String, Double> percentages = popularDeviceTypeCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (double) entry.getValue() / totalUsers * 100.0
                ));

        // Додаємо окремі відсотки для "others"
        percentages.put("others", (double) otherCount / totalUsers * 100.0);

        return percentages;
    }
}
