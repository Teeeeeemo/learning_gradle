package me.khrystal.gradle.todo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kHRYSTAL on 18/3/12.
 */
public class TodoRepository {
    private static Map<String, TodoItem> items = new HashMap<>();

    public void save(TodoItem item) {
        System.out.println("save:" + item);
        items.put(item.getName(), item);
    }

    public TodoItem get(String name) {
        TodoItem item = items.get(name);
            System.out.println("get:" + name);
            return item;
    }
}