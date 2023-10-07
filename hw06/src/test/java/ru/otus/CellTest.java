package ru.otus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.entity.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTest {
    private Cell cell;

    @BeforeEach
    void beforeEach() {}

    @DisplayName("Добавления банкнот в ячейку")
    @Test
    void addBanknote() {
        cell = new Cell(Banknote.PYAT_KOSAREY);
        cell.put(100L);
        assertThat(cell.getBanknoteQty()).isEqualTo(100L);
    }

    @DisplayName("Изьятие банктнот из ячейки (есть в наличии)")
    @Test
    void takeBanknote1() {
        cell = new Cell(Banknote.KOSAR);
        cell.put(10L);
        cell.get(3L);
        assertThat(cell.getBanknoteQty()).isEqualTo(7L);
    }

    @DisplayName("Изьятие банктнот из ячейки (больше наличия)")
    @Test
    void takeBanknote2() {
        cell = new Cell(Banknote.KOSAR);
        cell.put(10L);
        assertThrows(AtmException.class, () -> cell.get(13L));
    }

    @DisplayName("Получение банкноты нужнного номинала")
    @Test
    void getBanknoteType() {
        Cell cell1 = new Cell(Banknote.KOSAR);
        Cell cell2 = new Cell(Banknote.PYAT_KOSAREY);
        assertThat(cell1.getBanknoteType()).isEqualTo(Banknote.KOSAR);
        assertThat(cell2.getBanknoteType()).isEqualTo(Banknote.PYAT_KOSAREY);
    }
    @AfterEach
    void AfterEach() {}
}
