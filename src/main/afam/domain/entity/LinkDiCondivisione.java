package afam.domain.entity;

import java.time.LocalDate;
import java.util.Objects;

public final class LinkDiCondivisione {
    private final int idLink;
    private final int idStudente;
    private final String url;
    private final String descrizione;
    private final LocalDate dataDiScadenza;
    private final int numeroVisualizzazioni;

    public LinkDiCondivisione(int idLink, int idStudente, String url, String descrizione,
                              LocalDate dataDiScadenza, int numeroVisualizzazioni) {
        this.idLink = idLink;
        this.idStudente = idStudente;
        this.url = url;
        this.descrizione = descrizione;
        this.dataDiScadenza = dataDiScadenza;
        this.numeroVisualizzazioni = numeroVisualizzazioni;
    }

    public int idLink() { return idLink; }
    public int idStudente() { return idStudente; }
    public String url() { return url; }
    public String descrizione() { return descrizione; }
    public LocalDate dataDiScadenza() { return dataDiScadenza; }
    public int numeroVisualizzazioni() { return numeroVisualizzazioni; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkDiCondivisione that)) return false;
        return idLink == that.idLink
                && idStudente == that.idStudente
                && numeroVisualizzazioni == that.numeroVisualizzazioni
                && Objects.equals(url, that.url)
                && Objects.equals(descrizione, that.descrizione)
                && Objects.equals(dataDiScadenza, that.dataDiScadenza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLink, idStudente, url, descrizione, dataDiScadenza, numeroVisualizzazioni);
    }

    @Override
    public String toString() {
        return "LinkDiCondivisione[idLink=" + idLink
                + ", idStudente=" + idStudente
                + ", url=" + url
                + ", descrizione=" + descrizione
                + ", dataDiScadenza=" + dataDiScadenza
                + ", numeroVisualizzazioni=" + numeroVisualizzazioni + "]";
    }
}
