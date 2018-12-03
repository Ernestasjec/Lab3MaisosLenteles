package Lab3_Jecka_Ernestas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import laborai.demo.*;
import laborai.studijosktu.HashType;
import laborai.studijosktu.MapKTUx;
import laborai.gui.MyException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import laborai.studijosktu.MapKTUOAx;

/**
 * @author eimutis
 */
public class GreitaveikosTyrimas {

    public static final String FINISH_COMMAND = "finishCommand";
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    private final BlockingQueue resultsLogger = new SynchronousQueue();
    private final Semaphore semaphore = new Semaphore(-1);
    private final Timekeeper tk;

    private final String[] TYRIMU_VARDAI = {"add0.75", "add0.25", "rem0.75", "rem0.25", "get0.75", "get0.25"};
    private final int[] TIRIAMI_KIEKIAI = {10000, 20000, 40000, 80000};

    private final MapKTUx<String, Kasininkas> kasAtvaizdis
            = new MapKTUx(new String(), new Kasininkas(), 10, 0.75f, HashType.DIVISION);
    private final MapKTUx<String, Kasininkas> kasAtvaizdis2
            = new MapKTUx(new String(), new Kasininkas(), 10, 0.25f, HashType.DIVISION);
    private final Queue<String> chainsSizes = new LinkedList<>();
    private final MapKTUOAx<String, Kasininkas> kasAtvaizdisMapKTUOA
            = new MapKTUOAx(new String(), new Kasininkas(), 10, 0.75f, HashType.DIVISION);
    private final MapKTUx<String, Kasininkas> kasAtvaizdisMapKTU
            = new MapKTUx(new String(), new Kasininkas(), 10, 0.75f, HashType.DIVISION);
    private final HashMap<String, String> hashMap = new HashMap<String, String>();
    private final MapKTUOAx<String, String> atvaizdisKTUOA
            = new MapKTUOAx(new String(), new String(), 10, 0.75f, HashType.DIVISION);
    private final MapKTUx<String, String> atvaizdisKTU
            = new MapKTUx(new String(), new String(), 10, 0.75f, HashType.DIVISION);
    private final List<String> zodziai = new ArrayList();

    public GreitaveikosTyrimas() {
        semaphore.release();
        tk = new Timekeeper(TIRIAMI_KIEKIAI, resultsLogger, semaphore);
    }

