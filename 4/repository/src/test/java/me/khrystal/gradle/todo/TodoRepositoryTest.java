package me.khrystal.gradle.todo;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kHRYSTAL on 18/3/12.
 */
public class TodoRepositoryTest {
    private TodoRepository repository = new TodoRepository();

    /**
     * 测试保存方法
     */
    @Test
    public void testSave() {
        TodoItem item = new TodoItem("test");
        repository.save(item);
        Assert.assertNotNull(repository.get("test"));
    }
}
