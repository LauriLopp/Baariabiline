package com.example.projekt;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SulgemisKäsitleja <T extends Event> implements EventHandler <T> {
    private Stage stage;
    public SulgemisKäsitleja(Stage stage) {
        this.stage = stage;
    }

    // Lisame sündmuse akna sulgemisele (kopeeritud 8. nädala koduülesannetest) :
    // aknasündmuse lisamine

    public void handle(T event) {
        // luuakse teine lava
        Stage kusimus = new Stage();
        // küsimuse ja kahe nupu loomine
        Label label = new Label("Kas soovid programmi sulgeda?");
        Button okButton = new Button("JAH");
        Button cancelButton = new Button("EI");

        // sündmuse lisamine nupule Jah
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    Baariraamat2.sulge(okButton);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // sündmuse lisamine nupule Ei
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stage.show();
                kusimus.hide();
            }
        });

        // nuppude grupeerimine
        FlowPane pane = new FlowPane(10, 10);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(okButton, cancelButton);

        // küsimuse ja nuppude gruppi paigutamine
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, pane);

        //stseeni loomine ja näitamine
        Scene stseen2 = new Scene(vBox);
        kusimus.setScene(stseen2);
        kusimus.show();
    }
}


