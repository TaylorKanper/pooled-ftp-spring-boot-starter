package com.kanper.conf;

import com.kanper.service.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kangpeng
 */
@Configuration
@ConditionalOnClass(FtpService.class)
@EnableConfigurationProperties(FtpConfig.class)
public class FtpAutoConfigure {
    @Autowired
    private FtpConfig properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "ftp", value = "enabled", havingValue = "true")
    public FtpService exampleService() throws Exception {
        return new FtpService(properties);
    }

}
