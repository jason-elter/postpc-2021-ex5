package exercise.android.reemh.todo_items;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    public TodoItemsDataBase dataBase = null;
    private TodoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (dataBase == null) {
            dataBase = TodoItemsApplication.getInstance().getDatabase();
        }

        // Find all UI components.
        TextView creationTimeView = findViewById(R.id.creationTimeView);
        TextView modifiedTimeView = findViewById(R.id.modifiedTimeView);
        CheckBox progressCheckbox = findViewById(R.id.progressCheckbox);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);

        // Fill fields with item info.
        if (savedInstanceState != null) {
            item = (TodoItem) savedInstanceState.getSerializable("todo_item");
        } else {
            Intent openedMe = getIntent();
            item = (TodoItem) openedMe.getSerializableExtra("todo_item");
        }
        item = dataBase.getEqualItem(item);

        creationTimeView.setText(item.getCreationTimeString());
        modifiedTimeView.setText(item.getModifiedTimeString());
        progressCheckbox.setChecked(item.isDone());
        descriptionEditText.setText(item.getDescription());

        // Set listeners.
        progressCheckbox.setOnClickListener(v -> {
            item.modify();
            if (item.isDone()) {
                dataBase.markItemInProgress(item);
            } else {
                dataBase.markItemDone(item);
            }
            modifiedTimeView.setText(item.getModifiedTimeString());
        });

        descriptionEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                item.modify();
                dataBase.changeItemDescription(item, descriptionEditText.getText().toString());
                modifiedTimeView.setText(item.getModifiedTimeString());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("todo_item", item);
    }
}
