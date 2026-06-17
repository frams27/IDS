package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.entity.LinkDiCondivisione;
import afam.domain.repository.BoundaryDBMS;

import java.time.LocalDate;
import java.util.List;

public class CreazioneLinkControl {
    private final BoundaryDBMS boundaryDBMS;

    public CreazioneLinkControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public LinkDiCondivisione generaLink(AccountStudente account, List<ContenutoMultimediale> contenutiSelezionati, String descrizione, LocalDate dataDiScadenza) throws Exception {
        if (contenutiSelezionati == null || contenutiSelezionati.isEmpty()) {
            throw new ApplicationException("Seleziona almeno un contenuto visualizzabile.");
        }
        ValidazioneControlSupport.verificaLimiteTesto(descrizione, "Descrizione");
        if (dataDiScadenza != null && dataDiScadenza.isBefore(LocalDate.now())) {
            throw new ApplicationException("La data di scadenza non può essere precedente alla data odierna.");
        }
        List<Integer> ids = contenutiSelezionati.stream().map(ContenutoMultimediale::id).toList();
        return boundaryDBMS.salvaLinkDiCondivisione(account.id(), ids, descrizione, dataDiScadenza);
    }
}
