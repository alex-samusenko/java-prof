package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcessorExceptionGeneratorTest {
    @Test
    @DisplayName("Отсутствие исключения в нечетную секунду")
    void oddSecTest(){
        var processor = new ProcessorExceptionGenerator(() -> LocalDateTime.of(2023, 7, 19, 19, 50, 23));
        var msg = new Message.Builder(1L).build();
        assertDoesNotThrow(() -> processor.process(msg));
    }

    @Test
    @DisplayName("Вызов исключения в четную секунду")
    void EvenSecTest(){
        var processor = new ProcessorExceptionGenerator(() ->
                LocalDateTime.of(2023, 7, 19, 19, 50, 50));
        var msg = new Message.Builder(1L).build();
        assertThrows(RuntimeException.class,
                () -> processor.process(msg));
    }
}
