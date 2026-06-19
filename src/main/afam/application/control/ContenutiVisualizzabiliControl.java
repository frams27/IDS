package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.repository.BoundaryDBMS;

import java.util.List;

public class ContenutiVisualizzabiliControl {
    private final BoundaryDBMS boundaryDBMS;

    public ContenutiVisualizzabiliControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<ContenutoMultimediale> recuperaContenutiDisponibili(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaContenuti(account.id());
    }

    public void verificaSelezione(List<ContenutoMultimediale> selezionati) throws ApplicationException {
        if (selezionati == null || selezionati.isEmpty()) {
            throw new ApplicationException("Selezionare almeno un contenuto per procedere");
        }
    }
}
