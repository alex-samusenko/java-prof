package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSwapper implements Processor {
    public Message process(Message message){
        return message.toBuilder()
                .field11(message.getField12())
                .field12(message.getField11())
                .build();
    }
}
