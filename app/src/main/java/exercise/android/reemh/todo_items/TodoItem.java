package exercise.android.reemh.todo_items;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class TodoItem implements Serializable {
    public static final boolean IN_PROGRESS = false, DONE = true;

    private static int counter = 0;

    private final int id;
    private final String description;
    private boolean isDone;

    public TodoItem(String description, boolean isDone) {
        this.id = counter++;
        this.description = description;
        this.isDone = isDone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return id == todoItem.id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setProgress(boolean isDone) {
        this.isDone = isDone;
    }
}
