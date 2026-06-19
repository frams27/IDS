package afam.application.control;

import afam.application.dto.ApplicationException;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.repository.BoundaryDBMS;
import afam.util.FileUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class AggiungiContenutiControl {
    public static final long MAX_FILE_SIZE_BYTES = 512L * 1024L * 1024L;
    private final BoundaryDBMS boundaryDBMS;

    public AggiungiContenutiControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<ContenutoMultimediale> recuperaContenuti(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaContenuti(account.idAccount());
    }

    public void aggiungiContenuto(AccountStudente account, Path fileSelezionato) throws Exception {
        if (fileSelezionato == null) throw new ApplicationException("Nessun file selezionato.");
        long dimensione = Files.size(fileSelezionato);
        if (dimensione > MAX_FILE_SIZE_BYTES) throw new ApplicationException("ERRORE: la dimensione massima consentita è 512 MB");
        String formato = FileUtil.extension(fileSelezionato.getFileName().toString());
        Files.createDirectories(boundaryDBMS.cartellaUpload());
        String safeName = System.currentTimeMillis() + "_" + fileSelezionato.getFileName().toString().replaceAll("[^A-Za-z0-9._-]", "_");
        Path destinazione = boundaryDBMS.cartellaUpload().resolve(safeName);
        Files.copy(fileSelezionato, destinazione, StandardCopyOption.REPLACE_EXISTING);
        boundaryDBMS.salvaContenuto(account.idAccount(), FileUtil.stripExtension(fileSelezionato.getFileName().toString()),
                formato, dimensione, destinazione);
    }
}
