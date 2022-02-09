package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.integration.ip.tcp.serializer.AbstractByteArraySerializer;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.integration.ip.tcp.serializer.ByteArrayStxEtxSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * A custom serializer for incoming and/or outcoming messages.
 */
public class CustomSerializerDeserializer extends AbstractByteArraySerializer {

    private final Logger log = LogManager.getLogger(this);

    private ByteArrayStxEtxSerializer byteArrayStxEtxSerializer = new ByteArrayStxEtxSerializer();
    private ByteArrayLengthHeaderSerializer byteArrayLengthHeaderSerializer = new ByteArrayLengthHeaderSerializer(2);

    @Override
    public byte[] deserialize(InputStream inputStream) throws IOException {
        byte[] message = byteArrayStxEtxSerializer.deserialize(inputStream);
        if (message != null && message.length > 0) {
            if (message[0] == 1) {
                //Telegramm with small data field
                message = byteArrayLengthHeaderSerializer.deserialize(
                        new ByteArrayInputStream(Arrays.copyOfRange(message, 3, message.length))
                );
            } else if (message[0] == 2) {
                //Telegramm with unlimited data field
                message = Arrays.copyOfRange(message, 1, message.length);
            }
            log.debug("Deserialized message {}", new String(message, StandardCharsets.UTF_8));
        }
        return message;
    }

    @Override
    public void serialize(byte[] message, OutputStream outputStream) throws IOException {
        log.debug("Serializing {}", new String(message, StandardCharsets.UTF_8));
        outputStream.write(ByteArrayStxEtxSerializer.STX);
        if (message.length >= 32767) {
            byteArrayLengthHeaderSerializer.serialize(message, outputStream);
        } else {
            outputStream.write(message);
        }
        outputStream.write(ByteArrayStxEtxSerializer.ETX);
    }

}