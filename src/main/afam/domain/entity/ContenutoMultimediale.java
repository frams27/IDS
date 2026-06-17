package afam.domain.entity;

import java.nio.file.Path;
import java.util.Objects;

public final class ContenutoMultimediale {
    private final int id;
    private final int accountStudenteId;
    private final String titolo;
    private final String tipo;
    private final String nomeOriginale;
    private final String formato;
    private final long dimensione;
    private final Path percorsoFile;
    private final int posizione;

    public ContenutoMultimediale(int id, int accountStudenteId, String titolo, String tipo, String nomeOriginale,
                                 String formato, long dimensione, Path percorsoFile, int posizione) {
        this.id = id;
        this.accountStudenteId = accountStudenteId;
        this.titolo = titolo;
        this.tipo = tipo;
        this.nomeOriginale = nomeOriginale;
        this.formato = formato;
        this.dimensione = dimensione;
        this.percorsoFile = percorsoFile;
        this.posizione = posizione;
    }

    public int id() {
        return id;
    }

    public int accountStudenteId() {
        return accountStudenteId;
    }

    public String titolo() {
        return titolo;
    }

    public String tipo() {
        return tipo;
    }

    public String nomeOriginale() {
        return nomeOriginale;
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

    public int getId() {
        return id;
    }

    public int getAccountStudenteId() {
        return accountStudenteId;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNomeOriginale() {
        return nomeOriginale;
    }

    public String getFormato() {
        return formato;
    }

    public long getDimensione() {
        return dimensione;
    }

    public Path getPercorsoFile() {
        return percorsoFile;
    }

    public int getPosizione() {
        return posizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContenutoMultimediale that)) return false;
        return id == that.id
                && accountStudenteId == that.accountStudenteId
                && dimensione == that.dimensione
                && posizione == that.posizione
                && Objects.equals(titolo, that.titolo)
                && Objects.equals(tipo, that.tipo)
                && Objects.equals(nomeOriginale, that.nomeOriginale)
                && Objects.equals(formato, that.formato)
                && Objects.equals(percorsoFile, that.percorsoFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountStudenteId, titolo, tipo, nomeOriginale, formato, dimensione, percorsoFile, posizione);
    }

    @Override
    public String toString() {
        return "[" + tipo + "] " + titolo + " - " + formato;
    }
}
