package ru.otus;


import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> map;

    public CustomerService() {
        map = new TreeMap<>(Comparator.comparing(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        var data = map.firstEntry();
        return new AbstractMap.SimpleEntry<>(new Customer(data.getKey()), data.getValue()) ;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var data = map.higherEntry(customer);
        if (data == null)
            return null;
        return new AbstractMap.SimpleEntry<>(new Customer(data.getKey()), data.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
