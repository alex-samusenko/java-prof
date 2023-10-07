package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.entity.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtmTest {
    static final List<Cell> emptyCells = Arrays.asList(
            new Cell(Banknote.PYAT_KOSAREY, 0L),
            new Cell(Banknote.KOSAR, 0L),
            new Cell(Banknote.PYATIKHATKA, 0L),
            new Cell(Banknote.STOLNIK, 0L)
    );
    static final List<Cell> chargedCells = Arrays.asList(
            new Cell(Banknote.PYAT_KOSAREY, 10L),
            new Cell(Banknote.KOSAR, 15L),
            new Cell(Banknote.PYATIKHATKA, 20L),
            new Cell(Banknote.STOLNIK, 25L)
    );
    @DisplayName("Проверка остатка")
    @Test
    void balance() {
        Atm atm = new Atm(chargedCells);
        assertThat(atm.balance()).isEqualTo(72000L);
    }
    @DisplayName("Внесение наличных (АТМ содержит нужные ячейки)")
    @Test
    void depositCellsExists() {
        var atm = new Atm(emptyCells);
        var stack = new BanknoteStack();
        stack.setContent(Collections.singletonMap(Banknote.KOSAR, 1L));
        atm.deposit(stack);
        assertThat(atm.balance()).isEqualTo(1000L);
    }
    @DisplayName("Внесение наличных (АТМ содержит нужные ячейки)")
    @Test
    void depositCellsNotExists() {
        var atm = new Atm(Collections.emptyList());
        var stack = new BanknoteStack();
        stack.setContent(Collections.singletonMap(Banknote.PYATIKHATKA, 1L));
        assertThrows(AtmException.class, () -> atm.deposit(stack));
    }
    @DisplayName("Внесение наличных (АТМ не содержит ячеек)")
    @Test
    void depositNoCells() {
        var atm = new Atm(null);
        var stack = new BanknoteStack();
        stack.setContent(Collections.singletonMap(Banknote.PYATIKHATKA, 1L));
        assertThrows(AtmException.class, () -> atm.deposit(stack));
    }
    @DisplayName("Внесение наличных (АТМ не содержит нужные ячейки)")
    @Test
    void depositNoNeededCell() {
        var atm = new Atm(emptyCells);
        var stack = new BanknoteStack();
        stack.setContent(Collections.singletonMap(Banknote.CHERVONETS, 1L));
        assertThrows(AtmException.class, () -> atm.deposit(stack));
    }
    @DisplayName("Снятие наличных (сверх наличия)")
    @Test
    void withdrawOverStock() {
        var atm = new Atm(chargedCells);
        assertThrows(AtmException.class, () -> atm.withdraw(5590L));
    }
    @DisplayName("Снятие наличных (в наличии)")
    @Test
    void withdrawInStock() {
        var atm = new Atm(chargedCells);
        assertThat(atm.withdraw(5500L).getContent())
                .containsEntry(Banknote.PYAT_KOSAREY, 1L)
                .containsEntry(Banknote.PYATIKHATKA, 1L);
    }
}