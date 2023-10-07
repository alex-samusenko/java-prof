package ru.otus;

import ru.otus.entity.BanknoteStack;

public interface AtmInterface {

    Long balance();

    BanknoteStack withdraw(Long expectedSum);

    void deposit(BanknoteStack stack);
}
