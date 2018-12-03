package Lab3_Jecka_Ernestas;

import laborai.gui.MyException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class KasininkuGeneravimas {
    
    private static final String ID_CODE = "KS";      //  ***** nauja
    private static int Numeris = 10000;  
    private static Kasininkas[] kasininkai;
    private String[] raktai;
    private static int kiekis = 0, idKiekis = 0;

    
        public static Kasininkas[] generuotiKasininkus(int kiekis) {
        Kasininkas[] kasininkai = IntStream.range(0, kiekis)
                .mapToObj(i -> new Kasininkas.Builder().buildRandom())
                .toArray(Kasininkas[]::new);
        Collections.shuffle(Arrays.asList(kasininkai));
        return kasininkai;
    }

    public static String[] gamintiKasIds(int kiekis) {
        String[] raktai = IntStream.range(0, kiekis)
                .mapToObj(i -> ID_CODE + (Numeris++))
                .toArray(String[]::new);
        Collections.shuffle(Arrays.asList(raktai));
        return raktai;
    }
    
    public Kasininkas[] generuotiIrPateiktiKasininku(int aibesDydis,
            int aibesImtis) throws MyException {
        if (aibesImtis > aibesDydis) {
            aibesImtis = aibesDydis;
        }
        kasininkai = generuotiKasininkus(aibesDydis);
        raktai = gamintiKasIds(aibesDydis);
        this.kiekis = aibesImtis;
        return Arrays.copyOf(kasininkai, aibesImtis);
    }

    // Imamas po vienas elementas iš sugeneruoto masyvo. Kai elementai baigiasi sugeneruojama
    // nuosava situacija ir išmetamas pranešimas.
    public Kasininkas imtiKasininka() {
        if (kasininkai == null) {
            throw new MyException("counterNotGenerated");
        }
        if (kiekis < kasininkai.length) {
            return kasininkai[kiekis++];
        } else {
            throw new MyException("allSetStoredToMap");
        }
    }

    public String gautiIsBazesKasininkoId() {
        if (raktai == null) {
            throw new MyException("carsIdsNotGenerated");
        }
        if (idKiekis >= raktai.length) {
            idKiekis = 0;
        }
        return raktai[idKiekis++];
    }
}
