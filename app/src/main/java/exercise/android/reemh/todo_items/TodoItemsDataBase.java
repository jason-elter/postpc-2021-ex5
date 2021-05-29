package exercise.android.reemh.todo_items;

import androidx.lifecycle.LiveData;

import java.io.Serializable;
import java.util.ArrayList;


public interface TodoItemsDataBase extends Serializable {

    /**
     * Get a copy of the current items list
     */
    ArrayList<TodoItem> getCurrentItems();

    /**
     * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
     * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
     */
    void addNewInProgressItem(String description);

    /**
     * mark the @param item as DONE
     */
    void markItemDone(TodoItem item);

    /**
     * mark the @param item as IN-PROGRESS
     */
    void markItemInProgress(TodoItem item);

    /**
     * delete the @param item
     */
    void deleteItem(TodoItem item);

    /**
     * Change the description of @param item in the list to @param description.
     */
    void changeItemDescription(TodoItem item, String description);

    /**
     * Get an item from the list that is equal to the given one.
     */
    TodoItem getEqualItem(TodoItem item);

    /**
     * Get the LiveData object associated with this database.
     */
    LiveData<ArrayList<TodoItem>> getLiveData();
}
