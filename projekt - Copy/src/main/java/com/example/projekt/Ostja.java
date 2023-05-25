package com.example.projekt;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Ostja implements Comparable<Ostja> {
    private String nimi;
    private double krediit;

    // Konstruktor
    public Ostja(String nimi, double krediit) {
        this.nimi = nimi;
        this.krediit = krediit;
    }

    // Get, Set ja toString meetodid
    public String getNimi() {
        return nimi;
    }

    public double getKrediit() {
        return krediit;
    }

    public void setKrediit(double krediit) {
        this.krediit = krediit;
    }

    public String toString() {
        return nimi + ", " + krediit;
    }

    // loeb failist varasemad ostjad ja kredidi sisse (et info kaduma ei läheks, kui programm vahepeal sulgeda)
    // Argumendid: faili nimi, kuhu ostjate andmed salvestati; List, kuhu ostjate info salvestatakse
    // ei tagasta midagi, lisab järjendisse ostjad
    public static void teeJärjend(String failinimi, List<Ostja> ostjad) throws Exception {
        File fail = new File(failinimi);
        try (Scanner sc = new Scanner(fail, "UTF-8")) {
            while (sc.hasNextLine()) {
                String[] rida = sc.nextLine().split(", ");
                String uusOstjaNimi = rida[2];
                int i = 0;
                for (Ostja ostja : ostjad) {
                    if (rida[1].equals("MUUT")) {
                        if (ostja.getNimi().equals(uusOstjaNimi)) {
                            ostja.setKrediit(Double.parseDouble(rida[4]));
                            i++;
                        }
                    }
                }
                if (i != 1)
                    if (rida[1].equals("MUUT"))
                        ostjad.add(new Ostja(rida[2], Double.parseDouble(rida[4])));
            }
        }
    }

    // klassimeetod, kontrollib, kas ostja on juba krediidi avanud
    // Argumendid: kasutaja sisestatud nimi; List, kuhu on ostjate info kirjutatud
    // Tagastab: ostja tüüpi objekti
    // Kui ostjat veel ole, siis loob ostja ja tagastab selle
    // Väljastab ostja andmed või teate, et loodi uus ostja ja väljastab uue ostja andmed
    public static Ostja kontrolliOstjat(String sisestatudNimi, List<Ostja> ostjad, VBox vBoxLogi) {
        for (Ostja ostja : ostjad) {
            if (ostja.getNimi().equalsIgnoreCase(sisestatudNimi)) {
                vBoxLogi.getChildren().add(new Text(Baariraamat2.aeg() + " Kontrolliti ostjat: " + ostja));
                return ostja;
            }
        }
        System.out.println("Ostjat pole veel nimekirjas.");
        Ostja uusOstja = new Ostja(sisestatudNimi, 0);
        System.out.println("Lisasin: " + uusOstja);
        vBoxLogi.getChildren().add(new Text(Baariraamat2.aeg() + " Ostjat pole nimekirjas, lisan: " + uusOstja));
        ostjad.add(uusOstja);
        return uusOstja;
    }

    // Isendimeetod, muudab ostja krediiti
    // Argument: krediidi muutuse suurus
    // Väljastab inimese uue krediidi
    // Kirjutab faili, kas toimus ost või lisamine ning inimese uue krediidi
    public void muudaKrediiti(double krediidimuut, VBox vBoxLogi) throws IOException {
        krediit += krediidimuut;
        vBoxLogi.getChildren().add(new Text(Baariraamat2.aeg() + " Uus krediit = " + krediit));
        System.out.println("Uus krediit = " + krediit);
        File fail = new File("Salafail.txt");
        try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fail, true), "UTF-8"))) {
            output.append(Baariraamat2.aeg() + ", MUUT" +", " + nimi + ", " + krediidimuut + ", " + krediit + "\n");

            // if (krediidimuut > 0) output.append("Lisamine, ").append(toString()).append('\n');
            // else output.append("Ost, ").append(toString()).append('\n');
        }
    }

    @Override
    public int compareTo(Ostja o) {
        return nimi.toLowerCase().compareTo(o.getNimi().toLowerCase());
    }
}
