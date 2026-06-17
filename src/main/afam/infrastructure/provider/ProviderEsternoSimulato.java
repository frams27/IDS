package afam.infrastructure.provider;

public class ProviderEsternoSimulato implements BoundaryProviderEsterno {
    @Override
    public boolean autenticaStudente(String emailProvider) {
        return emailProvider != null && !emailProvider.isBlank();
    }
}
