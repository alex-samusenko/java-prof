package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class ProcessorExceptionGenerator implements Processor {
    public final TimeProvider provider;
    public ProcessorExceptionGenerator(TimeProvider provider) {
        this.provider = provider;
    }

    public Message process(Message message){
        LocalDateTime dateTime = provider.get();

        if(dateTime.getSecond()%2 == 0)
            throw new RuntimeException(String.format("Even second: %s", dateTime));

        return message;
    }
}
