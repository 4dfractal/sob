package com.example.demo.experiment;

import static com.example.demo.experiment.MessageChannelConfiguration.TCP_INPUT_CHANNEL_NAME;
import static com.example.demo.experiment.MessageChannelConfiguration.TCP_OUTPUT_CHANNEL_NAME;

import com.example.demo.CustomSerializerDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//@Configuration
@EnableIntegration
@EnableScheduling
public class InformationServerConfig22 {

    private final Logger log = LogManager.getLogger(InformationServerConfig22.class);

    @Value("${informationServer.port}")
    private int serverPort;

    @Bean
    public TcpNetServerConnectionFactory serverConnectionFactory() {
        TcpNetServerConnectionFactory connectionFactory = new TcpNetServerConnectionFactory(serverPort);
        connectionFactory.setSerializer(new CustomSerializerDeserializer());
        connectionFactory.setDeserializer(new CustomSerializerDeserializer());
        //connectionFactory.setSoTimeout(5000);
        connectionFactory.setSoKeepAlive(true);
        return connectionFactory;
    }

/*    @Bean
    public TcpInboundGateway tcpInGate(
            @Qualifier(TCP_INPUT_CHANNEL_NAME) MessageChannel tcpInputChannel,
            @Qualifier(TCP_OUTPUT_CHANNEL_NAME) MessageChannel tcpOutputChannel
    ) {
        TcpInboundGateway inGate = new TcpInboundGateway();
        inGate.setConnectionFactory(serverConnectionFactory());
        inGate.setRequestChannel(tcpInputChannel);
        inGate.setReplyChannel(tcpOutputChannel);
        return inGate;
    }*/

/*    @Bean
    public TcpOutboundGateway tcpOutGate(
            @Qualifier(TCP_INPUT_CHANNEL_NAME) MessageChannel tcpInputChannel,
            @Qualifier(TCP_OUTPUT_CHANNEL_NAME) MessageChannel tcpOutputChannel
    ) {
        TcpOutboundGateway outGate = new TcpOutboundGateway();
        outGate.setConnectionFactory(serverConnectionFactory());
        outGate.setReplyChannel(tcpOutputChannel);
        return outGate;
    }*/

    @Bean
    @ServiceActivator(inputChannel = TCP_OUTPUT_CHANNEL_NAME)
    public TcpSendingMessageHandler senderAdapter() {
        TcpSendingMessageHandler adapter = new TcpSendingMessageHandler();
        adapter.setConnectionFactory(serverConnectionFactory());
        return adapter;
    }

    @Bean
    public TcpReceivingChannelAdapter receiverAdapter(@Qualifier(TCP_INPUT_CHANNEL_NAME) MessageChannel tcpInputChannel) {
        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
        adapter.setConnectionFactory(serverConnectionFactory());
        adapter.setOutputChannel(tcpInputChannel);
        return adapter;
    }


/*    @Bean
    public IntegrationFlow informationServerFlow(
            TcpInboundGateway tcpInGate,
            InformationServer informationServer) {
        return IntegrationFlows
                .from(tcpInGate)
                .handle(informationServer::processQuery)
                .get();
    }*/
/*
    @Bean
    public IntegrationFlow informationServerFlow(
            TcpNetServerConnectionFactory serverConnectionFactory,
            InformationServer informationServer) {
        return IntegrationFlows
                .from(Tcp.inboundGateway(serverConnectionFactory))
                .handle(informationServer::processQuery)
                .get();
    }*/

/*    @Bean
    public ProcessMessage informationServer() {
        return new ProcessMessage();
    }*/

    @Bean
    @Qualifier("tcpConnections")
    public Set<String> tcpConnections() {
        return Collections.synchronizedSet(new HashSet<>());
    }

/*    @Bean
    public TcpConnectionApplicationListener tcpConnectionApplicationListener() {
        return new TcpConnectionApplicationListener(tcpConnections());
    }*/

/*    @Bean
    @ServiceActivator(inputChannel = TCP_OUTPUT_CHANNEL_NAME)
    public TcpSendingMessageHandler senderAdapter(
            TcpNetServerConnectionFactory serverConnectionFactory
    ) {
        TcpSendingMessageHandler adapter = new TcpSendingMessageHandler();
        adapter.setConnectionFactory(serverConnectionFactory);
        return adapter;
    }*/

/*    @Bean
    public TcpReceivingChannelAdapter receiverAdapter(
            TcpNetServerConnectionFactory serverConnectionFactory,
            @Qualifier(TCP_INPUT_CHANNEL_NAME) MessageChannel tcpInputChannel) {
        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
        adapter.setConnectionFactory(serverConnectionFactory);
        adapter.setOutputChannel(tcpInputChannel);
        return adapter;
    }*/

    @Bean
    //@DependsOn("client")
    public Runner runner(
            @Qualifier("tcpConnections") Set<String> connections,
            @Qualifier("tcpOutputChannel") MessageChannel outputChannel
    ) {
        return new Runner(connections, outputChannel);
    }

    public static class Runner {

        private final Set<String> connections;
        private final MessageChannel outputChannel;

        public Runner(
                Set<String> connections,
                MessageChannel outputChannel
        ) {
            this.connections = connections;
            this.outputChannel = outputChannel;
        }

        @Scheduled(fixedDelay = 5000)
        public void run() {

            for (String connection : connections) {
                Message<String> message = MessageBuilder.withPayload("Hello message! (using channels)")
                                                        .setHeader(IpHeaders.CONNECTION_ID, connection)
                                                        .build();
                //outputChannel.send(message);
            }
        }

    }


/*    @Bean
    public ApplicationListener<TcpConnectionOpenEvent> openEventlistener() {
        return new ApplicationListener<TcpConnectionOpenEvent>() {

            @Override
            public void onApplicationEvent(TcpConnectionOpenEvent event) {
                log.info("OPEN CONNECTION_ID: {}", event.getConnectionId());
            }

        };
    }
*/
/*    @Bean
    public ApplicationListener<TcpConnectionCloseEvent> closeEventlistener() {
        return new ApplicationListener<TcpConnectionCloseEvent>() {

            @Override
            public void onApplicationEvent(TcpConnectionCloseEvent event) {
                log.info("CLOSE CONNECTION_ID: {}", event.getConnectionId());
            }

        };
    }*/
/*
    @Bean
    public ApplicationListener<TcpConnectionEvent> eventlistener(
            @Qualifier("tcpConnections") Set<String> connections
    ) {
        return new ApplicationListener<TcpConnectionEvent>() {

            @Override
            public void onApplicationEvent(TcpConnectionEvent event) {
                if (event instanceof TcpConnectionOpenEvent) {
                    log.info("Got TCP Open connection event with id={}", event.getConnectionId());
                    connections.add(event.getConnectionId());
                } else if (event instanceof TcpConnectionCloseEvent) {
                    log.info("Got TCP Close connection event with id={}", event.getConnectionId());
                    connections.remove(event.getConnectionId());
                }
            }
        };
    }
*/

}
