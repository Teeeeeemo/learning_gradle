package me.khrystal.gradle.todo;

/**
 * Created by kHRYSTAL on 18/3/12.
 */
public class TodoItem {
    private String name;
    private boolean hasDone;

    public TodoItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isHasDone() {
        return hasDone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasDone(boolean hasDone) {
        this.hasDone = hasDone;
    }

    @Override
    public String toString() {
        return name + (hasDone ? " hasDone" : " need to do") + "!";
    }
}
