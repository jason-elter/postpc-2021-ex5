package exercise.android.reemh.todo_items;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoItemHolder extends RecyclerView.ViewHolder {
    public final TextView textView;
    public final CheckBox progressCheckbox;

    public TodoItemHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.descriptionText);
        progressCheckbox = itemView.findViewById(R.id.progressCheckbox);
    }

    public void setup(String newText, boolean isDone) {
        progressCheckbox.setChecked(isDone);
        textView.setText(newText);
        textView.setPaintFlags(isDone ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
    }
}
