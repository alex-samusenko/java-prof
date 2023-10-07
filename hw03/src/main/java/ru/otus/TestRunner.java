package ru.otus;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.otus.annotation.*;

public class TestRunner {
    public static void run(String className) throws ClassNotFoundException {
        runTest(getAnnotatedMethods(className), className);
    }
    private static void runTest(Map<Class, Set<Method>> map, String className) throws ClassNotFoundException {
        int testPassed = 0;
        int testFailed = 0;
        int testAmount = 0;

        for (Method testMethod : map.get(Test.class)) {
            testAmount++;
            System.out.println(testMethod.getName() + " started...");
            var instantiate = Reflection.instantiate(Class.forName(className));
            for (Method method : map.get(Before.class)) {
                Reflection.callMethod(instantiate, method.getName());
            }
            try {
                Reflection.callMethod(instantiate, testMethod.getName());
                testPassed++;
                System.out.println("passed");
            } catch (RuntimeException e) {
                System.out.println("failed");
                testFailed++;
            }
            for (Method method : map.get(After.class)) {
                Reflection.callMethod(instantiate, method.getName());
            }
            System.out.println("done.");
        }
        System.out.println("Всего тестов: " + testAmount);
        System.out.println("\tпройдено успешно: " + testPassed);
        System.out.println("\tне пройдено: " + testFailed);
    }
    private static Map<Class, Set<Method>> getAnnotatedMethods(String className) throws ClassNotFoundException {
        var methods = Class.forName(className).getDeclaredMethods();

        Set<Method> beforeSet = new HashSet<>();
        Set<Method> afterSet = new HashSet<>();
        Set<Method> testSet = new HashSet<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeSet.add(method);
            }
            else if (method.isAnnotationPresent(Test.class)) {
                testSet.add(method);
            }
            else if (method.isAnnotationPresent(After.class)) {
                afterSet.add(method);
            }
        }
        Map<Class, Set<Method>> map = new HashMap<>();

        map.put(Test.class, testSet);
        map.put(After.class, afterSet);
        map.put(Before.class, beforeSet);
        return map;
    }
}
