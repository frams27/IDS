package afam.application.session;

import afam.domain.entity.AccountStudente;

import java.util.Optional;

public class SessioneCorrente {
    private AccountStudente accountStudente;

    public Optional<AccountStudente> accountStudente() {
        return Optional.ofNullable(accountStudente);
    }

    public AccountStudente richiediAccountStudente() {
        if (accountStudente == null) {
            throw new IllegalStateException("Sessione assente.");
        }
        return accountStudente;
    }

    public void avviaSessione(AccountStudente accountStudente) {
        this.accountStudente = accountStudente;
    }

    public void terminaSessione() {
        this.accountStudente = null;
    }
}
