package afam.domain.entity;

import java.util.Objects;

public final class AccountStudente {
    private final int id;
    private final String email;

    public AccountStudente(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int id() {
        return id;
    }

    public String email() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountStudente that)) return false;
        return id == that.id && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "AccountStudente[id=" + id + ", email=" + email + "]";
    }
}
