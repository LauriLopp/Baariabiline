package com.example.projekt;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Baariraamat2 extends Application {

    // Ostjate list, kuhu hakkame salvestama ostjate andmeid:
    static ObservableList<Ostja> ostjad = FXCollections.observableArrayList();

    // Sõnastik joogi leidmiseks:
    static HashMap<String, Jook> joogid = new HashMap();

    @Override
    public void start(Stage stage) throws Exception {

        // Ettevalmistused programmi tööks
        try {
            Ostja.teeJärjend("Salafail.txt", ostjad);
        } catch (FileNotFoundException e) {
            System.out.println("küsitud faili pole");
        }

        // KÕIGE VÄLIMINE PAIGUTUS
        BorderPane paigustus = new BorderPane();
        //paigustus.setBorder(new Border(.createLineBorder(Color.black));

        // LISAME up
        Text üritus = new Text("Sünnipäev");
        üritus.setFont(new Font(40));
        HBox hBoxÜritus = new HBox(üritus);
        hBoxÜritus.setAlignment(Pos.TOP_CENTER);
        paigustus.setTop(hBoxÜritus);

        // LISAME bottom
        VBox vBoxAlumised = new VBox();
        Button suleNupp = new Button("Lõpeta");
        Text baaritulu = new Text();
        vBoxAlumised.getChildren().addAll(baaritulu, suleNupp);
        vBoxAlumised.setSpacing(10);
        vBoxAlumised.setPadding(new Insets(10, 10, 10, 10));
        paigustus.setBottom(vBoxAlumised);
        sulge(suleNupp);

        // LISAME center
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);


        HBox hBox1 = new HBox();
        Text nimi = new Text("Nimi: ");
        nimi.setFont(new Font(20));
        TextField nimeväli = new TextField();
        nimeväli.setFont(new Font(20));
        hBox1.getChildren().addAll(nimi, nimeväli);
        hBox1.setAlignment(Pos.CENTER);

        HBox hBox2 = new HBox();
        Text krediit = new Text("Lisatav krediit: ");
        krediit.setFont(new Font(20));
        TextField krediidiväli = new TextField();
        krediidiväli.setFont(new Font(20));

        hBox2.getChildren().addAll(krediit, krediidiväli);
        hBox2.setAlignment(Pos.CENTER);

        HBox hBox3 = new HBox();
        Text jook = new Text("Jook: ");
        jook.setFont(new Font(20));


        // Loome jookide jaoks listView ja väärtustame selle meetodis
        ListView<Jook> listViewJoogid = new ListView<>();
        looJoogid(listViewJoogid);
        listViewJoogid.setMaxHeight(300);
        hBox3.getChildren().addAll(jook, listViewJoogid);
        hBox3.setAlignment(Pos.CENTER);
        Jook valitudJook = new Jook("UUS", 5);

        HBox hBox4 = new HBox();
        Text kogus = new Text("Kogus: ");
        kogus.setFont(new Font(20));
        TextField koguseväli = new TextField();
        koguseväli.setFont(new Font(20));
        hBox4.getChildren().addAll(kogus, koguseväli);
        hBox4.setAlignment(Pos.CENTER);

        Button ostaNupp = new Button("Vormista");
        ostaNupp.setFont(new Font(30));

        Text errorTekst = new Text();
        errorTekst.setFill(Color.RED);


        vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, ostaNupp, errorTekst);
        paigustus.setCenter(vBox);

        // LISAME left

        ListView<Ostja> listViewOstjad = new ListView<>();
        //ObservableList<Ostja> ostjadObervable = FXCollections.observableArrayList(Baariraamat2.ostjad);
        listViewOstjad.setItems(Baariraamat2.ostjad);
        listViewOstjad.setPadding(new Insets(20, 20, 0, 20));
        paigustus.setLeft(listViewOstjad);


        // LISAME right

        VBox vBoxLogi = new VBox();

        paigustus.setRight(lisavBoxLogi(vBoxLogi));

        // listivaate omaduse kuulamine (kui midagi valitakse,
        // siis see omadus muutub)
        listViewOstjad.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ostja>() {
            @Override
            public void changed(ObservableValue<? extends Ostja> observable, Ostja oldValue, Ostja newValue) {
                if (newValue != null)
                    nimeväli.setText(newValue.getNimi());
            }
        });

        // Ostmise ja sulgemise meetodite väljakutse
        ostaNupp.setOnMouseClicked(new OstuKäsitleja(nimeväli, krediidiväli, listViewJoogid, koguseväli, vBoxLogi, listViewOstjad, baaritulu, errorTekst));
        stage.setOnHiding(new SulgemisKäsitleja(stage));
        suleNupp.setOnMouseClicked(new SulgemisKäsitleja(stage));


        Scene stseen = new Scene(paigustus, 700, 500);
        String css = Baariraamat2.class.getResource("style.css").toExternalForm();
        stseen.getStylesheets().add(css);
        stage.setTitle("Baariraamat!"); // abi saadud: https://stackoverflow.com/questions/22066799/how-to-use-external-css-file-to-style-a-javafx-application
        stage.setScene(stseen);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static String aeg() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    // Joogid
    private static void looJoogid(ListView<Jook> jookListView) {
        Jook tühiSõne = new Jook("vali", 0);
        Jook viinaShot = new Jook("Viina shot - 2 €", 2);
        Jook kruvikeeraja = new Jook("Kruvikeeraja - 5 €", 5);
        Jook mohito = new Jook("Mohito - 5 €", 5);
        Jook ginTonic = new Jook("Gin Tonic - 5 €", 5);
        Jook rumcola = new Jook("Rum Cola - 5 €", 5);

        ObservableList<Jook> joogid = FXCollections.observableArrayList(tühiSõne, viinaShot, kruvikeeraja, mohito, ginTonic, rumcola);
        jookListView.setItems(joogid);
        jookListView.getSelectionModel().selectFirst();

        for (Jook jook : joogid) {
            Baariraamat2.joogid.put(jook.getJoogiNimi(), jook);
        }
    }

    // Parempoolne logi
    private static ScrollPane lisavBoxLogi(VBox vBoxLogi) {

        ScrollPane scroll = new ScrollPane();
        vBoxLogi.setSpacing(5);
        vBoxLogi.setMinWidth(300);
        vBoxLogi.setAlignment(Pos.TOP_LEFT);
        vBoxLogi.setPadding(new Insets(20, 20, 0, 20));
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setContent(vBoxLogi);
        vBoxLogi.getChildren().add(new Text(aeg() + " Programm käivitati"));

        return scroll;
    }

    // Sulgemisnupp
    public static void sulge(Button suleNupp) throws Exception {

        suleNupp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    kirjutaKokkuvõte();
                    System.exit(0);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static String saaValitudJook(ListView<Jook> listViewJoogid) {
        return listViewJoogid.getSelectionModel().getSelectedItem().toString();
    }

    // tagastab, kui palju on inimsetel kassas kasutamata krediiti
    private static double krediitiKassas(List<Ostja> ostjad) {
        double krediit = 0;
        for (Ostja ostja : ostjad) {
            krediit += ostja.getKrediit();
        }
        return krediit;
    }

    // tagastab listi ostjatega, kellel on veel kassas krediiti
    private static List<Ostja> inimesteKrediit(List<Ostja> ostjad) throws FileNotFoundException {
        List<Ostja> väljastatav = new ArrayList<>();
        for (Ostja ostja : ostjad) {
            if (ostja.getKrediit() != 0) {
                väljastatav.add(ostja);
            }
        }
        Collections.sort(väljastatav);
        return väljastatav;
    }

    // Väljastab, kui palju jäi kasutamata krediiti baari ja kirjudab selle info ka faili
    // Argument: List ostjatest
    public static double baaritulu(String failinimi) throws Exception {
        double tulu = 0;
        try (Scanner sc = new Scanner(new File(failinimi), "UTF-8")) {
            while (sc.hasNextLine()) {
                String[] rida = sc.nextLine().split(", ");
                if (rida[1].equals("OST")) {
                    Jook jook = joogid.get(rida[3]);
                    tulu += Double.parseDouble(rida[4]) * Double.parseDouble(rida[5]);
                }
            }
        }
        return tulu;
    }

    // kirjutab kokkuvõttesse programmi sulgemise kuupäeva ja kellaaja,
    //baari teenitud tulu, baari jäänud kasutama krediit,
    // inimesed, kellel jäi peo lõpus krediiti koos nende krediidiga
    // info selle kohta, mis jooke ja kui palju osteti
    public static void kirjutaKokkuvõte() throws Exception {
        Map<Jook, Integer> joogidKokku = Baariraamat2.joogidKokku(joogid);
        try (BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Kokkuvõte.txt", true), "UTF-8"))) {
            bf.append("Programm sulgeti: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n");
            bf.append("Baari tulu: " + baaritulu("Salafail.txt") + " eurot." + "\n");
            bf.append("Baari jäänud inimeste krediit: " + krediitiKassas(Baariraamat2.ostjad) + " eurot." + "\n");
            bf.append("Inimsesd, kellel jäi kassasse krediiti: " + "\n");
            List<Ostja> krediidiga = inimesteKrediit(Baariraamat2.ostjad);
            for (Ostja ostja : krediidiga) {
                bf.append(ostja.toString() + "\n");
            }

            bf.append("\n" + "Baarist ostetud joogid:" + "\n");
            for (Map.Entry<Jook, Integer> entry : joogidKokku.entrySet()) {
                bf.append(entry.getKey().getJoogiNimi()).append(" müüdi ").append(String.valueOf(entry.getValue()));
                bf.newLine();
                System.out.println(entry.getKey() + " müüdi " + entry.getValue());
            }
            bf.append("\n");
        }
    }


    // loeb kokku kui palju mingit jooki osteti
    // Argumendid: sõnastik, mis võimaldab jooginime järgi õige Jook obejkti leida
    public static Map<Jook, Integer> joogidKokku(HashMap<String, Jook> joogid) throws Exception {
        File ostud = new File("Salafail.txt");
        File kokkuvõte = new File("Kokkuvõte.txt");
        Map<Jook, Integer> joogidKokku = new HashMap<>();
        try (Scanner sc = new Scanner(ostud, "UTF-8")) {
            while (sc.hasNextLine()) {
                String[] tükid = sc.nextLine().split(", ");
                if (tükid[1].equals("OST")) {
                    int uusKogus = Integer.parseInt(tükid[4]);
                    if (joogidKokku.containsKey(joogid.get(tükid[3])))
                        joogidKokku.put(joogid.get(tükid[3]), joogidKokku.get(joogid.get(tükid[3])) + uusKogus);
                    else
                        joogidKokku.put(joogid.get(tükid[3]), uusKogus);
                }
            }
        }
        return joogidKokku;
    }
}