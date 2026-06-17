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
import java.util.Locale;
import java.util.Set;

public class AggiungiContenutiControl {
    public static final long MAX_FILE_SIZE_BYTES = 512L * 1024L * 1024L;
    private final BoundaryDBMS boundaryDBMS;

    public AggiungiContenutiControl(BoundaryDBMS boundaryDBMS) {
        this.boundaryDBMS = boundaryDBMS;
    }

    public List<ContenutoMultimediale> recuperaContenuti(AccountStudente account) throws Exception {
        return boundaryDBMS.recuperaContenuti(account.id());
    }

    public void aggiungiContenuto(AccountStudente account, Path fileSelezionato, String tipo) throws Exception {
        if (fileSelezionato == null) throw new ApplicationException("Nessun file selezionato.");
        long dimensione = Files.size(fileSelezionato);
        if (dimensione > MAX_FILE_SIZE_BYTES) throw new ApplicationException("La dimensione massima consentita è 512 MB.");
        String formato = FileUtil.extension(fileSelezionato.getFileName().toString());
        if (!formatoConsentito(tipo, formato)) throw new ApplicationException("Formato non supportato per la categoria selezionata.");
        Files.createDirectories(boundaryDBMS.cartellaUpload());
        String safeName = System.currentTimeMillis() + "_" + fileSelezionato.getFileName().toString().replaceAll("[^A-Za-z0-9._-]", "_");
        Path destinazione = boundaryDBMS.cartellaUpload().resolve(safeName);
        Files.copy(fileSelezionato, destinazione, StandardCopyOption.REPLACE_EXISTING);
        boundaryDBMS.salvaContenuto(account.id(), FileUtil.stripExtension(fileSelezionato.getFileName().toString()), tipo,
                fileSelezionato.getFileName().toString(), formato, dimensione, destinazione);
    }

    private static boolean formatoConsentito(String tipo, String formato) {
        String t = tipo == null ? "" : tipo.toLowerCase(Locale.ROOT);
        String f = formato == null ? "" : formato.toLowerCase(Locale.ROOT);
        return switch (t) {
            case "documento" -> Set.of("pdf").contains(f);
            case "audio" -> Set.of("mp3", "wav", "flac").contains(f);
            case "foto" -> Set.of("jpg", "jpeg", "png", "svg").contains(f);
            case "video" -> Set.of("mp4", "mov", "avi").contains(f);
            default -> false;
        };
    }
}
