package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.repository.BoundaryDBMS;

import java.util.List;

public class OrganizzaContenutiControl {
    private final BoundaryDBMS boundaryDBMS;

    public OrganizzaContenutiControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<ContenutoMultimediale> recuperaContenuti(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaContenuti(account.id());
    }

    public void salvaNuovoOrdine(List<ContenutoMultimediale> contenutiOrdinati) throws Exception {
        if (contenutiOrdinati == null || contenutiOrdinati.size() < 2) {
            throw new ApplicationException("Servono almeno due contenuti per organizzarli.");
        }
        boundaryDBMS.salvaOrdineContenuti(contenutiOrdinati);
    }
}
