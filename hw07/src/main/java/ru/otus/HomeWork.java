package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.*;
import ru.otus.processor.*;
import ru.otus.processor.homework.ProcessorExceptionGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(),new ProcessorExceptionGenerator(() -> LocalDateTime.now()));
        var complexProcessor = new ComplexProcessor(processors, ex -> {System.out.println("Exception:"+ ex);});
        var historyListener = new HistoryListener();

        complexProcessor.addListener(historyListener);

        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add("field8");
        field13Data.add("field9");
        field13Data.add("field11");
        field13Data.add("field12");
        field13Data.add("field13");
        field13.setData(field13Data);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field4("field4")
                .field5("field5")
                .field6("field6")
                .field7("field7")
                .field10("field10")
                .field13(field13)
                .build();
        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(historyListener);
        System.out.println("message.field13: " + message.getField13().getData());
        message.getField13().setData(new ArrayList<>());
        System.out.println("message.field13: " + message.getField13().getData());

        var messageFromHistory = historyListener.findMessageById(1L);
        messageFromHistory.ifPresent(value -> System.out.println("messageFromHistory:" + value.getField13().getData()));
    }
}
