package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.application.dto.PortfolioCondiviso;
import afam.domain.entity.LinkDiCondivisione;
import afam.domain.repository.BoundaryDBMS;

import java.util.Optional;

public class VisualizzazioneContenutiControl {
    private final BoundaryDBMS boundaryDBMS;

    public VisualizzazioneContenutiControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public LinkDiCondivisione validaLink(String url) throws Exception {
        if (url == null || url.trim().isEmpty()) {
            throw new ApplicationException("Inserisci il link ricevuto.");
        }
        Optional<LinkDiCondivisione> link = boundaryDBMS.recuperaLinkValido(url.trim());
        if (link.isEmpty()) {
            throw new ApplicationException("Link Inesistente.");
        }
        boundaryDBMS.registraVisualizzazione(link.get().id(), "Soggetto esterno");
        return link.get();
    }

    public PortfolioCondiviso recuperaPortfolioCondiviso(LinkDiCondivisione link) throws Exception {
        return new PortfolioCondiviso(
                boundaryDBMS.recuperaDatiCurriculari(link.accountStudenteId()),
                boundaryDBMS.recuperaContenutiPerLink(link.id())
        );
    }
}
