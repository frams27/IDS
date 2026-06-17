package afam.application.dto;

import afam.domain.entity.ContenutoMultimediale;
import afam.domain.entity.DatiCurriculari;

import java.util.List;

public record PortfolioCondiviso(DatiCurriculari datiCurriculari, List<ContenutoMultimediale> contenutiMultimediali) {
}
