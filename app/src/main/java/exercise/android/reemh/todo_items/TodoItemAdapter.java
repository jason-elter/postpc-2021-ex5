package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemHolder> {

    private final TodoItemsDataBase dataBase;
    private List<TodoItem> items;

    public TodoItemAdapter(AppCompatActivity activity) {
        super();
        dataBase = TodoItemsApplication.getInstance().getDatabase();
        items = dataBase.getCurrentItems();
        notifyDataSetChanged();

        dataBase.getLiveData().observe(activity, todoItems -> {
            items = todoItems;
            notifyDataSetChanged();
        });
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public TodoItemHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_todo_item, parent, false);
        return new TodoItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull TodoItemHolder holder, int position) {
        TodoItem item = items.get(position);
        holder.setup(item.getDescription(), item.isDone());

        // Check box.
        holder.progressCheckbox.setOnClickListener(v -> {
            if (item.isDone()) {
                dataBase.markItemInProgress(item);
            } else {
                dataBase.markItemDone(item);
            }
        });

        // Delete item.
        holder.deleteView.setOnClickListener(v -> dataBase.deleteItem(item));

        // Edit item.
        holder.textView.setOnClickListener(v -> {
            // Create new intent and open edit activity
            Context context = v.getContext();
            Intent editIntent = new Intent(context, EditActivity.class);
            editIntent.putExtra("todo_item", item);
            context.startActivity(editIntent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
