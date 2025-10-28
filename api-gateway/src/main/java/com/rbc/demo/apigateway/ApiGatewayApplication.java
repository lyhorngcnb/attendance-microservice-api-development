// ========================================
// 8. ApiGatewayApplication.java (Enhanced Main Class)
// Location: src/main/java/.../apigateway/ApiGatewayApplication.java
// ========================================
package com.rbc.demo.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Slf4j
public class ApiGatewayApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
        
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "/");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        
        log.info("\n----------------------------------------------------------\n" +
                "🚀 API Gateway is running!\n" +
                "----------------------------------------------------------\n" +
                "📍 Local:      http://localhost:{}{}\n" +
                "📍 External:   http://{}:{}{}\n" +
                "📊 Actuator:   http://localhost:{}/actuator\n" +
                "🔒 Profile:    {}\n" +
                "----------------------------------------------------------",
                port, contextPath,
                hostAddress, port, contextPath,
                port,
                env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "default");
    }
}
