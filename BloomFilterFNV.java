import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class BloomFilterFNV {
    private BitSet filter;
    private int setSize; 
    private int bitsPerElement;
    private int numHashes;
    private int dataSize;

    public BloomFilterFNV(int setSize, int bitsPerElement) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
        this.numHashes = (int) Math.ceil((Math.log(2) * bitsPerElement * setSize) / setSize);
        this.filter = new BitSet(setSize * bitsPerElement);
        this.dataSize = 0;
    }
    
    // This needs to be k hash functions 
    // FNV-1a 64-bit hash function
    public long fnvHash(String s) {
        long hash = 0xcbf29ce484222325L;
        for (char c : s.toLowerCase().toCharArray()) {
            hash ^= c;
            // Update the hash function to include 2^64
            hash *= 0x100000001b3L;
        }
        return hash;
    }

    public void add(String s) {
        long hash = fnvHash(s);
        for (int i = 0; i < numHashes; i++) {
            int index = (int) (Math.abs(hash) % (setSize * bitsPerElement));
            filter.set(index, true);
            // Update the code for next hash function 
            // Update to include k prime numbers. Store k prime numbers in a list 
            // and then instead of >>32 multiple with the prime numbers to generate k hash functions
            hash = hash >> 32; // Next hash
        }
        dataSize++;
    }

    public boolean appears(String s) {
        long hash = fnvHash(s);
        for (int i = 0; i < numHashes; i++) {
            int index = (int) (Math.abs(hash) % (setSize * bitsPerElement));
            if (!filter.get(index)) {
                return false;
            }
            hash = hash >> 32; // Next hash
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
        BloomFilterFNV bloomFilter = new BloomFilterFNV(10000, 10);
        Set<String> words = new HashSet<>();
        words.add("apple");
        words.add("banana");
        words.add("cherry");

        for (String word : words) {
            bloomFilter.add(word);
        }

        System.out.println("Bloom Filter Size: " + bloomFilter.filterSize());
        System.out.println("Number of Hash Functions: " + bloomFilter.numHashes());
        System.out.println("Data Size: " + bloomFilter.dataSize());

        System.out.println("Word 'apple' appears: " + bloomFilter.appears("apple"));
        System.out.println("Word 'orange' appears: " + bloomFilter.appears("orange"));
    }
}
