package com.example.demo.experiment;

import static com.example.demo.experiment.MessageChannelConfiguration.TCP_INPUT_CHANNEL_NAME;

import com.example.demo.service.ProcessMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MessagingController {

    private final Logger log = LogManager.getLogger(MessagingController.class);

    private final Set<String> connections;
    private final MessageChannel outputChannel;
    ProcessMessageService processMessageService;

    public MessagingController(
            @Qualifier("tcpConnections") Set<String> connections,
            @Qualifier("tcpOutputChannel") MessageChannel outputChannel,
            ProcessMessageService processMessageService
    ) {
        this.connections = connections;
        this.outputChannel = outputChannel;
        this.processMessageService = processMessageService;
    }

    @StreamListener(TCP_INPUT_CHANNEL_NAME)
    public void messageListener(Message<byte[]> message) {
        log.info("Got message via controller:" + message.getPayload());
        String cid = (String) message.getHeaders().get(IpHeaders.CONNECTION_ID);
        log.info("MessageHeaders - CONNECTION_ID: {}", cid);

        Message<String> resp = processMessageService.processQuery(message.getPayload(), message.getHeaders());
        outputChannel.send(resp);
        /*for (String connection : connections) {
            Message<String> resp = MessageBuilder.withPayload("Hello message! (using channels)")
                                                    .setHeader(IpHeaders.CONNECTION_ID, connection)
                                                    .build();
            outputChannel.send(resp);
        }*/
    }
}
