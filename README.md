# pooled-ftp-spring-boot-starter

> 众所周知，FTP连接每次使用的时候需要建立连接，然后执行业务再销毁连接，但是FTP连接是比较稀缺的资源，这点和JDBC连接比较类似，因此萌生了做一个FTP连接池并集成到Springboot中的项目

## 使用

首先下载该项目
git clone https://github.com/tydic-kanper/pooled-ftp-spring-boot-starter.git

然后

``` java
mvn install
```

将项目的jar包安装到本地maven仓库

在项目中只需要在Springboot的yml文件中引入相应的配置，即可完成自动注入

![pooled-ftp](http://image.kanper.top/pooled-ftp.png "pooled-ftp")

## 核心

```java
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
```

``` java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.kanper.conf.FtpAutoConfigure
```
## 说明

上面的Bean会自动注入到Spring容器；可以在执行业务的地方调用FtpService的方法
