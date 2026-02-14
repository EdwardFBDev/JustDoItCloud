package teccr.justdoitcloud.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class User {
    private final String userName;
    private final String name;
    private final String email;
    private final Type type;
    private List<Task> tasks = new ArrayList<>();

    public enum Type {
        ADMIN,
        REGULAR
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void advanceTask(UUID taskId) {
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);

            if (t.getId().equals(taskId)) {

                Task.Status next = switch (t.getStatus()) {
                    case PENDING -> Task.Status.INPROGRESS;
                    case INPROGRESS -> Task.Status.DONE;
                    case DONE -> Task.Status.DONE;
                };

                Task updated = new Task(
                        t.getId(),
                        t.getDescription(),
                        t.getCreated(),
                        t.getDeadline(),
                        next
                );

                tasks.set(i, updated);
                return;
            }
        }
    }

}
