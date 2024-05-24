package ru.gb.shopservice.service.rest;

import lombok.AllArgsConstructor;
import ru.gb.shopservice.service.LoggingService;

@AllArgsConstructor
public abstract class AbstractService {

    private final LoggingService loggingService;

    private final String LOGGING_MESSAGE;

    private final String ERROR_MESSAGE;

    public void process(long id, int count) {
        try {
            log(String.format(LOGGING_MESSAGE, id, count));
            work(id, count);
        } catch (Exception e) {
            rollback(id, count);
            generateExeption(ERROR_MESSAGE);
        }
    }

    private void log(String message) {
        loggingService.log(message);
    }

    private void generateExeption(String message) {
        throw new RuntimeException(message);
    }

    abstract void work(long id, int count);

    abstract void rollback(long id, int count);

}
