package afam.application.control;

import afam.domain.entity.AccountStudente;
import afam.domain.entity.LinkDiCondivisione;
import afam.domain.repository.BoundaryDBMS;

import java.util.List;

public class FeedbackContenutiControl {
    private final BoundaryDBMS boundaryDBMS;

    public FeedbackContenutiControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<LinkDiCondivisione> recuperaLink(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaLinkDiCondivisione(account.id());
    }
}
