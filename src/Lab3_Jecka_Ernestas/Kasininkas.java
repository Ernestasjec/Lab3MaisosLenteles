/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab3_Jecka_Ernestas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import laborai.studijosktu.KTUable;
import laborai.studijosktu.Ks;

/**
 *
 * @author Ernestas
 */
public class Kasininkas implements KTUable {
    
    final static private int priimtinųMetųRiba = LocalDate.now().getYear() - 64;
    final static private int esamiMetai  = LocalDate.now().getYear();
    SimpleDateFormat formatuoti = new SimpleDateFormat("yyyy-MM-dd");
    
    private String pavarde = "";
    private String vardas = "";
    private Date gimimoData;
    private int asmensKodas = 00;
    private char lytis = '0';
    
    public Kasininkas() {
    }
    
    public Kasininkas(String vardas, String pavarde, String gimimoData, int asmensKodas, char lytis){
        try{
            this.pavarde = pavarde;
            this.vardas = vardas;
            this.gimimoData = formatuoti.parse(gimimoData);
            this.asmensKodas = asmensKodas;
            this.lytis = lytis;
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
    
    public Kasininkas(String dataString) {
        this.parse(dataString);
    }

    public Kasininkas(Builder builder){
        this.pavarde = builder.pavarde;
        this.vardas = builder.vardas;
        this.gimimoData = builder.gimimoData;
        this.asmensKodas = builder.asmensKodas;
        this.lytis = builder.lytis;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pavarde, vardas, gimimoData, asmensKodas, lytis);
    }
    
        @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kasininkas other = (Kasininkas) obj;
        if (!Objects.equals(this.pavarde, other.pavarde)) {
            return false;
        }
        if (!Objects.equals(this.gimimoData, other.gimimoData)) {
            return false;
        }
        if (this.asmensKodas != other.asmensKodas) {
            return false;
        }
        if (this.lytis != other.lytis) {
            return false;
        }

        return true;
    }
    
    @Override
    public Kasininkas create(String duomenuEilute) {
         Kasininkas a = new Kasininkas();
        a.parse(duomenuEilute);
        return a;
    }
    
        @Override
    public String toString(){  // surenkama visa reikalinga informacija
        return String.format("%-10s %-10s %10s %7d %2c", pavarde, vardas, 
                formatuoti.format(gimimoData), asmensKodas, lytis);
    };    
   
   public String getPavarde(){
        return pavarde;
    }
    public String getVardas(){
        return vardas;
    }
    public Date getGimimoData(){
        return gimimoData;
    }
    public int getAsmensKodas(){
        return asmensKodas;
    }
    public int getLytis(){
        return lytis;
    }
    
    public int getAmžius(){
        Date Šiandien = new Date();
        long skirtumasMiliSekundemis = Math.abs(Šiandien.getTime() - this.gimimoData.getTime());
        long skirtumasDienomis = TimeUnit.DAYS.convert(skirtumasMiliSekundemis, TimeUnit.MILLISECONDS);
        double skirtumasMetais = skirtumasDienomis / 365.25;
        return (int)skirtumasMetais;
    }

    @Override
    public String validate() {
        String klaidosTipas = "";
        if (gimimoData.getYear() < priimtinųMetųRiba || gimimoData.getYear() > esamiMetai - 18)
           klaidosTipas = "Kasininkas negali buti jaunesnis nei 18 ir vyresnis nei 64 metai";
        return klaidosTipas;
    }

    @Override
    public void parse(String duomenuEilute) {
            try {   // ed - tai elementarūs duomenys, atskirti tarpais
                SimpleDateFormat formatuoti = new SimpleDateFormat("yyyy-MM-dd");
                Scanner ed = new Scanner(duomenuEilute);
                vardas = ed.next();
                pavarde   = ed.next();
                gimimoData = formatuoti.parse(ed.next());
                asmensKodas = ed.nextInt();
                lytis = ed.next().charAt(0);
        } catch (InputMismatchException  e) {
            Ks.ern("Blogas duomenų formatas apie kasininka -> " + duomenuEilute);
        } catch (NoSuchElementException e) {
            Ks.ern("Trūksta duomenų apie kasininka -> " + duomenuEilute);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }
    
    public final static Comparator<Kasininkas> pagalVardusPavardes =
              new Comparator<Kasininkas>() {
       @Override
       public int compare(Kasininkas k1, Kasininkas k2) {
          int cmp = k1.getVardas().compareTo(k2.getVardas());
          if(cmp != 0) return cmp;
          return k1.getPavarde().compareTo(k2.getPavarde());
       }
    };
    public final static Comparator pagalAmžių = new Comparator() {
       // sarankiškai priderinkite prie generic interfeiso ir Lambda funkcijų
       @Override
       public int compare(Object o1, Object o2) {
          double k1 = ((Kasininkas) o1).getAmžius();
          double k2 = ((Kasininkas) o2).getAmžius();
          // didėjanti tvarka, pradedant nuo mažiausios
          if(k1<k2) return -1;
          if(k1>k2) return 1;
          return 0;
       }
    };
    
    public static class Builder {
        private final static Random RANDOM = new Random(1949);
        private final static String[] datos = {"1958-05-04", "1968-10-12", "1980-05-16", "1995-05-28", "1997-09-01", 
            "1955-01-04", "1977-02-15", "1977-11-15"};  // Datos
        private final static String[][] Vardai = { // galimų vardų ir pavardžių masyvas
          {"Jonas", "Antanaitis", "Petraitis", "Kumanaitis", "Lenkaitis", "V"},
          {"Antanas", "Rigauskas", "Kirobavicius", "Inkilaitis", "Morkavicus", "V"},
          {"Kazys", "Petraitonis", "Ignavicius", "Mondeonaitis", "Grigaitis", "V"},
          {"Ignas", "Jablonskis", "Koranovas", "Ignanovasi", "Bonavičius", "V"},
          {"Inga", "Kamoriene", "Lumaniene", "Lenkiene", "Ignaciene", "M"},
          {"Janina", "Komanoviene", "Lambardiene", "Lomanoviene", "Lodarova", "M"}
       };

        private String pavarde = "";
        private String vardas = "";
        private Date gimimoData = new Date();
        private int asmensKodas = 0;
        private char lytis = ' ';

        public Kasininkas build() {
            return new Kasininkas(this);
        }

        public Kasininkas buildRandom() {
            int vard = RANDOM.nextInt(Vardai.length);        // Vardo indeksas
            int pav = RANDOM.nextInt(Vardai[vard].length - 2) + 1;// Pavardės inteksas    
            //Konstrujamas atsitiktinis kasininkas
            return new Kasininkas(Vardai[vard][0],
                    Vardai[vard][pav],
                    datos[RANDOM.nextInt(datos.length - 1)],//datos iš masyvo
                    30000000 + RANDOM.nextInt(9999999),// asmens kodas
                    Vardai[vard][Vardai[vard].length -1].charAt(0));// lytis
        }

        public Builder pavarde(String pavarde) {
            this.pavarde = pavarde;
            return this;
        }

        public Builder gimimoData(Date gimimoData) {
            this.gimimoData = gimimoData;
            return this;
        }

        public Builder asmensKodas(int asmensKodas) {
            this.asmensKodas = asmensKodas;
            return this;
        }

        public Builder lytis(char lytis) {
            this.lytis = lytis;
            return this;
        }
   }
}
