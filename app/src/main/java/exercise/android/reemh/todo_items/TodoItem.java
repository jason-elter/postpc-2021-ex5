package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TodoItem implements Serializable {
    public static final boolean IN_PROGRESS = false, DONE = true;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm", Locale.ENGLISH),
            HOURS_FORMAT = new SimpleDateFormat("'Today at' HH:mm", Locale.ENGLISH);

    private final String id;
    private final long creationTime;
    private String description;
    private boolean isDone;
    private long modificationTime;


    public TodoItem(String description, boolean isDone) {
        this(UUID.randomUUID().toString(), description, isDone, System.currentTimeMillis());
    }

    private TodoItem(String id, String description, boolean isDone, long creationTime) {
        this.id = id;
        this.description = description;
        this.isDone = isDone;
        this.creationTime = creationTime;
        this.modificationTime = creationTime;
    }

    public static TodoItem fromSerialized(String format) {
        if (format == null) return null;

        String[] fields = format.split("█");
        if (fields.length != 4) return null;

        try {
            boolean isDone = Boolean.parseBoolean(fields[2]);
            long creationTime = Long.parseLong(fields[3]);

            return new TodoItem(fields[0], fields[1], isDone, creationTime);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String getTimeString(long time) {
        long currentTime = System.currentTimeMillis();
        long differenceInMinutes = (currentTime - time) / 1000L / 60L;

        if (differenceInMinutes < 60L) {
            // In the last hour.
            return differenceInMinutes + " minutes ago";
        }

        long today = currentTime / 1000L / 60L / 60L / 24L;
        long dayToCompare = time / 1000L / 60L / 60L / 24L;

        if (today == dayToCompare) {
            // In the last day.
            return HOURS_FORMAT.format(new Date(time));
        }

        // Before the last day
        return DATE_FORMAT.format(new Date(time));
    }

    public String serialize() {
        return id + "█" + description + "█" + isDone + "█" + creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return id.equals(todoItem.id);
    }

    public int compareTo(TodoItem other) {
        return Long.compare(creationTime, other.creationTime);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setProgress(boolean isDone) {
        this.isDone = isDone;
    }

    public void modify() {
        modificationTime = System.currentTimeMillis();
    }

    public String getCreationTimeString() {
        return getTimeString(creationTime);
    }

    public String getModifiedTimeString() {
        return getTimeString(modificationTime);
    }
}
