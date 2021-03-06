package net.nulearn4j.core.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jiachiliu on 10/17/14.
 */
public class Row<T> {

    private List<T> data;

    public Row() {
        data = new ArrayList<>();
    }

    public Row(List<T> data) {
        this.data = data;
    }

    public Row(Row<T> row) {
        data = new ArrayList<>();
        for (int i = 0; i < row.size(); i++)
            data.add(row.get(i));
    }

    public Row(T[] data) {
        this.data = Stream.of(data).collect(Collectors.toList());
    }

    public T get(int i) {
        return data.get(i);
    }

    public void set(int i, T val) {
        data.set(i, val);
    }

    public int size() {
        return data.size();
    }

    public Stream<T> stream() {
        return data.stream();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void add(T val) {
        data.add(val);
    }

    public String toString() {
        return data.toString();
    }
}
