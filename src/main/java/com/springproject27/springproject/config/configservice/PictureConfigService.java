package com.springproject27.springproject.config.configservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "picture")
@Getter
@Setter
public class PictureConfigService {
    private String usersPictureRoot;
}
