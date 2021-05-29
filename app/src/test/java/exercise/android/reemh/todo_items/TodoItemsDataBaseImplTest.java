package exercise.android.reemh.todo_items;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TodoItemsDataBaseImplTest {

    private TodoItem getItemFromDescription(TodoItemsDataBaseImpl db, String description) {
        for (TodoItem item : db.getCurrentItems()) {
            if (item.getDescription().equals(description)) {
                return item;
            }
        }
        return null;
    }

    private boolean databaseContainsDescription(TodoItemsDataBaseImpl db, String description) {
        return getItemFromDescription(db, description) != null;
    }

    private boolean compareLists(TodoItemsDataBaseImpl db, List<String> descriptions) {
        List<TodoItem> items = db.getCurrentItems();
        int length = items.size();
        if (length != descriptions.size()) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (items.get(i).getDescription().equals(descriptions.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void when_addingTodoItem_then_callingListShouldHaveThisItem() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("do shopping");

        // verify
        Assert.assertEquals(1, dbUnderTest.getCurrentItems().size());
    }

    @Test
    public void when_addingInProgressTodoItem_then_callingListShouldHaveThisItemInProgress() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("do shopping");

        // verify
        Assert.assertEquals(1, dbUnderTest.getCurrentItems().size());
        Assert.assertTrue(databaseContainsDescription(dbUnderTest, "do shopping"));
    }

    @Test
    public void when_markingItemDone_then_callingListShouldHaveThisItemDone() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("do shopping");
        TodoItem item = getItemFromDescription(dbUnderTest, "do shopping");
        Assert.assertNotNull(item);
        dbUnderTest.markItemDone(item);

        // verify
        TodoItem item2 = getItemFromDescription(dbUnderTest, "do shopping");
        Assert.assertNotNull(item2);
        Assert.assertEquals(TodoItem.DONE, item2.isDone());
    }

    @Test
    public void when_markingItemDoneAndThenInProgress_then_callingListShouldHaveThisItemInProgress() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("do shopping");
        TodoItem item = getItemFromDescription(dbUnderTest, "do shopping");
        Assert.assertNotNull(item);
        dbUnderTest.markItemDone(item);
        dbUnderTest.markItemInProgress(item);

        // verify
        TodoItem item2 = getItemFromDescription(dbUnderTest, "do shopping");
        Assert.assertNotNull(item2);
        Assert.assertEquals(TodoItem.IN_PROGRESS, item2.isDone());
    }

    @Test
    public void when_addingTodoItemAndThenDeletingIt_then_callingListShouldNotHaveThisItem() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("do shopping");
        TodoItem item = getItemFromDescription(dbUnderTest, "do shopping");
        Assert.assertNotNull(item);
        dbUnderTest.deleteItem(item);

        // verify
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());
    }

    @Test
    public void when_addingInProgressTodoItems_then_callingListShouldBeInCreationOrder() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("third");
        dbUnderTest.addNewInProgressItem("second");
        dbUnderTest.addNewInProgressItem("first");

        // verify
        Assert.assertTrue(compareLists(dbUnderTest, Arrays.asList("first", "second", "third")));
    }

    @Test
    public void when_addingTodoItems_then_callingListShouldHaveDoneItemsAtEnd() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("second");
        dbUnderTest.addNewInProgressItem("done");
        dbUnderTest.addNewInProgressItem("first");
        TodoItem item = getItemFromDescription(dbUnderTest, "done");
        Assert.assertNotNull(item);
        dbUnderTest.markItemDone(item);

        // verify
        Assert.assertTrue(compareLists(dbUnderTest, Arrays.asList("first", "second", "done")));
    }

    @Test
    public void when_markingItemDoneAndThenInProgress_then_callingListShouldHaveCorrectOrder() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("second");
        dbUnderTest.addNewInProgressItem("done");
        dbUnderTest.addNewInProgressItem("first");
        TodoItem item = getItemFromDescription(dbUnderTest, "done");
        Assert.assertNotNull(item);
        dbUnderTest.markItemDone(item);
        dbUnderTest.markItemInProgress(item);

        // verify
        Assert.assertTrue(compareLists(dbUnderTest, Arrays.asList("first", "done", "second")));
    }

    @Test
    public void when_deletingDoneItem_then_callingListShouldHaveCorrectOrder() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("second");
        dbUnderTest.addNewInProgressItem("done");
        dbUnderTest.addNewInProgressItem("first");
        TodoItem item = getItemFromDescription(dbUnderTest, "done");
        Assert.assertNotNull(item);
        dbUnderTest.markItemDone(item);
        dbUnderTest.deleteItem(item);

        // verify
        Assert.assertTrue(compareLists(dbUnderTest, Arrays.asList("first", "second")));
    }

    @Test
    public void when_deletingInProgressItem_then_callingListShouldHaveCorrectOrder() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("second");
        dbUnderTest.addNewInProgressItem("done");
        dbUnderTest.addNewInProgressItem("first");
        TodoItem item = getItemFromDescription(dbUnderTest, "first");
        Assert.assertNotNull(item);
        dbUnderTest.deleteItem(item);

        // verify
        Assert.assertTrue(compareLists(dbUnderTest, Arrays.asList("done", "second")));
    }

    @Test
    public void when_markingAllItemsDone_then_callingListShouldHaveCorrectOrder() {
        // setup
        TodoItemsDataBaseImpl dbUnderTest = new TodoItemsDataBaseImpl(null);
        Assert.assertEquals(0, dbUnderTest.getCurrentItems().size());

        // test
        dbUnderTest.addNewInProgressItem("third");
        dbUnderTest.addNewInProgressItem("second");
        dbUnderTest.addNewInProgressItem("first");

        TodoItem item1 = getItemFromDescription(dbUnderTest, "third");
        TodoItem item2 = getItemFromDescription(dbUnderTest, "second");
        TodoItem item3 = getItemFromDescription(dbUnderTest, "first");

        Assert.assertNotNull(item1);
        Assert.assertNotNull(item2);
        Assert.assertNotNull(item3);

        dbUnderTest.markItemDone(item1);
        dbUnderTest.markItemDone(item2);
        dbUnderTest.markItemDone(item3);

        // verify
        Assert.assertTrue(compareLists(dbUnderTest, Arrays.asList("first", "second", "third")));
    }
}