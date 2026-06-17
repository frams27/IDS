package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.LinkDiCondivisione;
import afam.domain.repository.BoundaryDBMS;

import java.util.List;

public class DisattivaLinkControl {
    private final BoundaryDBMS boundaryDBMS;

    public DisattivaLinkControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<LinkDiCondivisione> recuperaLinkAttivi(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaLinkDiCondivisione(account.id(), true);
    }

    public void disattivaLink(LinkDiCondivisione link) throws Exception {
        if (link == null) throw new ApplicationException("Seleziona il link da disattivare.");
        boundaryDBMS.disattivaLink(link.id());
    }
}
