package afam.infrastructure.provider;

import afam.domain.provider.BoundaryProviderEsterno;

public class ProviderEsternoSimulato implements BoundaryProviderEsterno {
    @Override
    public boolean autenticaStudente(String emailProvider) {
        return emailProvider != null && !emailProvider.isBlank();
    }
}
