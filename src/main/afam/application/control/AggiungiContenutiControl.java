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
import java.util.Set;

public class AggiungiContenutiControl {
    public static final long MAX_FILE_SIZE_BYTES = 512L * 1024L * 1024L;
    private static final Set<String> FORMATI_CONSENTITI = Set.of(
            "pdf", "doc", "docx", "txt", "odt",
            "mp3", "wav", "ogg", "flac", "aac",
            "jpg", "jpeg", "png", "gif", "bmp", "webp",
            "mp4", "avi", "mov", "mkv", "wmv"
    );
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
        if (!FORMATI_CONSENTITI.contains(formato.toLowerCase()))
            throw new ApplicationException("Formato file non supportato: " + formato);
        Files.createDirectories(boundaryDBMS.cartellaUpload());
        String safeName = System.currentTimeMillis() + "_" + fileSelezionato.getFileName().toString().replaceAll("[^A-Za-z0-9._-]", "_");
        Path destinazione = boundaryDBMS.cartellaUpload().resolve(safeName);
        Files.copy(fileSelezionato, destinazione, StandardCopyOption.REPLACE_EXISTING);
        boundaryDBMS.salvaContenuto(account.idAccount(), FileUtil.stripExtension(fileSelezionato.getFileName().toString()),
                formato, dimensione, destinazione);
    }
}
