package afam.application.control;

public class ConnessioneControl {
    private Runnable schermataPrecedente;

    public void memorizzaSchermata(Runnable screenRenderer) {
        this.schermataPrecedente = screenRenderer;
    }

    public void ripristinaSchermataPrecedente() {
        if (schermataPrecedente != null) {
            schermataPrecedente.run();
        }
    }
}
