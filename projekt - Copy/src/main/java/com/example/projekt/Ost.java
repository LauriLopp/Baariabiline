package com.example.projekt;

import javafx.scene.layout.VBox;

public class Ost {
    // Väljad kassas tehtud tehingu andmete kogumiseks

    private String ostja;
    private String krediidimuut;
    private String jook;
    private String kogus;

    // Konstruktor
    public Ost(String ostja, String krediidimuut, String jook, String kogus) {
        this.ostja = ostja;
        this.jook = jook;
        this.kogus = kogus;
        this.krediidimuut = krediidimuut;
    }

    /*
    // toString meetod ostu kuvamiseks
    public String toString() {
        return ostja + " " + jook + " " + kogus;
    }

     */

    @Override
    public String toString() {
        return "Ost{" +
                "ostja='" + ostja + '\'' +
                ", krediidimuut='" + krediidimuut + '\'' +
                ", jook='" + jook + '\'' +
                ", kogus='" + kogus + '\'' +
                '}';
    }

    // see hakkab valima, millist meetodit kasutada
    // Meetod kontrollib, kas sisestati tühi sõne või kasutatav suurus
    // vastavalt sisestatud väärtustele valib meetodi, mida rakendada
    // väljastab teate, kui sisestatud andmetega pole midagi ratsionaalset teha
    public void osta(VBox vBoxLogi) throws Exception {
        double krediidimuut = 0;
        int kogus = 0;

        try {
            krediidimuut = Double.parseDouble(this.krediidimuut);
        } catch (NumberFormatException e) {
            if (!this.krediidimuut.equals(""))
                throw new VigaseSisendiErind("Krediit peab ole arv!");
        }
        try {
            kogus = Integer.parseInt(this.kogus);
        } catch (NumberFormatException e) {
            if (!this.kogus.equals(""))
                throw new VigaseSisendiErind("Kogus peab olema täisarv!");
        }

        if (!ostja.equals("") && this.krediidimuut.equals("") && jook.equals("vali") && this.kogus.equals("")) {
            realiseeriost(ostja, vBoxLogi);
        } else if (!ostja.equals("") && !this.krediidimuut.equals("") && jook.equals("vali") && this.kogus.equals("")) {
            realiseeriost(ostja, krediidimuut, vBoxLogi);
        } else if (!ostja.equals("") && this.krediidimuut.equals("") && !jook.equals("vali") && !this.kogus.equals("")) {
            Jook jook = Baariraamat2.joogid.get(this.jook); // hetkel sõnastik tühi, täitub vist main'i rakendamisel
            realiseeriost(ostja, jook, kogus, vBoxLogi);

        } else if (ostja.equals("") && this.krediidimuut.equals("") && !jook.equals("vali") && !this.kogus.equals("")) {
            Jook jook = Baariraamat2.joogid.get(this.jook); // hetkel sõnastik tühi, täitub vist main'i rakendamisel
            realiseeriost(jook, kogus, vBoxLogi);

        } else if (!ostja.equals("") && !this.krediidimuut.equals("") && !jook.equals("vali") && !this.kogus.equals("")) {
            Jook jook = Baariraamat2.joogid.get(this.jook); // hetkel sõnastik tühi, täitub vist main'i rakendamisel
            realiseeriost(ostja, krediidimuut, jook, kogus, vBoxLogi);

        } else if (ostja.equals("") && !this.krediidimuut.equals("") && jook.equals("vali") && this.kogus.equals("")) {
            throw new VigaseSisendiErind("Sisestasid ainult krediidimuutuse.");

        } else if (ostja.equals("") && this.krediidimuut.equals("") && jook.equals("vali") && !this.kogus.equals("")) {
            throw new VigaseSisendiErind("Sisestasid ainult joogi koguse.");

        }
    }




    // üledefineeritud meetodid ostuprotsessi läbiviimiseks


    // Argumendid: kasutaja sisestatud ostja nimi
    // rakendatakse, kui sisestatakse vaid nimi
    // väljastab ainult krediidi, ei tagasta midagi
    private static void realiseeriost(String ostjanimi, VBox vBoxLogi) {
        Ostja.kontrolliOstjat(ostjanimi, Baariraamat2.ostjad, vBoxLogi);

    }

    // kontrollib kas arve on avatud
    // Argumendid: kasutaja sisestatud ostjanimi, krediidi muutuse suurus
    // rakendub, kui sisestatakse ostja nimi ja krediidimuutuse suurus
    // suurendab krediiti
    // väljastab uue krediidi
    private static void realiseeriost(String ostjanimi, double krediit, VBox vBoxLogi) throws Exception {
        Ostja ostja = Ostja.kontrolliOstjat(ostjanimi, Baariraamat2.ostjad, vBoxLogi);
        ostja.muudaKrediiti(krediit, vBoxLogi);
    }

    // kontrollib, kas ostjal on krediiti
    // Argumendid: ostja nimi, sisestatud joogi nimi, jookide kogus
    // rakendub, kui sisestatakse ostja nimi, joogi nimi, jookide kogus
    // kui on, siis ostab selle eest
    // kui ei ole, siis väljastab teate
    private static void realiseeriost(String ostjanimi, Jook jook, int kogus, VBox vBoxLogi) throws Exception {
        Ostja ostja = Ostja.kontrolliOstjat(ostjanimi, Baariraamat2.ostjad, vBoxLogi);
        double kulu = jook.getJoogiHind() * kogus;
        if (kulu <= ostja.getKrediit()) {
            ostja.muudaKrediiti(-kulu, vBoxLogi);
            jook.lisaOst(ostjanimi, kogus, vBoxLogi);
            System.out.println(ostja.getNimi() + " ostis " + kogus + " " + jook.getJoogiNimi());
        } else
            throw new VigaseSisendiErind("Ostu jaoks pole piisavalt krediiti!");
    }

    // lisab ostu
    // Argumendid: jookide nimi, jookide kogus
    // rakendub kui sisestatakse vaid joogi nimi ja kogus
    private static void realiseeriost(Jook jook, int kogus, VBox vBoxLogi) throws Exception {
        jook.lisaOst(kogus, vBoxLogi);
    }

    // kontrollib ostja olemasolu
    // Lisab ostjale krediiti
    // kontrollib, kas ostjal on ostuks piisavalt krediiti
    // kui on, siis sooritab ostu ja kannab ostu faili
    // kui ei ole, siis väljastab teate
    // Argumendid: ostja nimi, sisestatud krediit, jooginimi, jookide kogus
    private static void realiseeriost(String ostjanimi, double krediit, Jook jook, int kogus, VBox vBoxLogi) throws Exception {
        Ostja ostja = Ostja.kontrolliOstjat(ostjanimi, Baariraamat2.ostjad, vBoxLogi);
        double kulu = jook.getJoogiHind() * kogus;
        ostja.muudaKrediiti(krediit, vBoxLogi);
        if (kulu <= ostja.getKrediit()) {
            ostja.muudaKrediiti(-kulu, vBoxLogi);
            jook.lisaOst(ostjanimi, kogus, vBoxLogi);
            System.out.println(ostja.getNimi() + " ostis " + kogus + " " + jook.getJoogiNimi());
        } else System.out.println("Ostu jaoks pole piisavalt krediiti!");
    }



}
