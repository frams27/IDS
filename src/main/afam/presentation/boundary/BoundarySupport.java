package afam.presentation.boundary;

import afam.domain.entity.ContenutoMultimediale;
import afam.domain.entity.LinkDiCondivisione;
import afam.util.FileUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.Collections;

public final class BoundarySupport {
    private BoundarySupport() {}

    public static ListView<ContenutoMultimediale> listaContenuti(ObservableList<ContenutoMultimediale> items) {
        ListView<ContenutoMultimediale> list = new ListView<>(items);
        list.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(ContenutoMultimediale item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("[" + item.tipo() + "] " + item.titolo() + " — "
                            + FileUtil.humanSize(item.dimensione()) + " — " + item.formato());
                }
            }
        });
        return list;
    }

    public static ListView<LinkDiCondivisione> listaLink(ObservableList<LinkDiCondivisione> links) {
        ListView<LinkDiCondivisione> list = new ListView<>(links);
        list.setPrefHeight(360);
        list.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(LinkDiCondivisione item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.url()
                            + " — scadenza: " + (item.dataDiScadenza() == null ? "nessuna" : item.dataDiScadenza())
                            + " — stato: " + (item.attivo() ? "attivo" : "disattivo")
                            + " — visualizzazioni: " + item.numeroVisualizzazioni());
                }
            }
        });
        return list;
    }

    public static void spostaSelezionato(ListView<ContenutoMultimediale> list, int delta) {
        int idx = list.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        int target = idx + delta;
        if (target < 0 || target >= list.getItems().size()) return;
        Collections.swap(list.getItems(), idx, target);
        list.getSelectionModel().select(target);
    }
}
