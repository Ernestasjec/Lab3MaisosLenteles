package Lab3_Jecka_Ernestas;

import laborai.studijosktu.Ks;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import laborai.studijosktu.HashType;
import laborai.studijosktu.MapKTUOA;
import laborai.studijosktu.MapKTUOAx;
import laborai.studijosktu.MapKTUx;

/*
 * Aibės testavimas be Swing'o
 *
 */
public class TikrintiKasininka2 {

    public static void main(String[] args) throws CloneNotSupportedException {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus
        aibėsTestas();
    }

    public static void aibėsTestas() throws CloneNotSupportedException {
        Kasininkas k1 = new Kasininkas("Jonas","Kumanas","1992-12-01",383092400,'V');
        Kasininkas k2 = new Kasininkas("Petras","Lumanas","1986-05-02",343092400,'V');
        Kasininkas k3 = new Kasininkas("Angelė","Bagienė","1990-12-01",333294400,'M');
        Kasininkas k4 = new Kasininkas();
        Kasininkas k5 = new Kasininkas();
        Kasininkas k6 = new Kasininkas();
        Kasininkas k7 = new Kasininkas();
        Kasininkas k8 = new Kasininkas();
        Kasininkas k9 = new Kasininkas();
        k4.parse("Angelė Petraitienė 1986-10-02 33444400 M");
        k5.parse("Jonas Petraitis 1984-09-24 33442500 V");
        k6.parse("Kasparas Domeika 1976-07-25 33484400 V");
        k7.parse("Kasparas Andreika 1976-07-25 35444400 V");
        k8.parse("Ignas Andreika 1980-07-25 33444500 V");
        k9.parse("Tomas Kubiliauskas 1980-07-25 35444400 V");
        Kasininkas k10 = new Kasininkas.Builder().buildRandom();

            // Raktų masyvas
        String[] kasId = {"KS354", "KS457", "KS255", "KS171", "KS105", "KS457", "KS107", "KS547", "KS887"};
        int id = 0;
        MapKTUOAx<String, Kasininkas> atvaizdis
                = new MapKTUOAx(new String(), new Kasininkas(), HashType.DIVISION);
        // Reikšmių masyvas
        Kasininkas[] kas = {k1, k2, k3, k4, k5, k6, k7, k8};
        for (Kasininkas a : kas) {
            atvaizdis.put(kasId[id++], a);
        }
        atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
        //Ks.ounn(atvaizdis);
        
        
//        atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
//        Ks.oun("Ar egzistuoja pora atvaizdyje?");
//        Ks.oun(atvaizdis.contains(kasId[5]));
//        Ks.oun(atvaizdis.contains(kasId[8]));
//        Ks.oun("Šalinamos reikšmės");
        Ks.oun(atvaizdis.remove("KS105"));
        Ks.oun(atvaizdis.contains("KS457"));
        atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
//        Ks.oun(atvaizdis.remove(kasId[7]));
//        atvaizdis.println("Porų išsidėstymas atvaizdyje pagal raktus");
//        Ks.oun("Atliekame porų paiešką atvaizdyje:");
//        Ks.oun(atvaizdis.get(kasId[2]));
//        Ks.oun(atvaizdis.get(kasId[7]));
//        Ks.oun("Išspausdiname atvaizdžio poras String eilute:");
//        Ks.ounn(atvaizdis);
    }

    //Konsoliniame režime
    private static void greitaveikosTestas() {
        System.out.println("Greitaveikos tyrimas:\n");
        GreitaveikosTyrimas gt = new GreitaveikosTyrimas();
        //Šioje gijoje atliekamas greitaveikos tyrimas
        new Thread(() -> gt.pradetiTyrima(),
                "Greitaveikos_tyrimo_gija").start();
        try {
            String result;
            while (!(result = gt.getResultsLogger().take())
                    .equals(GreitaveikosTyrimas.FINISH_COMMAND)) {
                System.out.println(result);
                gt.getSemaphore().release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        gt.getSemaphore().release();
    }
}
