package com.alibaba.csp.sentinel.dashboard.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * 项目启动后打印启动成功提示信息|SwaggerApi文档访问网址
 *
 * @author ht-caofan
 */
@Component
@Order(value = 1)
public class InitialedRunner implements CommandLineRunner, ApplicationListener<WebServerInitializedEvent> {

    /**
     * 生产环境标识符
     */
    private static final String PROD_ENV_KEY = "prodnew";
    private final Logger log = LoggerFactory.getLogger(InitialedRunner.class);
    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    public String springActiveProfile;
    @Value("${nacos.config.namespace:NONE}")
    public String configEnv;
    /**
     * 端口
     */
    private int serverPort;
    /**
     * IP
     */
    private String serverIp;

    @Override
    public void run(String... strings) {
        // 非生产环境都打印环境信息
        if (!PROD_ENV_KEY.equals(springActiveProfile)) {
            log.info("项目启动成功, 配置环境[{}], IP=[{}], Port=[{}]", configEnv, this.serverIp, serverPort);
            log.info("主页访问地址: http://{}:{}/", this.serverIp, serverPort);
        }
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        this.serverPort = webServerInitializedEvent.getWebServer().getPort();
        try {
            this.serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            if (Objects.isNull(this.serverIp)) {
                this.serverIp = "UnknownHost";
            }
        }
    }
}
