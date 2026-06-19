package afam.presentation.navigation;

class ConnessioneControl {
    private Runnable schermataPrecedente;

    void memorizzaSchermata(Runnable screenRenderer) {
        this.schermataPrecedente = screenRenderer;
    }

    void ripristinaSchermataPrecedente() {
        if (schermataPrecedente != null) {
            schermataPrecedente.run();
        }
    }
}
