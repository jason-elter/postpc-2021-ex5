package exercise.android.reemh.todo_items;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public TodoItemsDataBase dataBase = null;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (dataBase == null) {
            dataBase = TodoItemsApplication.getInstance().getDatabase();
        }

        // Find all UI components.
        FloatingActionButton createButton = findViewById(R.id.buttonCreateTodoItem);
        editText = findViewById(R.id.editTextInsertTask);
        RecyclerView recyclerView = findViewById(R.id.recyclerTodoItemsList);

        // Set up Recycler View
        TodoItemAdapter adapter = new TodoItemAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Set up listeners for UI elements
        createButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (text.isEmpty()) {
                return;
            }

            dataBase.addNewInProgressItem(text);
            editText.setText("");
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edit_text", editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editText.setText(savedInstanceState.getString("edit_text"));
    }
}
