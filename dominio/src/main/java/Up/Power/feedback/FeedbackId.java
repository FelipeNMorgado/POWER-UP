package Up.Power.feedback;

import java.util.Objects;

public class FeedbackId {
    private int id;

    public FeedbackId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackId that = (FeedbackId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}


