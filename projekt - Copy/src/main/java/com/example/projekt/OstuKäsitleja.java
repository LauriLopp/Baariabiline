package com.example.projekt;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Collections;

public class OstuKäsitleja implements EventHandler<InputEvent> {
    private TextField nimeväli;
    private TextField krediidiväli;
    private ListView listViewJoogid;
    private TextField koguseväli;
    private VBox vBoxLogi;
    private ListView<Ostja> listViewOstjad;
    private Text baaritulu;
    private Text errorTekst;

    public OstuKäsitleja(TextField nimeväli, TextField krediidiväli, ListView listViewJoogid, TextField koguseväli, VBox vBoxLogi, ListView<Ostja> listViewOstjad, Text baaritulu, Text errorTekst) {
        this.nimeväli = nimeväli;
        this.krediidiväli = krediidiväli;
        this.listViewJoogid = listViewJoogid;
        this.koguseväli = koguseväli;
        this.vBoxLogi = vBoxLogi;
        this.listViewOstjad = listViewOstjad;
        this.baaritulu = baaritulu;
        this.errorTekst = errorTekst;
    }

    @Override
    public void handle(InputEvent event) {
        String nimi = nimeväli.getText();
        if (nimi.contains(",")) nimi = nimi.replaceAll(",", " ");
        Ost ost = new Ost(nimi, krediidiväli.getText(), Baariraamat2.saaValitudJook(listViewJoogid), koguseväli.getText());
        try {
            ost.osta(vBoxLogi);
            Collections.sort(Baariraamat2.ostjad);
            listViewOstjad.refresh();
            nimeväli.setText("");
            krediidiväli.setText("");
            koguseväli.setText("");
            listViewJoogid.getSelectionModel().selectFirst();
            baaritulu.setText("Baari tulu: " + Baariraamat2.baaritulu("Salafail.txt"));
            errorTekst.setText("");
            //vBoxLogi.getChildren().add(new Text(aeg() + " " ));
        } catch (VigaseSisendiErind e) {
            errorTekst.setText("VIGA: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("See tegelt ei ole runtime");
            throw new RuntimeException(e);
        }

    }
}
