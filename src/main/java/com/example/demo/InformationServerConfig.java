package com.example.demo;

import com.example.demo.service.ProcessMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.h2.tools.Server;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableIntegration
public class InformationServerConfig {

    private final Logger log = LogManager.getLogger(InformationServerConfig.class);

    @Value("${informationServer.port}")
    private int serverPort;

    @Bean
    public TcpNetServerConnectionFactory serverConnectionFactory() {
        TcpNetServerConnectionFactory connectionFactory = new TcpNetServerConnectionFactory(serverPort);
        connectionFactory.setSerializer(new CustomSerializerDeserializer());
        connectionFactory.setDeserializer(new CustomSerializerDeserializer());
        connectionFactory.setSoTimeout(5000);
        connectionFactory.setSoKeepAlive(true);
        return connectionFactory;
    }

    @Bean
    public IntegrationFlow informationServerFlow(
            TcpNetServerConnectionFactory serverConnectionFactory,
            ProcessMessageService processMessageService) {
        return IntegrationFlows
                .from(Tcp.inboundGateway(serverConnectionFactory))
                .handle(processMessageService::processQuery)
                .get();
    }

/*    @Bean
    public ProcessMessage informationServer() {
        return new ProcessMessage();
    }*/

    @Bean
    @Qualifier("tcpConnections")
    public Set<String> tcpConnections() {
        return Collections.synchronizedSet(new HashSet<>());
    }


    @Bean
    public Server h2WebServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8088").start();
    }

}
