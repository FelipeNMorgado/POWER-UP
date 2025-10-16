package Up.Power.avatar;

import java.util.Objects;

public class AvatarId {
    private int id;

    public AvatarId(int id) { this.id = id; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AvatarId avatarId = (AvatarId) o;
        return id == avatarId.id;
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}


