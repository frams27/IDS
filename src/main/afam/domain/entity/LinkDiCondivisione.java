package afam.domain.entity;

import java.time.LocalDate;
import java.util.Objects;

public final class LinkDiCondivisione {
    private final int id;
    private final int accountStudenteId;
    private final String url;
    private final String descrizione;
    private final LocalDate dataDiScadenza;
    private final boolean attivo;
    private final int numeroVisualizzazioni;

    public LinkDiCondivisione(int id, int accountStudenteId, String url, String descrizione,
                              LocalDate dataDiScadenza, boolean attivo, int numeroVisualizzazioni) {
        this.id = id;
        this.accountStudenteId = accountStudenteId;
        this.url = url;
        this.descrizione = descrizione;
        this.dataDiScadenza = dataDiScadenza;
        this.attivo = attivo;
        this.numeroVisualizzazioni = numeroVisualizzazioni;
    }

    public int id() {
        return id;
    }

    public int accountStudenteId() {
        return accountStudenteId;
    }

    public String url() {
        return url;
    }

    public String descrizione() {
        return descrizione;
    }

    public LocalDate dataDiScadenza() {
        return dataDiScadenza;
    }

    public boolean attivo() {
        return attivo;
    }

    public int numeroVisualizzazioni() {
        return numeroVisualizzazioni;
    }

    public int getId() {
        return id;
    }

    public int getAccountStudenteId() {
        return accountStudenteId;
    }

    public String getUrl() {
        return url;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDate getDataDiScadenza() {
        return dataDiScadenza;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public int getNumeroVisualizzazioni() {
        return numeroVisualizzazioni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkDiCondivisione that)) return false;
        return id == that.id
                && accountStudenteId == that.accountStudenteId
                && attivo == that.attivo
                && numeroVisualizzazioni == that.numeroVisualizzazioni
                && Objects.equals(url, that.url)
                && Objects.equals(descrizione, that.descrizione)
                && Objects.equals(dataDiScadenza, that.dataDiScadenza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountStudenteId, url, descrizione, dataDiScadenza, attivo, numeroVisualizzazioni);
    }

    @Override
    public String toString() {
        return "LinkDiCondivisione[id=" + id
                + ", accountStudenteId=" + accountStudenteId
                + ", url=" + url
                + ", descrizione=" + descrizione
                + ", dataDiScadenza=" + dataDiScadenza
                + ", attivo=" + attivo
                + ", numeroVisualizzazioni=" + numeroVisualizzazioni + "]";
    }
}
