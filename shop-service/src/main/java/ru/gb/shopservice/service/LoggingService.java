package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LoggingService {

    private final FileGatewayService fileGatewayService;

    /*
     * Метод для логирования сообщений
     */

    public void log(String message) {
        fileGatewayService.writeToFile(
                "log.txt",
                "Shop-service (" + LocalDateTime.now() + "): " + message);
    }

}
