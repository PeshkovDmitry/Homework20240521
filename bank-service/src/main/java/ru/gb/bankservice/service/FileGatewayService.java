package ru.gb.bankservice.service;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "textInputChannel")
public interface FileGatewayService {

    void writeToFile(@Header(FileHeaders.FILENAME) String fileName, String data);

}
