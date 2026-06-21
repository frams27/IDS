package afam.application.control;

import afam.application.dto.SystemException;
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
        return boundaryDBMS.recuperaContenuti(account.idAccount());
    }

    public void verificaSelezione(List<ContenutoMultimediale> selezionati) throws SystemException {
        if (selezionati == null || selezionati.isEmpty()) {
            throw new SystemException("Selezionare almeno un contenuto per procedere");
        }
    }
}
