package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.repository.BoundaryDBMS;

import java.util.List;

public class EliminaContenutiControl {
    private final BoundaryDBMS boundaryDBMS;

    public EliminaContenutiControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<ContenutoMultimediale> recuperaContenutiEliminabili(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaContenuti(account.id());
    }

    public void eliminaContenuti(List<ContenutoMultimediale> selezionati) throws Exception {
        if (selezionati == null || selezionati.isEmpty()) {
            throw new ApplicationException("Seleziona almeno un contenuto da eliminare.");
        }
        for (ContenutoMultimediale contenuto : selezionati) {
            boundaryDBMS.eliminaContenuto(contenuto.id());
        }
    }
}
