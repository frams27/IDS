package afam.application.control;

import afam.application.dto.SystemException;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.LinkDiCondivisione;
import afam.domain.repository.BoundaryDBMS;

import java.util.List;

public class DisattivaLinkControl {
    private final BoundaryDBMS boundaryDBMS;

    public DisattivaLinkControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<LinkDiCondivisione> recuperaLink(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaLinkDiCondivisione(account.idAccount());
    }

    public void disattivaLink(LinkDiCondivisione link) throws Exception {
        if (link == null) throw new SystemException("Seleziona il link da eliminare.");
        boundaryDBMS.disattivaLink(link.idLink());
    }
}
