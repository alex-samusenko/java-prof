package ru.otus;

import ru.otus.annotation.*;
public class TestClass {

    @Before
    void before_1() {
        System.out.println("\tbefore_1 done");
    }
    @Before
    void before_2() {
        System.out.println("\tbefore_2 done");
    }
    @Test
    void test_1() {
        System.out.print("\ttest_1 ");
    }
    @Test
    void test_2() {
        System.out.print("\ttest_2 ");
        throw new RuntimeException();
    }
    @After
    void after_1() {
        System.out.println("\tafter_1 done");
    }

    @After
    void after_2() {
        System.out.println("\tafter_2 done");
    }
    @Override
    public String toString() {
        return "TestClass{}";
    }
}