    public void pradetiTyrima() {
        try {
            Skaityti();
            SisteminisTyrimas3();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void Skaityti() throws FileNotFoundException{
        File file = new File("C:\\Users\\Ernestas\\Desktop\\DuomenuStrukturos\\Lab3_MaisosLenteles\\Duomenys\\zodynas.txt"); 
        Scanner sc = new Scanner(file); 
  
        while (sc.hasNextLine()) 
           zodziai.add(sc.nextLine()); 
        
    }
    
    public void SisteminisTyrimas() throws InterruptedException {
        try {
            chainsSizes.add(MESSAGES.getString("msg4"));
            chainsSizes.add("   kiekis      " + TYRIMU_VARDAI[0] + "   " + TYRIMU_VARDAI[1]);
            for (int k : TIRIAMI_KIEKIAI) {
                Kasininkas[] kasArray = KasininkuGeneravimas.generuotiKasininkus(k);
                String[] kasIdArray = KasininkuGeneravimas.gamintiKasIds(k);
                kasAtvaizdis.clear();
                kasAtvaizdis2.clear();
                tk.startAfterPause();
                tk.start();

                for (int i = 0; i < k; i++) {
                    kasAtvaizdis.put(kasIdArray[i], kasArray[i]);
                }
                tk.finish(TYRIMU_VARDAI[0]);

                String str = "   " + k + "          " + kasAtvaizdis.getMaxChainSize();
                for (int i = 0; i < k; i++) {
                    kasAtvaizdis2.put(kasIdArray[i], kasArray[i]);
                }
                tk.finish(TYRIMU_VARDAI[1]);

                str += "         " + kasAtvaizdis2.getMaxChainSize();
                chainsSizes.add(str);
                for (String s : kasIdArray) {
                    kasAtvaizdis.remove(s);
                }
                tk.finish(TYRIMU_VARDAI[2]);

                for (String s : kasIdArray) {
                    kasAtvaizdis2.remove(s);
                }
                tk.finish(TYRIMU_VARDAI[3]);

                for (String s : kasIdArray) {
                    kasAtvaizdis2.get(s);
                }
                tk.finish(TYRIMU_VARDAI[4]);

                for (String s : kasIdArray) {
                    kasAtvaizdis2.get(s);
                }
                tk.finish(TYRIMU_VARDAI[5]);
                tk.seriesFinish();
            }

            StringBuilder sb = new StringBuilder();
            chainsSizes.stream().forEach(p -> sb.append(p).append(System.lineSeparator()));
            tk.logResult(sb.toString());
            tk.logResult(FINISH_COMMAND);
        } catch (MyException e) {
            tk.logResult(e.getMessage());
        }
    }

        public void SisteminisTyrimas2() throws InterruptedException {
        try {
            chainsSizes.add(MESSAGES.getString("msg4"));
            chainsSizes.add("   kiekis      " + TYRIMU_VARDAI[0] + "   " + TYRIMU_VARDAI[1]);
            for (int k : TIRIAMI_KIEKIAI) {
                Kasininkas[] kasArray = KasininkuGeneravimas.generuotiKasininkus(k);
                String[] kasIdArray = KasininkuGeneravimas.gamintiKasIds(k);
                kasAtvaizdisMapKTU.clear();
                kasAtvaizdisMapKTUOA.clear();
                tk.startAfterPause();
                tk.start();

                for (int i = 0; i < k; i++) {
                    kasAtvaizdisMapKTU.put(kasIdArray[i], kasArray[i]);
                }
                tk.finish("KTUput");

                String str = "   " + k + "          " + kasAtvaizdisMapKTU.getMaxChainSize();
                for (int i = 0; i < k; i++) {
                    kasAtvaizdisMapKTUOA.put(kasIdArray[i], kasArray[i]);
                }
                tk.finish("KTUOAput");

                str += "         " + kasAtvaizdisMapKTUOA.getMaxChainSize();
                chainsSizes.add(str);
                for (String s : kasIdArray) {
                    kasAtvaizdisMapKTU.contains(s);
                }
                tk.finish("KTU cont");

                for (String s : kasIdArray) {
                    kasAtvaizdisMapKTUOA.contains(s);
                }
                tk.finish("KTUOA cont");
                
                for (String s : kasIdArray) {
                    kasAtvaizdisMapKTU.remove(s);
                }
                tk.finish("KTU rem");

                for (String s : kasIdArray) {
                    kasAtvaizdisMapKTUOA.remove(s);
                }
                tk.finish("KTUOA rem");
                tk.seriesFinish();
            }

            
            StringBuilder sb = new StringBuilder();
            chainsSizes.stream().forEach(p -> sb.append(p).append(System.lineSeparator()));
            tk.logResult(sb.toString());
            tk.logResult(FINISH_COMMAND);
        } catch (MyException e) {
            tk.logResult(e.getMessage());
        }
    }
    
    public void SisteminisTyrimas3() throws InterruptedException, FileNotFoundException {
        try {
            chainsSizes.add(MESSAGES.getString("msg4"));
            chainsSizes.add("   kiekis      " + TYRIMU_VARDAI[0] + "   " + TYRIMU_VARDAI[1]);
            for (int k : TIRIAMI_KIEKIAI) {
                hashMap.clear();
                atvaizdisKTU.clear();
                atvaizdisKTUOA.clear();
                for (int i = 0; i < k; i++) {
                    atvaizdisKTUOA.put(zodziai.get(i), zodziai.get(i));
                }
                tk.startAfterPause();
                tk.start();
                for (int i = 0; i < k; i++) {
                    hashMap.put(zodziai.get(i), zodziai.get(i));
                }
                tk.finish("hashMapput");
                String str = "   " + k + "          " + kasAtvaizdis.getMaxChainSize();
                for (int i = 0; i < k; i++) {
                    atvaizdisKTU.put(zodziai.get(i), zodziai.get(i));
                }
                tk.finish("KTUput");

                str += "         " + atvaizdisKTU.getMaxChainSize();
                chainsSizes.add(str);
                for (int i = 0; i < k; i++) {
                    hashMap.containsValue(zodziai.get(i));
                }
                tk.finish("hashMapcontVal");

                for (int i = 0; i < k; i++) {
                    atvaizdisKTUOA.contains(zodziai.get(i));
                }
                tk.finish("KTUOAContVal");
                tk.seriesFinish();
            }

            
            StringBuilder sb = new StringBuilder();
            chainsSizes.stream().forEach(p -> sb.append(p).append(System.lineSeparator()));
            tk.logResult(sb.toString());
            tk.logResult(FINISH_COMMAND);
        } catch (MyException e) {
            tk.logResult(e.getMessage());
        }
    }
        
    public BlockingQueue<String> getResultsLogger() {
        return resultsLogger;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
