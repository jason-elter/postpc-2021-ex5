package exercise.android.reemh.todo_items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

    private ActivityController<MainActivity> activityController;
    private TodoItemsDataBase mockDataBase;

    @Before
    public void setup() {
        mockDataBase = Mockito.mock(TodoItemsDataBase.class);
        // when asking the `mockDataBase` to get the current items, return an empty list
        Mockito.when(mockDataBase.getCurrentItems())
                .thenReturn(new ArrayList<>());

        activityController = Robolectric.buildActivity(MainActivity.class);

        // let the activity use our `mockDataBase` as the TodoItemsDataBase
        MainActivity activityUnderTest = activityController.get();
        activityUnderTest.dataBase = mockDataBase;
    }

    @Test
    public void when_activityIsLaunched_then_theEditTextStartsEmpty() {
        // setup
        activityController.create().visible();
        MainActivity activityUnderTest = activityController.get();
        EditText editText = activityUnderTest.findViewById(R.id.editTextInsertTask);
        String userInput = editText.getText().toString();
        // verify
        assertTrue(userInput.isEmpty());
    }

    @Test
    public void when_userPutInputAndClicksButton_then_activityShouldCallAddItem() {
        // setup
        String userInput = "Call my grandma today at 18:00";
        activityController.create().visible(); // let the activity think it is being shown
        MainActivity activityUnderTest = activityController.get();
        EditText editText = activityUnderTest.findViewById(R.id.editTextInsertTask);
        View fab = activityUnderTest.findViewById(R.id.buttonCreateTodoItem);

        // test - mock user interactions
        editText.setText(userInput);
        fab.performClick();

        // verify: verify that `mockDataBase.addNewInProgressItem()` was called, with exactly same string
        Mockito.verify(mockDataBase).addNewInProgressItem(eq(userInput));
    }

    @Test
    public void when_userPutInputAndClicksButton_then_inputShouldBeErasedFromEditText() {
        // setup
        String userInput = "Call my grandma today at 18:00";
        activityController.create().visible(); // let the activity think it is being shown
        MainActivity activityUnderTest = activityController.get();
        EditText editText = activityUnderTest.findViewById(R.id.editTextInsertTask);
        View fab = activityUnderTest.findViewById(R.id.buttonCreateTodoItem);

        // test - mock user interactions
        editText.setText(userInput);
        fab.performClick();

        // verify
        String afterText = editText.getText().toString();
        assertTrue(afterText.isEmpty());
    }

    @Test
    public void when_dataBaseSaysNoItems_then_recyclerViewShowsZeroItems() {
        // setup
        Mockito.when(mockDataBase.getCurrentItems())
                .thenReturn(new ArrayList<>());

        // test - let the activity think it is being shown
        activityController.create().visible();

        // verify
        MainActivity activityUnderTest = activityController.get();
        RecyclerView recyclerView = activityUnderTest.findViewById(R.id.recyclerTodoItemsList);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertNotNull(adapter);
        assertEquals(0, adapter.getItemCount());
    }

    @Test
    public void when_dataBaseSays1ItemOfTypeInProgress_then_activityShouldShow1MatchingViewInRecyclerView() {
        // setup

        // when asking the `mockHolder` to get the current items, return a list with 1 item of type "in progress"
        ArrayList<TodoItem> itemsReturnedByHolder = new ArrayList<>();
        Mockito.when(mockDataBase.getCurrentItems())
                .thenReturn(itemsReturnedByHolder);
        TodoItem itemInProgress = new TodoItem("do homework", TodoItem.IN_PROGRESS);
        itemsReturnedByHolder.add(itemInProgress);

        // test - let the activity think it is being shown
        activityController.create().visible();

        // verify: make sure that the activity shows a matching subview in the recycler view
        MainActivity activityUnderTest = activityController.get();
        RecyclerView recyclerView = activityUnderTest.findViewById(R.id.recyclerTodoItemsList);

        // 1. verify that adapter says there should be 1 item showing
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertNotNull(adapter);
        assertEquals(1, adapter.getItemCount());

        // 2. verify that the shown view has a checkbox being not-checked and has a TextView showing the correct description
        View viewInRecycler = recyclerView.findViewHolderForAdapterPosition(0).itemView;
        TextView textView = viewInRecycler.findViewById(R.id.descriptionText);
        CheckBox progressCheckbox = viewInRecycler.findViewById(R.id.progressCheckbox);

        Assert.assertEquals(textView.getText().toString(), "do homework");
        Assert.assertFalse(progressCheckbox.isChecked());
    }


    @Test
    public void when_dataBaseSays1ItemOfTypeDone_then_activityShouldShow1MatchingViewInRecyclerView() {
        // setup

        // when asking the `mockHolder` to get the current items, return a list with 1 item of type "DONE"
        ArrayList<TodoItem> itemsReturnedByHolder = new ArrayList<>();
        Mockito.when(mockDataBase.getCurrentItems())
                .thenReturn(itemsReturnedByHolder);
        TodoItem itemDone = new TodoItem("buy tomatoes", TodoItem.DONE);
        itemsReturnedByHolder.add(itemDone);

        // test - let the activity think it is being shown
        activityController.create().visible();

        // verify: make sure that the activity shows a matching subview in the recycler view
        MainActivity activityUnderTest = activityController.get();
        RecyclerView recyclerView = activityUnderTest.findViewById(R.id.recyclerTodoItemsList);

        // 1. verify that adapter says there should be 1 item showing
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertNotNull(adapter);
        assertEquals(1, adapter.getItemCount());

        // 2. verify that the shown view has a checkbox being checked and has a TextView showing the correct description
        View viewInRecycler = recyclerView.findViewHolderForAdapterPosition(0).itemView;
        TextView textView = viewInRecycler.findViewById(R.id.descriptionText);
        CheckBox progressCheckbox = viewInRecycler.findViewById(R.id.progressCheckbox);

        Assert.assertEquals(textView.getText().toString(), "buy tomatoes");
        Assert.assertTrue(progressCheckbox.isChecked());
    }
}
