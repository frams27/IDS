package afam.domain.entity;

import java.util.Objects;

public final class DatiCurriculari {
    private final String biografia;
    private final String titoliDiStudio;
    private final String esperienzeArtisticheEFormative;

    public DatiCurriculari(String biografia, String titoliDiStudio, String esperienzeArtisticheEFormative) {
        this.biografia = biografia;
        this.titoliDiStudio = titoliDiStudio;
        this.esperienzeArtisticheEFormative = esperienzeArtisticheEFormative;
    }

    public static DatiCurriculari vuoti() {
        return new DatiCurriculari("", "", "");
    }

    public String biografia() {
        return biografia;
    }

    public String titoliDiStudio() {
        return titoliDiStudio;
    }

    public String esperienzeArtisticheEFormative() {
        return esperienzeArtisticheEFormative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatiCurriculari that)) return false;
        return Objects.equals(biografia, that.biografia)
                && Objects.equals(titoliDiStudio, that.titoliDiStudio)
                && Objects.equals(esperienzeArtisticheEFormative, that.esperienzeArtisticheEFormative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(biografia, titoliDiStudio, esperienzeArtisticheEFormative);
    }

    @Override
    public String toString() {
        return "DatiCurriculari[biografia=" + biografia
                + ", titoliDiStudio=" + titoliDiStudio
                + ", esperienzeArtisticheEFormative=" + esperienzeArtisticheEFormative + "]";
    }
}
