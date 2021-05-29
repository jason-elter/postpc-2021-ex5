package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;

public class TodoItemsDataBaseImpl implements TodoItemsDataBase {
    private final LinkedList<TodoItem> inProgressItems, doneItems;
    private final SharedPreferences sp;

    private final MutableLiveData<ArrayList<TodoItem>> itemsLiveData;

    public TodoItemsDataBaseImpl(Context context) {
        inProgressItems = new LinkedList<>();
        doneItems = new LinkedList<>();

        if (context != null) {
            sp = context.getSharedPreferences("local_db_todo_list", Context.MODE_PRIVATE);
            loadFromSP();
        } else {
            sp = null;
        }

        itemsLiveData = new MutableLiveData<>();
        itemsLiveData.setValue(getCurrentItems());
    }

    private void loadFromSP() {
        Collection<?> values = sp.getAll().values();
        for (Object obj : values) {
            String serialized = (String) obj;
            TodoItem newItem = TodoItem.fromSerialized(serialized);
            if (newItem != null) {
                if (newItem.isDone()) {
                    doneItems.addFirst(newItem);
                } else {
                    addOldInProgressItem(newItem);
                }
            }
        }
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
        TodoItem newItem = new TodoItem(description, TodoItem.IN_PROGRESS);
        inProgressItems.addFirst(newItem);

        saveChangesToFile(newItem);
        itemsLiveData.setValue(getCurrentItems());
    }

    // Warning! doesn't save item to SP file.
    private void addOldInProgressItem(TodoItem newItem) {
        // Add to correct place in list.
        ListIterator<TodoItem> it = inProgressItems.listIterator();
        while (it.hasNext()) {
            TodoItem current = it.next();
            if (current.compareTo(newItem) < 0) {
                it.previous();
                break;
            }
        }
        it.add(newItem);
    }

    @Override
    public void markItemDone(TodoItem item) {
        if (!item.isDone()) {
            item.setProgress(TodoItem.DONE);
            inProgressItems.remove(item);
            doneItems.addFirst(item);

            saveChangesToFile(item);
            itemsLiveData.setValue(getCurrentItems());
        }
    }

    @Override
    public void markItemInProgress(TodoItem item) {
        if (item.isDone()) {
            item.setProgress(TodoItem.IN_PROGRESS);
            doneItems.remove(item);

            addOldInProgressItem(item);
            saveChangesToFile(item);
            itemsLiveData.setValue(getCurrentItems());
        }
    }

    @Override
    public void deleteItem(TodoItem item) {
        if (item.isDone()) {
            doneItems.remove(item);
        } else {
            inProgressItems.remove(item);
        }

        // Remove item from file.
        if (sp != null) {
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.remove(item.getId());
            spEditor.apply();
        }

        itemsLiveData.setValue(getCurrentItems());
    }

    @Override
    public void changeItemDescription(TodoItem item, String description) {
        item.setDescription(description);
        itemsLiveData.setValue(getCurrentItems());
    }

    @Override
    public TodoItem getEqualItem(TodoItem item) {
        LinkedList<TodoItem> toIterate = item.isDone() ? doneItems : inProgressItems;
        for (TodoItem current : toIterate) {
            if (current.equals(item)) {
                return current;
            }
        }

        return null;
    }

    @Override
    public LiveData<ArrayList<TodoItem>> getLiveData() {
        return itemsLiveData;
    }

    private void saveChangesToFile(TodoItem item) {
        if (sp != null) {
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.putString(item.getId(), item.serialize());
            spEditor.apply();
        }
    }
}
