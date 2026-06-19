package afam.domain.entity;

import java.nio.file.Path;
import java.util.Objects;

public final class ContenutoMultimediale {
    private final int idContenuto;
    private final int idAccount;
    private final String titolo;
    private final String formato;
    private final long dimensione;
    private final Path percorsoFile;
    private final int posizione;

    public ContenutoMultimediale(int idContenuto, int idAccount, String titolo,
                                 String formato, long dimensione, Path percorsoFile, int posizione) {
        this.idContenuto = idContenuto;
        this.idAccount = idAccount;
        this.titolo = titolo;
        this.formato = formato;
        this.dimensione = dimensione;
        this.percorsoFile = percorsoFile;
        this.posizione = posizione;
    }

    public int idContenuto() {
        return idContenuto;
    }

    public int idAccount() {
        return idAccount;
    }

    public String titolo() {
        return titolo;
    }

    public String formato() {
        return formato;
    }

    public long dimensione() {
        return dimensione;
    }

    public Path percorsoFile() {
        return percorsoFile;
    }

    public int posizione() {
        return posizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContenutoMultimediale that)) return false;
        return idContenuto == that.idContenuto
                && idAccount == that.idAccount
                && dimensione == that.dimensione
                && posizione == that.posizione
                && Objects.equals(titolo, that.titolo)
                && Objects.equals(formato, that.formato)
                && Objects.equals(percorsoFile, that.percorsoFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idContenuto, idAccount, titolo, formato, dimensione, percorsoFile, posizione);
    }

    @Override
    public String toString() {
        return titolo + " - " + formato;
    }
}
