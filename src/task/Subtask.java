package task;

import java.util.Objects;

public class Subtask extends Task {

    private final int epicLink;

    public Subtask(int id, String name, String description, int epicLink) {
        super(id, name, description);
        this.epicLink = epicLink;
    }

    public Subtask(String name, String description, int epicLink) {
        super(name, description);
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
