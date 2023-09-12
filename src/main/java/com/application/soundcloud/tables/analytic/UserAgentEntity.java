package com.application.soundcloud.tables.analytic;

import com.application.soundcloud.tables.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_agent_logs")
public class UserAgentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userId;
    private String country;
    private String operatingSystem;
    private String operatingSystemGroup;
    private String deviceType; // desktop or mobile etc
    private String browser;
    private String browseVersion;
    private String browseType;
    private LocalDateTime time;

    public UserAgentEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String os) {
        this.operatingSystem = os;
    }

    public String getOperatingSystemGroup() {
        return operatingSystemGroup;
    }

    public void setOperatingSystemGroup(String operatingSystemGroup) {
        this.operatingSystemGroup = operatingSystemGroup;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String typeOfOS) {
        this.deviceType = typeOfOS;
    }



    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowseVersion() {
        return browseVersion;
    }

    public void setBrowseVersion(String browseVersion) {
        this.browseVersion = browseVersion;
    }

    public String getBrowseType() {
        return browseType;
    }

    public void setBrowseType(String browseType) {
        this.browseType = browseType;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
