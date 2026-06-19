package afam.application.control;

import afam.domain.entity.AccountStudente;
import afam.domain.entity.DatiCurriculari;
import afam.domain.repository.BoundaryDBMS;

public class ModificaDatiCurriculariControl {
    private final BoundaryDBMS boundaryDBMS;

    public ModificaDatiCurriculariControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public DatiCurriculari recuperaDatiCurriculari(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaDatiCurriculari(account.idAccount());
    }

    public void salvaDatiCurriculari(AccountStudente account, String biografia, String titoliDiStudio, String esperienzeArtisticheEFormative) throws Exception {
        ValidazioneControlSupport.verificaLimiteTesto(biografia, "Biografia");
        ValidazioneControlSupport.verificaLimiteTesto(titoliDiStudio, "Titoli di studio");
        ValidazioneControlSupport.verificaLimiteTesto(esperienzeArtisticheEFormative, "Esperienze artistiche e formative");
        boundaryDBMS.salvaDatiCurriculari(account.idAccount(), biografia, titoliDiStudio, esperienzeArtisticheEFormative);
    }
}
