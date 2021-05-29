package exercise.android.reemh.todo_items;

import android.app.Application;

public class TodoItemsApplication extends Application {
    private static TodoItemsApplication instance;
    private TodoItemsDataBase database;

    public static TodoItemsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = new TodoItemsDataBaseImpl(this);
        instance = this;
    }

    public TodoItemsDataBase getDatabase() {
        return database;
    }
}
