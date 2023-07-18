package ru.otus.entity;

public class Cell {
    private final Banknote banknote;
    private long count;

    public Cell(Banknote banknote) {
        this.banknote = banknote;
        this.count = 0L;
    }

    public Cell(Banknote banknote, Long qty) {
        this.banknote = banknote;
        this.count = qty;
    }

    public void put(Long qty) {
        count += qty;
    }

    public void get(Long qty) {
        if (count < qty) {
            throw new AtmException("недостаточно банкнот");
        }
        else {
            count -= qty;
        }
    }

    public Banknote getBanknoteType() {
        return banknote;
    }

    public Long getBanknoteQty() {
        return count;
    }

    public Long getAvailableCash() {
        return banknote.getNominal() * count;
    }
}
