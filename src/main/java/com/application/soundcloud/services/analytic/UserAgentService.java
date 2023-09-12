package com.application.soundcloud.services.analytic;

import com.application.soundcloud.repositories.analytic.UserAgentRepository;
import com.application.soundcloud.services.GeoIpService;
import com.application.soundcloud.tables.UserEntity;
import com.application.soundcloud.tables.analytic.UserAgentEntity;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.maxmind.geoip2.exception.GeoIp2Exception;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class UserAgentService {
    @Autowired
    private GeoIpService geoIpService;
    @Autowired
    private UserAgentRepository userAgentRepository;
    public void addUserAgentInDB(UserEntity userEntity, UserAgent userAgent, HttpServletRequest request) throws IOException, GeoIp2Exception {
        UserAgentEntity userAgentEntity = new UserAgentEntity();

        userAgentEntity.setBrowser(userAgent.getBrowser().getGroup().getName());
        userAgentEntity.setBrowseVersion(userAgent.getBrowserVersion().getVersion());
        userAgentEntity.setBrowseType(userAgent.getBrowser().getBrowserType().getName());

        userAgentEntity.setOperatingSystem(userAgent.getOperatingSystem().getName());
        userAgentEntity.setOperatingSystemGroup(userAgent.getOperatingSystem().getGroup().getName());

        userAgentEntity.setDeviceType(userAgent.getOperatingSystem().getDeviceType().getName());

        userAgentEntity.setUserId(userEntity);
//        userEntity.setCountry(geoIpService.getCountryNameByIp(request.getRemoteAddr()));
        userAgentEntity.setTime(LocalDateTime.now());

        userAgentRepository.save(userAgentEntity);
    }
}
