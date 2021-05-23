package exercise.android.reemh.todo_items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class TodoItemsDataBaseImpl implements TodoItemsDataBase {
    private final LinkedList<TodoItem> inProgressItems, doneItems;

    public TodoItemsDataBaseImpl() {
        inProgressItems = new LinkedList<>();
        doneItems = new LinkedList<>();
    }

    @Override
    public ArrayList<TodoItem> getCurrentItems() {
        ArrayList<TodoItem> newList = new ArrayList<>(inProgressItems.size() + doneItems.size());
        newList.addAll(inProgressItems);
        newList.addAll(doneItems);
        return newList;
    }

    @Override
    public void addNewInProgressItem(String description) {
        inProgressItems.addFirst(new TodoItem(description, TodoItem.IN_PROGRESS));
    }

    @Override
    public void markItemDone(TodoItem item) {
        if (!item.isDone()) {
            item.setProgress(TodoItem.DONE);
            inProgressItems.remove(item);
            doneItems.addFirst(item);
        }
    }

    @Override
    public void markItemInProgress(TodoItem item) {
        if (item.isDone()) {
            item.setProgress(TodoItem.IN_PROGRESS);
            doneItems.remove(item);

            // Add to correct place in list.
            ListIterator<TodoItem> it = inProgressItems.listIterator();
            while (it.hasNext()) {
                TodoItem current = it.next();
                if (current.getId() < item.getId()) {
                    it.previous();
                    break;
                }
            }
            it.add(item);
        }
    }

    @Override
    public void deleteItem(TodoItem item) {
        if (item.isDone()) {
            doneItems.remove(item);
        } else {
            inProgressItems.remove(item);
        }
    }
}
