package afam.domain.entity;

import java.util.Objects;

public final class AccountStudente {
    private final int idAccount;
    private final String email;

    public AccountStudente(int idAccount, String email) {
        this.idAccount = idAccount;
        this.email = email;
    }

    public int idAccount() {
        return idAccount;
    }

    public String email() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountStudente that)) return false;
        return idAccount == that.idAccount && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount, email);
    }

    @Override
    public String toString() {
        return "AccountStudente[idAccount=" + idAccount + ", email=" + email + "]";
    }
}
