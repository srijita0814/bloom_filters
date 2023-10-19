import java.util.BitSet;
import java.util.HashSet;
//import java.util.Random;
import java.util.Set;

public class BloomFilterFNV {
    private BitSet filter;
    private int setSize; 
    private int bitsPerElement;
    private int numHashes;
    private int dataSize;
    private int filterSize;

    public BloomFilterFNV(int setSize, int bitsPerElement) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
        this.numHashes = (int) Math.ceil((Math.log(2) * bitsPerElement * setSize) / setSize);
        this.filter = new BitSet(setSize * bitsPerElement);
        this.dataSize = 0;
        this.filterSize = (int) setSize * bitsPerElement;
    }
    
    // FNV 64-bit hash function
    public long fnvHash(String s) {
        long FNV64INIT = 95981039346656037L;
    	long FNV64PRIME = 109951168211L;
    	long hash = FNV64INIT;
        for ( int c=0 ; c<s.length(); c++) {
            hash ^= s.charAt(c);
            hash = (hash * FNV64PRIME); 
        }
        return hash;
    }

    public void add(String s) {
    	s = s.toLowerCase(); // to make the code case insensitive
        long hash = fnvHash(s);
        for (int k = 0; k < numHashes; k++) {
            int index = (int) (Math.abs(hash) % filterSize);
            filter.set(index, true);
            // Next hash function
            hash = hash + k;
            //System.out.println("Hash: " + hash);
        }
        dataSize++;
    }

    public boolean appears(String s) {
    	s = s.toLowerCase();
        long hash = fnvHash(s);
        for (int k = 0; k < numHashes; k++) {
            int index = (int) (Math.abs(hash) % filterSize);
            if (!filter.get(index)) {
                return false;
            }
            hash = hash + (k);
        }
        return true;
    }

    public int filterSize() {
        return setSize * bitsPerElement;
    }

    public int dataSize() {
        return dataSize;
    }

    public int numHashes() {
        return numHashes;
    }

    public boolean getBit(int j) {
        return filter.get(j);
    }

    public static void main(String[] args) {
        BloomFilterFNV bloomFilter = new BloomFilterFNV(10,8);
        Set<String> words = new HashSet<>();
        words.add("apple");
        words.add("banana");
        words.add("cherry");
        words.add("Mango");
        
        for (String word : words) {
            bloomFilter.add(word);
        }

        System.out.println("Bloom Filter Size: " + bloomFilter.filterSize());
        System.out.println("Number of Hash Functions: " + bloomFilter.numHashes());
        System.out.println("Data Size: " + bloomFilter.dataSize());

        System.out.println("Word 'apple' appears: " + bloomFilter.appears("apple"));
        System.out.println("Word 'orange' appears: " + bloomFilter.appears("orange"));
        System.out.println("Word 'mango' appears: " + bloomFilter.appears("Mango"));
    }
}
