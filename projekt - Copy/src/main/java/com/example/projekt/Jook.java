package com.example.projekt;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;

public class Jook {
    private String joogiNimi;
    private double joogiHind;

    // konstruktor
    public Jook(String joogiNimi, double joogiHind) {
        this.joogiNimi = joogiNimi;
        this.joogiHind = joogiHind;
    }

    // get
    public String getJoogiNimi() {

        return joogiNimi;
    }

    public double getJoogiHind() {

        return joogiHind;
    }

    // toString
    @Override
    public String toString() {
        return joogiNimi;
    }


    // failist saab lugeda lõpuks kui palju jooke ühe õhtu jooksul osteti
    // Argumendiks: kogus (täisarv)
    // Kirjutab faili jooginime ja koguse
    public void lisaOst(String ostjanimi, int kogus, VBox vBoxLogi) throws Exception {

        vBoxLogi.getChildren().add(new Text(Baariraamat2.aeg() + " Osteti: " + joogiNimi + ", " + kogus));

        File fail = new File("Salafail.txt");
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fail, true), "UTF-8"))) {
            bw.append(Baariraamat2.aeg() + ", OST, " + ostjanimi + ", " + joogiNimi + ", " + kogus + ", " + this.joogiHind + "\n");
        }
    }

    public void lisaOst(int kogus, VBox vBoxLogi) throws Exception {
        vBoxLogi.getChildren().add(new Text(Baariraamat2.aeg() + " Osteti: " + joogiNimi + ", " + kogus));

        File fail = new File("Salafail.txt");
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fail, true), "UTF-8"))) {
            bw.append(Baariraamat2.aeg() + ", OST, , " + joogiNimi + ", " + kogus + ", " + this.joogiHind + "\n");
        }
    }
}


