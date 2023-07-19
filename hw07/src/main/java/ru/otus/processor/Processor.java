package ru.otus.processor;

import ru.otus.model.Message;

public interface Processor {

    Message process(Message message);

}
