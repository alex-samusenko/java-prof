package ru.otus.entity;

public enum Banknote {
    CHERVONETS(10),
    STOLNIK(100),
    PYATIKHATKA(500),
    KOSAR(1000),
    PYAT_KOSAREY(5000);
    private final int nominal;

    Banknote(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }
}
