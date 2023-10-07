package ru.otus;

import ru.otus.entity.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Atm implements AtmInterface {
    private final List<Cell> cells;
    public Atm(List<Cell> cells) {
        this.cells = cells;
    }
    @Override
    public void deposit(BanknoteStack stack) {
        if (cells == null || cells.isEmpty()) {
            throw new AtmException("В ATM нет ячеек для приема банкнот");
        }
        stack.getContent().forEach((k, v) ->
                getCellByBanknote(k, cells).put(v));
    }
    @Override
    public BanknoteStack withdraw(Long cash) {
        if (balance() < cash) {
            throw new AtmException("недостаточно средств");
        }
        else {
            BanknoteStack stack = new BanknoteStack();
            Map<Banknote, Long> map = stack.getContent();
            cells.sort(Comparator.comparingInt(o -> o.getBanknoteType().getNominal()));
            Collections.reverse(cells);
            for (Cell cell : cells) {
                long x = cash / cell.getBanknoteType().getNominal();
                if (x > 0) {
                    if (cell.getBanknoteQty() >= x) {
                        cash -= (cell.getBanknoteType().getNominal() * x);
                        map.put(cell.getBanknoteType(), x);
                        cell.get(x);
                    }
                    else {
                        cash -= cell.getBanknoteType().getNominal() * cell.getBanknoteQty();
                        map.put(cell.getBanknoteType(), cell.getBanknoteQty());
                        cell.get(cell.getBanknoteQty());
                    }
                }
                if (cash == 0) {
                    break;
                }
            }
            if (cash != 0) {
                throw new AtmException("Не удалось снять деньги");
            }
            return stack;
        }
    }
    @Override
    public Long balance() {
        return cells.stream().mapToLong(Cell::getAvailableCash).sum();
    }
    private Cell getCellByBanknote(Banknote banknote, List<Cell> calls) {
        return calls.stream().filter(el -> el.getBanknoteType().equals(banknote))
                .findFirst().orElseThrow(() -> new AtmException("В ATM нет ячееки для приема этого наминала банкнот: "
                        + banknote.toString()));
    }
    @Override
    public String toString() {
        return "Atm{" + "balance=" + balance() + '}';
    }
}
