/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laborai.studijosktu;

import java.util.Arrays;

/**
 *
 * @author Ernestas
 */
public class MapKTUOA<K, V> implements MapADTp<K, V> {

    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final HashType DEFAULT_HASH_TYPE = HashType.DIVISION;
    
    // Maišos lentelė
    protected Entry<K, V>[] table;
    // Lentelėje esančių raktas-reikšmė porų kiekis
    protected int size = 0;
    // Apkrovimo faktorius
    protected float loadFactor;
    // Maišos metodas
    protected HashType ht;
    //--------------------------------------------------------------------------
    //  Maišos lentelės įvertinimo parametrai
    //--------------------------------------------------------------------------
    // Maksimalus suformuotos maišos lentelės grandinėlės ilgis
    protected int maxChainSize = 0;
    // Permaišymų kiekis
    protected int rehashesCounter = 0;
    // Paskutinės patalpintos poros grandinėlės indeksas maišos lentelėje
    protected int lastUpdatedChain = 0;
    // Lentelės grandinėlių skaičius     
    protected int chainsCounter = 0;
    // Einamas poros indeksas maišos lentelėje
    protected int index = 0;
    
    
    public MapKTUOA() {
        this(DEFAULT_HASH_TYPE);
    }

    public MapKTUOA(HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, ht);
    }

    public MapKTUOA(int initialCapacity, HashType ht) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    public MapKTUOA(float loadFactor, HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor, ht);
    }

    public MapKTUOA(int initialCapacity, float loadFactor, HashType ht) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.table = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.ht = ht;
    }
    
    @Override
    public int getMaxChainSize() {
        return 1;
    }

    @Override
    public int getRehashesCounter() {
        return this.rehashesCounter;
    }

    @Override
    public int getTableCapacity() {
        return table.length;
    }

    @Override
    public int getLastUpdatedChain() {
        return this.lastUpdatedChain;
    }

    @Override
    public int getChainsCounter() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
        lastUpdatedChain = 0;
        maxChainSize = 0;
        rehashesCounter = 0;
        chainsCounter = 0;
    }

    @Override
    public String[][] toArray() {
        String[][] result = new String[table.length][];
        int count = 0;
        for (Entry<K, V> n : table) {
            String[] list = new String[1];
                list[0] = n.toString();
            result[count] = list;
            count++;
        }
        return result;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(Key key, Value value)");
        }
        int entryIndex = this.findPosition(key);     
            table[entryIndex] = new Entry(key, value);
            this.lastUpdatedChain = entryIndex;
            size++;
        if (size >= table.length * this.loadFactor) {
            rehash();
        }
        return value;
    }

    private void rehash() {
        MapKTUOA mapKTUOA
                = new MapKTUOA(table.length * 2, loadFactor, ht);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                mapKTUOA.put(table[i].key, table[i].value);
            }
        }
        table = mapKTUOA.table;
        maxChainSize = mapKTUOA.maxChainSize;
        chainsCounter = mapKTUOA.chainsCounter;
        rehashesCounter++;
    }
    
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(Key key)");
        }
        int index = findIfKeyExist(key);
        if (table[index] == null || index == -1) {
            return null;
        }
        return table[index].value;
    }
    private int findPosition(K key){
        int index = hash(key, ht);
        int indexO = index;
        int i = 0;
        for (int j = 0; j < table.length; j++) {
            if (table[index] == null|| table[index].key.equals(key)) {
                return index;
            }
            i++; 
            index = (indexO + i) % table.length;
        }
        return -1;
    }
    
    private int findIfKeyExist(K key){
        int index = hash(key, ht);
        int indexO = index;
        int i = 0;
        for (int j = 0; j < table.length; j++) {
            if (table[index].key.equals(key)) {
                return index;
            }
            i++; 
            index = (indexO + i) % table.length;
        }
        return -1;
    }
    
    private int hash(K key, HashType hashType) {
        int h = key.hashCode();
        switch (hashType) {
            case DIVISION:
                System.out.println("HasCode: " + Math.abs(h) % table.length);
                return Math.abs(h) % table.length;
            case MULTIPLICATION:
                double k = (Math.sqrt(5) - 1) / 2;
                return (int) (((k * Math.abs(h)) % 1) * table.length);
            case JCF7:
                h ^= (h >>> 20) ^ (h >>> 12);
                h = h ^ (h >>> 7) ^ (h >>> 4);
                return h & (table.length - 1);
            case JCF8:
                h = h ^ (h >>> 16);
                return h & (table.length - 1);
            default:
                return Math.abs(h) % table.length;
        }
    }
    
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in remove(Key key)");
        }
        V value = null;
        int removableIndex = this.findIfKeyExist(key);
        if (removableIndex != -1 && table[removableIndex] != null) {
           value = table[removableIndex].value;
           table[removableIndex] = null;  
        }
        return value;
    }

    @Override
    public boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in contains(Key key)");
        }
        int indexOfKey = this.findIfKeyExist(key);
        return table[indexOfKey] != null;
    }
    
    public boolean containsValue(V value) {
        if (value == null) {
            throw new IllegalArgumentException("Key is null in contains(Key key)");
        }
        for (int i = 0; i < table.length; i++) {
            if (table[i].value.equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    class Entry<K, V>{
        //Raktas
        K key;
        //Reiksme
        V value;
        
        Entry(){
        }
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String toString(){
            return key + " = " + value;
        }
    }
}
