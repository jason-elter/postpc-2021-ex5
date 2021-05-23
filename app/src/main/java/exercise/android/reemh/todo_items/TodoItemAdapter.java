package exercise.android.reemh.todo_items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemHolder> {

    private List<TodoItem> items = new ArrayList<>();

    public TodoItemsDataBase dataBase = null;

    public void setItems(ArrayList<TodoItem> newItems) {
        items = newItems;
        notifyDataSetChanged();
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
            setItems(dataBase.getCurrentItems());
        });

        // Delete item by long press.
        holder.textView.setOnLongClickListener(v -> {
            dataBase.deleteItem(item);
            setItems(dataBase.getCurrentItems());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
