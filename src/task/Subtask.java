package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    protected final int epicLink;

    public Subtask(int id, String name, String description, int epicLink) {
        super(id, name, description);
        this.epicLink = epicLink;
    }

    public Subtask(String name, String description, int epicLink) {
        super(name, description);
        this.epicLink = epicLink;
    }

    public Subtask(int id, String name, String description, LocalDateTime startTime, Duration duration, int epicLink) {
        super(id, name, description);
        this.startTime = startTime;
        this.duration = duration;
        this.epicLink = epicLink;
    }

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration, int epicLink) {
        super(name, description);
        this.startTime = startTime;
        this.duration = duration;
        this.epicLink = epicLink;
    }

    public int getEpicLink() {
        return epicLink;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Subtask subtask = (Subtask) object;
        return epicLink == subtask.epicLink;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicLink);
    }

    @Override
    public String toString() {
        return "task.Subtask{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description.length() + "'" +
                ", status=" + status +
                ", epicLink=" + epicLink +

                '}';
    }
}
