package afam.presentation.navigation;

import afam.application.control.*;
import afam.application.dto.ApplicationException;
import afam.application.dto.PortfolioCondiviso;
import afam.application.session.SessioneCorrente;
import afam.domain.entity.AccountStudente;
import afam.domain.entity.ContenutoMultimediale;
import afam.domain.entity.DatiCurriculari;
import afam.domain.entity.LinkDiCondivisione;
import afam.domain.provider.BoundaryProviderEsterno;
import afam.domain.repository.BoundaryDBMS;
import afam.presentation.boundary.*;
import afam.presentation.ui.UiFactory;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppNavigator {
    private enum Vista {
        STARTING,
        REGISTRAZIONE,
        LOGIN,
        DUE_FA,
        AUTENTICAZIONE_PROVIDER,
        RECUPERA_PASSWORD,
        NUOVA_PASSWORD,
        HOME,
        ERRORE_CONNESSIONE,
        PANNELLO_DI_NOTIFICA,
        PANNELLO_DI_CONFERMA,
        GESTIONE_PROFILO,
        AGGIUNGI_CONTENUTI,
        CONTENUTI_ELIMINABILI,
        ORGANIZZA_CONTENUTI,
        MODIFICA_DATI_CURRICULARI,
        MODIFICA_PASSWORD,
        GESTIONE_CONDIVISIONE,
        CONTENUTI_VISUALIZZABILI,
        PANNELLO_CONTENUTI_VISUALIZZABILI,
        PARAMETRI_FACOLTATIVI,
        LINK_GENERATO,
        FEEDBACK_CONTENUTI,
        LISTA_LINK_ATTIVI,
        ACCESSO_LINK,
        CONTENUTI_CONDIVISI
    }

    private final HostServices hostServices;
    private final BoundaryDBMS boundaryDBMS;
    private final SessioneCorrente sessioneCorrente;

    private final RegistrazioneControl registrazioneControl;
    private final AutenticazioneCredenzialiControl autenticazioneCredenzialiControl;
    private final DueFAControl dueFAControl;
    private final AutenticazioneEsternaControl autenticazioneEsternaControl;
    private final PasswordDimenticataControl passwordDimenticataControl;
    private final LogoutControl logoutControl;
    private final ModificaPasswordControl modificaPasswordControl;
    private final AggiungiContenutiControl aggiungiContenutiControl;
    private final EliminaContenutiControl eliminaContenutiControl;
    private final OrganizzaContenutiControl organizzaContenutiControl;
    private final ModificaDatiCurriculariControl modificaDatiCurriculariControl;
    private final ContenutiVisualizzabiliControl contenutiVisualizzabiliControl;
    private final CreazioneLinkControl creazioneLinkControl;
    private final FeedbackContenutiControl feedbackContenutiControl;
    private final DisattivaLinkControl disattivaLinkControl;
    private final VisualizzazioneContenutiControl visualizzazioneContenutiControl;
    private final ConnessioneControl connessioneControl;

    private UiFactory ui;
    private Stage stage;
    private Parent currentScreenRoot;
    private Runnable ripristinaSchermataCorrente;

    public AppNavigator(HostServices hostServices, BoundaryDBMS boundaryDBMS, BoundaryProviderEsterno boundaryProviderEsterno) {
        this.hostServices = hostServices;
        this.boundaryDBMS = boundaryDBMS;
        this.sessioneCorrente = new SessioneCorrente();
        this.registrazioneControl = new RegistrazioneControl(boundaryDBMS);
        this.autenticazioneCredenzialiControl = new AutenticazioneCredenzialiControl(boundaryDBMS, sessioneCorrente);
        this.dueFAControl = new DueFAControl(boundaryDBMS, sessioneCorrente);
        this.autenticazioneEsternaControl = new AutenticazioneEsternaControl(boundaryDBMS, boundaryProviderEsterno, sessioneCorrente);
        this.passwordDimenticataControl = new PasswordDimenticataControl(boundaryDBMS);
        this.logoutControl = new LogoutControl(boundaryDBMS, sessioneCorrente);
        this.modificaPasswordControl = new ModificaPasswordControl(boundaryDBMS, sessioneCorrente);
        this.aggiungiContenutiControl = new AggiungiContenutiControl(boundaryDBMS);
        this.eliminaContenutiControl = new EliminaContenutiControl(boundaryDBMS);
        this.organizzaContenutiControl = new OrganizzaContenutiControl(boundaryDBMS);
        this.modificaDatiCurriculariControl = new ModificaDatiCurriculariControl(boundaryDBMS);
        this.contenutiVisualizzabiliControl = new ContenutiVisualizzabiliControl(boundaryDBMS);
        this.creazioneLinkControl = new CreazioneLinkControl(boundaryDBMS);
        this.feedbackContenutiControl = new FeedbackContenutiControl(boundaryDBMS);
        this.disattivaLinkControl = new DisattivaLinkControl(boundaryDBMS);
        this.visualizzazioneContenutiControl = new VisualizzazioneContenutiControl(boundaryDBMS);
        this.connessioneControl = new ConnessioneControl();
    }

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.ui = new UiFactory(hostServices);
        this.ui.setSimulaPerditaConnessioneAction(() -> {
            Runnable schermataDaRipristinare = ripristinaSchermataCorrente;
            if (schermataDaRipristinare != null) {
                mostra(Vista.ERRORE_CONNESSIONE, schermataDaRipristinare);
            }
        });
        try {
            boundaryDBMS.inizializza();
        } catch (Exception e) {
            mostra(Vista.PANNELLO_DI_NOTIFICA, "Impossibile inizializzare il DBMS: " + e.getMessage(), null);
            stage.centerOnScreen();
            stage.show();
            return;
        }
        stage.setTitle("Identita Digitale AFAM");
        stage.setMinWidth(1100);
        stage.setMinHeight(760);
        stage.setWidth(1100);
        stage.setHeight(760);
        stage.setResizable(true);
        mostra(Vista.STARTING);
        stage.centerOnScreen();
        stage.show();
    }

    private void setScreen(Parent root, String title) {
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        currentScreenRoot = root;
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1100, 760);
            scene.getStylesheets().add(css);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
            if (!scene.getStylesheets().contains(css)) scene.getStylesheets().add(css);
        }
        stage.setTitle("Identita Digitale AFAM - " + title);
    }

    private void setPopup(Parent panel) {
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        if (currentScreenRoot == null) {
            StackPane emptyRoot = new StackPane();
            emptyRoot.setPrefSize(1100, 760);
            emptyRoot.getStyleClass().add("page-root");
            currentScreenRoot = emptyRoot;
        }
        StackPane overlay = new StackPane(panel);
        overlay.getStyleClass().add("popup-overlay");
        StackPane root = new StackPane(currentScreenRoot, overlay);
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root, 1100, 760);
            scene.getStylesheets().add(css);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
            if (!scene.getStylesheets().contains(css)) scene.getStylesheets().add(css);
        }
    }

    private AccountStudente requireStudent() {
        try {
            return sessioneCorrente.richiediAccountStudente();
        } catch (Exception e) {
            mostra(Vista.PANNELLO_DI_NOTIFICA, "Effettua prima il login.", (Runnable) () -> mostra(Vista.STARTING));
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private void mostra(Vista vista, Object... dati) {
        if (vista != Vista.ERRORE_CONNESSIONE
                && vista != Vista.PANNELLO_DI_NOTIFICA
                && vista != Vista.PANNELLO_DI_CONFERMA
                && vista != Vista.AGGIUNGI_CONTENUTI
                && vista != Vista.CONTENUTI_ELIMINABILI
                && vista != Vista.PANNELLO_CONTENUTI_VISUALIZZABILI
                && vista != Vista.PARAMETRI_FACOLTATIVI
                && vista != Vista.LINK_GENERATO
                && vista != Vista.ACCESSO_LINK) {
            Object[] datiCorrenti = dati == null ? new Object[0] : dati.clone();
            ripristinaSchermataCorrente = () -> mostra(vista, datiCorrenti);
        }
        switch (vista) {
            case STARTING -> {
                sessioneCorrente.terminaSessione();
                SchermataStartingPage page = new SchermataStartingPage(ui);

                page.pulsanteRegistrati().setOnAction(e -> mostra(Vista.REGISTRAZIONE)); /*Cliccando sul tasto viene
                                                                                          mostrata la pagina di registrazione*/
                page.pulsanteAccedi().setOnAction(e -> mostra(Vista.LOGIN));
                page.pulsanteEntraComeUtenteEsterno().setOnAction(e -> mostra(Vista.ACCESSO_LINK));
                setScreen(page.mostra(), "Schermata iniziale");
            }
            case REGISTRAZIONE -> {
                PaginaDiRegistrazione page = new PaginaDiRegistrazione(ui, () -> mostra(Vista.STARTING));
                page.pulsanteConfermaRegistrazione().setOnAction(e -> {
                    try {
                        registrazioneControl.registra(page.email(), page.password(), page.confermaPassword());
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Registrazione eseguita con successo!",
                                (Runnable) () -> mostra(Vista.STARTING));
                    } catch (Exception ex) {
                        handleError(ex, () -> mostra(Vista.STARTING));
                    }
                });
                setScreen(page.mostra(), "Pagina di registrazione");
            }
            case LOGIN -> {
                PaginaDiLogin page = new PaginaDiLogin(ui, () -> mostra(Vista.STARTING));
                page.pulsanteAccessoConCredenziali().setOnAction(e -> {
                    try {
                        autenticazioneCredenzialiControl.autentica(page.email(), page.password());
                        mostra(Vista.DUE_FA);
                    } catch (Exception ex) {
                        handleError(ex, () -> mostra(Vista.STARTING));
                    }
                });
                page.pulsanteLoginEsterno().setOnAction(e -> mostra(Vista.AUTENTICAZIONE_PROVIDER));
                page.pulsantePasswordDimenticata().setOnAction(e -> mostra(Vista.RECUPERA_PASSWORD));
                setScreen(page.mostra(), "Pagina di login");
            }
            case DUE_FA -> {
                Pagina2FA page = new Pagina2FA(ui, () -> mostra(Vista.LOGIN));
                page.pulsanteGeneraOTP().setOnAction(e -> runUserAction(() -> {
                    String otp = dueFAControl.generaOTP();
                    String email = sessioneCorrente.accountStudente().map(AccountStudente::email).orElse("");
                    mostra(Vista.PANNELLO_DI_NOTIFICA, "Codice OTP simulato inviato a " + email + ":\n\n" + otp);
                }));
                page.pulsanteVerificaOTP().setOnAction(e -> {
                    try {
                        dueFAControl.verificaOTP(page.codiceVerifica());
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Login effettuato!",
                                (Runnable) () -> mostra(Vista.HOME));
                    } catch (Exception ex) {
                        if ("Numero massimo di tentativi raggiunto. Sessione annullata.".equals(ex.getMessage())) {
                            handleError(ex, () -> mostra(Vista.STARTING));
                        } else {
                            handleError(ex);
                        }
                    }
                });
                setScreen(page.mostra(), "Pagina 2FA");
            }
            case AUTENTICAZIONE_PROVIDER -> {
                PaginaAutenticazioneProviderEsterno page = new PaginaAutenticazioneProviderEsterno(ui, () -> mostra(Vista.LOGIN));
                page.pulsanteEsitoPositivoProvider().setOnAction(e -> runUserAction(() -> {
                    autenticazioneEsternaControl.autenticaConProviderEsterno(page.emailProvider());
                    mostra(Vista.PANNELLO_DI_NOTIFICA, "Login effettuato!",
                            (Runnable) () -> mostra(Vista.HOME));
                }));
                page.pulsanteEsitoNegativoProvider().setOnAction(e -> {
                    mostra(Vista.PANNELLO_DI_NOTIFICA, "Autenticazione tramite provider esterno fallita o annullata.",
                            (Runnable) () -> mostra(Vista.LOGIN));
                });
                setScreen(page.mostra(), "Autenticazione con provider esterno");
            }
            case RECUPERA_PASSWORD -> {
                PaginaRecuperaPassword page = new PaginaRecuperaPassword(ui, () -> mostra(Vista.LOGIN));
                page.pulsanteInvia().setOnAction(e -> runUserAction(() -> {
                    AccountStudente account = passwordDimenticataControl.richiediRecuperoPassword(page.email());
                    String token = passwordDimenticataControl.generaLinkRipristino(account);
                    mostra(Vista.PANNELLO_DI_NOTIFICA, "Link di ripristino inviato. Controlla la tua casella di posta!\nToken: " + token,
                            (Runnable) () -> mostra(Vista.NUOVA_PASSWORD, account, token));
                }));
                setScreen(page.mostra(), "Pagina recupera password");
            }
            case NUOVA_PASSWORD -> {
                AccountStudente account = (AccountStudente) dati[0];
                String token = (String) dati[1];
                PaginaNuovaPassword page = new PaginaNuovaPassword(ui, () -> mostra(Vista.LOGIN));
                page.pulsanteConferma().setOnAction(e -> runUserAction(() -> {
                    passwordDimenticataControl.impostaNuovaPassword(account, token, page.nuovaPassword(), page.confermaNuovaPassword());
                    mostra(Vista.PANNELLO_DI_NOTIFICA, "Password modificata con successo!",
                            (Runnable) () -> mostra(Vista.LOGIN));
                }));
                setScreen(page.mostra(), "Pagina nuova password");
            }
            case HOME -> {
                AccountStudente account = requireStudent();
                SchermataHomePage page = new SchermataHomePage(ui);
                Parent root = page.mostra(account);
                page.pulsanteGestioneProfilo().setOnAction(e -> mostra(Vista.GESTIONE_PROFILO));
                page.pulsanteGestioneCondivisione().setOnAction(e -> mostra(Vista.GESTIONE_CONDIVISIONE));
                page.pulsanteLogout().setOnAction(e -> confermaLogout());
                setScreen(root, "Home page");
            }
            case ERRORE_CONNESSIONE -> {
                Runnable previous = (Runnable) dati[0];
                connessioneControl.memorizzaSchermata(previous);
                SchermataErroreConnessione page = new SchermataErroreConnessione(ui);
                page.pulsanteRiconnessione().setOnAction(e -> connessioneControl.ripristinaSchermataPrecedente());
                setScreen(page.mostra(), "Errore di connessione");
            }
            case PANNELLO_DI_NOTIFICA -> {
                String messaggio = (String) dati[0];
                Runnable azioneDopoOk = dati.length > 1 ? (Runnable) dati[1] : null;
                PannelloDiNotifica page = new PannelloDiNotifica(ui, messaggio);
                page.pulsanteOk().setOnAction(e -> {
                    if (azioneDopoOk != null) {
                        azioneDopoOk.run();
                    } else if (ripristinaSchermataCorrente != null) {
                        ripristinaSchermataCorrente.run();
                    }
                });
                setPopup(page.mostra());
            }
            case PANNELLO_DI_CONFERMA -> {
                String messaggio = (String) dati[0];
                Runnable azioneConferma = (Runnable) dati[1];
                Runnable azioneAnnulla = dati.length > 2 ? (Runnable) dati[2] : null;
                PannelloDiConferma page = new PannelloDiConferma(ui, messaggio);
                page.pulsanteConferma().setOnAction(e -> {
                    if (azioneConferma != null) {
                        azioneConferma.run();
                    }
                });
                page.pulsanteAnnulla().setOnAction(e -> {
                    if (azioneAnnulla != null) {
                        azioneAnnulla.run();
                    } else if (ripristinaSchermataCorrente != null) {
                        ripristinaSchermataCorrente.run();
                    }
                });
                setPopup(page.mostra());
            }
            case GESTIONE_PROFILO -> {
                AccountStudente account = requireStudent();
                try {
                    DatiCurriculari datiCurriculari = modificaDatiCurriculariControl.recuperaDatiCurriculari(account);
                    ObservableList<ContenutoMultimediale> contenuti = FXCollections.observableArrayList(aggiungiContenutiControl.recuperaContenuti(account));
                    SchermataGestioneProfilo page = new SchermataGestioneProfilo(ui, () -> mostra(Vista.HOME));
                    Parent root = page.mostra(datiCurriculari, contenuti);
                    page.listaContenuti().setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                            ContenutoMultimediale item = page.listaContenuti().getSelectionModel().getSelectedItem();
                            if (item != null) ui.openFile(item.percorsoFile());
                        }
                    });
                    page.pulsanteAggiungiContenuti().setOnAction(e -> mostra(Vista.AGGIUNGI_CONTENUTI));
                    page.pulsanteEliminaContenuti().setDisable(contenuti.isEmpty());
                    page.pulsanteEliminaContenuti().setOnAction(e -> mostra(Vista.CONTENUTI_ELIMINABILI));
                    page.pulsanteOrganizzaContenuti().setDisable(contenuti.size() < 2);
                    page.pulsanteOrganizzaContenuti().setOnAction(e -> mostra(Vista.ORGANIZZA_CONTENUTI));
                    page.pulsanteModificaDatiCurriculari().setOnAction(e -> mostra(Vista.MODIFICA_DATI_CURRICULARI));
                    page.pulsanteModificaPassword().setOnAction(e -> mostra(Vista.MODIFICA_PASSWORD));
                    setScreen(root, "Gestione profilo");
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
            case AGGIUNGI_CONTENUTI -> {
                PannelloAggiungiContenuti page = new PannelloAggiungiContenuti(ui);
                page.pulsanteAggiungiDocumento().setOnAction(e -> selezionaECaricaFile("Documento"));
                page.pulsanteAggiungiAudio().setOnAction(e -> selezionaECaricaFile("Audio"));
                page.pulsanteAggiungiFoto().setOnAction(e -> selezionaECaricaFile("Foto"));
                page.pulsanteAggiungiVideo().setOnAction(e -> selezionaECaricaFile("Video"));
                page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.GESTIONE_PROFILO));
                setPopup(page.mostra());
            }
            case CONTENUTI_ELIMINABILI -> {
                try {
                    ObservableList<ContenutoMultimediale> items = FXCollections.observableArrayList(eliminaContenutiControl.recuperaContenutiEliminabili(requireStudent()));
                    PannelloContenutiEliminabili page = new PannelloContenutiEliminabili(ui);
                    Parent root = page.mostra(items);
                    page.pulsanteConferma().setOnAction(e -> runUserAction(() -> {
                        List<ContenutoMultimediale> selezionati = new ArrayList<>(page.listaContenutiEliminabili().getSelectionModel().getSelectedItems());
                        if (selezionati.isEmpty()) {
                            mostra(Vista.PANNELLO_DI_NOTIFICA, "Selezionare almeno un contenuto per procedere",
                                    (Runnable) () -> mostra(Vista.CONTENUTI_ELIMINABILI));
                        } else {
                            eliminaContenutiControl.eliminaContenuti(selezionati);
                            mostra(Vista.PANNELLO_DI_NOTIFICA, "Eliminazione avvenuta con successo!",
                                    (Runnable) () -> mostra(Vista.GESTIONE_PROFILO));
                        }
                    }));
                    page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.GESTIONE_PROFILO));
                    setPopup(root);
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
            case ORGANIZZA_CONTENUTI -> {
                try {
                    ObservableList<ContenutoMultimediale> items = FXCollections.observableArrayList(organizzaContenutiControl.recuperaContenuti(requireStudent()));
                    PaginaOrganizzaContenuti page = new PaginaOrganizzaContenuti(ui, () -> mostra(Vista.GESTIONE_PROFILO));
                    Parent root = page.mostra(items);
                    page.pulsanteSpostaSu().setOnAction(e -> page.spostaSelezionato(-1));
                    page.pulsanteSpostaGiu().setOnAction(e -> page.spostaSelezionato(1));
                    page.pulsanteConferma().setOnAction(e -> runUserAction(() -> {
                        organizzaContenutiControl.salvaNuovoOrdine(new ArrayList<>(items));
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Modifica effettuata!",
                                (Runnable) () -> mostra(Vista.GESTIONE_PROFILO));
                    }));
                    page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.GESTIONE_PROFILO));
                    setScreen(root, "Organizza contenuti");
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
            case MODIFICA_DATI_CURRICULARI -> {
                try {
                    DatiCurriculari datiCurriculari = modificaDatiCurriculariControl.recuperaDatiCurriculari(requireStudent());
                    PaginaModificaDatiCurriculari page = new PaginaModificaDatiCurriculari(ui, () -> mostra(Vista.GESTIONE_PROFILO));
                    Parent root = page.mostra(datiCurriculari);
                    page.pulsanteSalva().setOnAction(e -> runUserAction(() -> {
                        modificaDatiCurriculariControl.salvaDatiCurriculari(requireStudent(), page.biografia(), page.titoliDiStudio(), page.esperienzeArtisticheEFormative());
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Dati curriculari aggiornati con successo",
                                (Runnable) () -> mostra(Vista.GESTIONE_PROFILO));
                    }));
                    page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.GESTIONE_PROFILO));
                    setScreen(root, "Modifica dati curriculari");
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
            case MODIFICA_PASSWORD -> {
                PaginaModificaPassword page = new PaginaModificaPassword(ui, () -> mostra(Vista.GESTIONE_PROFILO));
                page.pulsanteConferma().setOnAction(e -> runUserAction(() -> {
                    modificaPasswordControl.modificaPassword(page.vecchiaPassword(), page.nuovaPassword(), page.confermaNuovaPassword());
                    mostra(Vista.PANNELLO_DI_NOTIFICA, "Modifica avvenuta con successo",
                            (Runnable) () -> mostra(Vista.GESTIONE_PROFILO));
                }));
                page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.GESTIONE_PROFILO));
                setScreen(page.mostra(), "Modifica password");
            }
            case GESTIONE_CONDIVISIONE -> {
                try {
                    AccountStudente account = requireStudent();
                    SchermataGestioneCondivisione page = new SchermataGestioneCondivisione(ui, () -> mostra(Vista.HOME));
                    List<LinkDiCondivisione> links = feedbackContenutiControl.recuperaLink(account);
                    page.pulsanteGeneraLink().setOnAction(e -> mostra(Vista.CONTENUTI_VISUALIZZABILI));
                    page.pulsanteFeedbackContenuti().setDisable(links.isEmpty());
                    page.pulsanteFeedbackContenuti().setOnAction(e -> mostra(Vista.FEEDBACK_CONTENUTI));
                    page.pulsanteDisattivaLink().setOnAction(e -> mostra(Vista.LISTA_LINK_ATTIVI));
                    setScreen(page.mostra(), "Gestione condivisione");
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
            case CONTENUTI_VISUALIZZABILI -> {
                PaginaContenutiVisualizzabili page = new PaginaContenutiVisualizzabili(ui, () -> mostra(Vista.GESTIONE_CONDIVISIONE));
                page.pulsanteSelezionaContenuti().setOnAction(e -> mostra(Vista.PANNELLO_CONTENUTI_VISUALIZZABILI));
                setScreen(page.mostra(), "Contenuti visualizzabili");
            }
            case PANNELLO_CONTENUTI_VISUALIZZABILI -> {
                try {
                    ObservableList<ContenutoMultimediale> contents = FXCollections.observableArrayList(contenutiVisualizzabiliControl.recuperaContenutiDisponibili(requireStudent()));
                    if (contents.isEmpty()) {
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Carica almeno un contenuto prima di generare un link.",
                                (Runnable) () -> mostra(Vista.CONTENUTI_VISUALIZZABILI));
                        return;
                    }
                    PannelloContenutiVisualizzabili page = new PannelloContenutiVisualizzabili(ui);
                    Parent root = page.mostra(contents);
                    page.pulsanteConferma().setOnAction(e -> runUserAction(() -> {
                        List<ContenutoMultimediale> selected = page.contenutiSelezionati();
                        contenutiVisualizzabiliControl.verificaSelezione(selected);
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Selezione avvenuta con successo!",
                                (Runnable) () -> mostra(Vista.PARAMETRI_FACOLTATIVI, selected));
                    }));
                    page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.CONTENUTI_VISUALIZZABILI));
                    setPopup(root);
                } catch (Exception ex) {
                    handleError(ex, () -> mostra(Vista.CONTENUTI_VISUALIZZABILI));
                }
            }
            case PARAMETRI_FACOLTATIVI -> {
                List<ContenutoMultimediale> contenutiSelezionati = (List<ContenutoMultimediale>) dati[0];
                PannelloParametriFacoltativi page = new PannelloParametriFacoltativi(ui);
                page.pulsanteConferma().setOnAction(e -> runUserAction(() -> {
                    LinkDiCondivisione link = creazioneLinkControl.generaLink(requireStudent(), contenutiSelezionati, page.descrizione(), page.dataDiScadenza());
                    mostra(Vista.LINK_GENERATO, link);
                }));
                page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.GESTIONE_CONDIVISIONE));
                setPopup(page.mostra());
            }
            case LINK_GENERATO -> {
                LinkDiCondivisione link = (LinkDiCondivisione) dati[0];
                PannelloLinkGenerato page = new PannelloLinkGenerato(ui);
                Parent root = page.mostra(link);
                page.pulsanteCopiaLink().setOnAction(e -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(page.linkGenerato());
                    Clipboard.getSystemClipboard().setContent(content);
                    mostra(Vista.PANNELLO_DI_NOTIFICA, "Link copiato!",
                            (Runnable) () -> mostra(Vista.GESTIONE_CONDIVISIONE));
                });
                setPopup(root);
            }
            case FEEDBACK_CONTENUTI -> {
                try {
                    ObservableList<LinkDiCondivisione> links = FXCollections.observableArrayList(feedbackContenutiControl.recuperaLink(requireStudent()));
                    int visualizzazioniTotali = links.stream().mapToInt(LinkDiCondivisione::numeroVisualizzazioni).sum();
                    if (visualizzazioniTotali == 0) {
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "I tuoi contenuti non sono ancora stati visualizzati.",
                                (Runnable) () -> mostra(Vista.GESTIONE_CONDIVISIONE));
                        return;
                    }
                    PaginaFeedbackContenuti page = new PaginaFeedbackContenuti(ui, () -> mostra(Vista.GESTIONE_CONDIVISIONE));
                    Parent root = page.mostra(links);
                    setScreen(root, "Feedback contenuti");
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
            case LISTA_LINK_ATTIVI -> {
                try {
                    ObservableList<LinkDiCondivisione> links = FXCollections.observableArrayList(disattivaLinkControl.recuperaLink(requireStudent()));
                    if (links.isEmpty()) {
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Nessun link attivo presente.",
                                (Runnable) () -> mostra(Vista.GESTIONE_CONDIVISIONE));
                        return;
                    }
                    PaginaLinkAttivi page = new PaginaLinkAttivi(ui, () -> mostra(Vista.GESTIONE_CONDIVISIONE));
                    Parent root = page.mostra(links);
                    page.pulsanteElimina().setOnAction(e -> runUserAction(() -> {
                        disattivaLinkControl.disattivaLink(page.listaLinkAttivi().getSelectionModel().getSelectedItem());
                        mostra(Vista.PANNELLO_DI_NOTIFICA, "Link disattivato con successo.",
                                (Runnable) () -> mostra(Vista.GESTIONE_CONDIVISIONE));
                    }));
                    setScreen(root, "Link attivi");
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
            case ACCESSO_LINK -> {
                PannelloAccessoLink page = new PannelloAccessoLink(ui);
                page.pulsanteConferma().setOnAction(e -> {
                    try {
                        LinkDiCondivisione link = visualizzazioneContenutiControl.validaLink(page.linkInserito());
                        mostra(Vista.CONTENUTI_CONDIVISI, link);
                    } catch (Exception ex) {
                        handleError(ex, () -> mostra(Vista.STARTING));
                    }
                });
                page.pulsanteAnnulla().setOnAction(e -> mostra(Vista.STARTING));
                setPopup(page.mostra());
            }
            case CONTENUTI_CONDIVISI -> {
                LinkDiCondivisione link = (LinkDiCondivisione) dati[0];
                try {
                    PortfolioCondiviso portfolio = visualizzazioneContenutiControl.recuperaPortfolioCondiviso(link);
                    SchermataContenutiCondivisi page = new SchermataContenutiCondivisi(ui, () -> mostra(Vista.STARTING));
                    Parent root = page.mostra(portfolio);
                    page.listaContenutiMultimediali().setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                            ContenutoMultimediale item = page.listaContenutiMultimediali().getSelectionModel().getSelectedItem();
                            if (item != null) ui.openFile(item.percorsoFile());
                        }
                    });
                    page.pulsanteTornaAllaSchermataIniziale().setOnAction(e -> mostra(Vista.STARTING));
                    setScreen(root, "Contenuti condivisi");
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
        }
    }

    private void confermaLogout() { //gestito come metodo e non come case (page) perché non esiste una pagina di logout
        mostra(Vista.PANNELLO_DI_CONFERMA, "Desideri effettuare il logout?", (Runnable) () -> {
            runUserAction(() -> {
                logoutControl.eseguiLogout();
                mostra(Vista.PANNELLO_DI_NOTIFICA, "Logout effettuato con successo.",
                        (Runnable) () -> mostra(Vista.STARTING));
            });
        }, null);
    }

    private void selezionaECaricaFile(String tipoContenuto) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Finestra selezione file");
        chooser.getExtensionFilters().add(filtroPerTipoContenuto(tipoContenuto));
        var file = chooser.showOpenDialog(stage);
        if (file != null) {
            runUserAction(() -> {
                aggiungiContenutiControl.aggiungiContenuto(requireStudent(), file.toPath());
                mostra(Vista.PANNELLO_DI_NOTIFICA, "Caricamento avvenuto con successo",
                        (Runnable) () -> mostra(Vista.GESTIONE_PROFILO));
            });
        }
    }

    private FileChooser.ExtensionFilter filtroPerTipoContenuto(String tipoContenuto) {
        return switch (tipoContenuto) {
            case "Documento" -> new FileChooser.ExtensionFilter("Documenti PDF", "*.pdf");
            case "Audio" -> new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav", "*.flac");
            case "Foto" -> new FileChooser.ExtensionFilter("Foto", "*.jpg", "*.jpeg", "*.png", "*.svg");
            case "Video" -> new FileChooser.ExtensionFilter("Video", "*.mp4", "*.mov", "*.avi");
            default -> new FileChooser.ExtensionFilter("Tutti i file", "*.*");
        };
    }

    private void runUserAction(CheckedAction action) {
        try {
            action.run();
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    private void handleError(Exception ex) {
        handleError(ex, null);
    }

    private void handleError(Exception ex, Runnable azioneDopoOk) {
        if (ex instanceof ApplicationException) {
            mostra(Vista.PANNELLO_DI_NOTIFICA, ex.getMessage(), azioneDopoOk);
        } else {
            mostra(Vista.PANNELLO_DI_NOTIFICA, ex.getMessage() == null ? ex.toString() : ex.getMessage(), azioneDopoOk);
        }
    }

    @FunctionalInterface
    private interface CheckedAction {
        void run() throws Exception;
    }
}
