import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BloomFilterRan {
    private BitSet filter;
    private int setSize;
    private int bitsPerElement;
    private int numHashes;
    private int dataSize;
    private Random random;
    private int primeP;
    private int[] hashCoeffA;
    private int[] hashCoeffB;

    public BloomFilterRan(int setSize, int bitsPerElement) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
        this.numHashes = (int) Math.ceil((Math.log(2) * bitsPerElement * setSize) / setSize);
        this.filter = new BitSet(setSize * bitsPerElement);
        this.dataSize = 0;
        this.random = new Random();
        this.primeP = generatePrime(bitsPerElement * setSize + 1); // Slightly larger than M
        this.hashCoeffA = new int[numHashes];
        this.hashCoeffB = new int[numHashes];

        for (int i = 0; i < numHashes; i++) {
            hashCoeffA[i] = random.nextInt(primeP);
            hashCoeffB[i] = random.nextInt(primeP);
        }
    }

    public int generatePrime(int min) {
        int prime = min;
        while (!isPrime(prime)) {
            prime++;
        }
        return prime;
    }

    public boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
    public int randomHashFunction(String s, int hashIndex) {
    	int xvalue = s.hashCode();
        long hash = (hashCoeffA[hashIndex] * xvalue + hashCoeffB[hashIndex]) % primeP;
        return (int) Math.abs(hash);
    }

    public void add(String s) {
    	s = s.toLowerCase();
        for (int i = 0; i < numHashes; i++) {
            int index = randomHashFunction(s, i);
            filter.set(index, true);
        }
        dataSize++;
    }

    public boolean appears(String s) {
    	s = s.toLowerCase();
        for (int i = 0; i < numHashes; i++) {
            int index = randomHashFunction(s, i);
            if (!filter.get(index)) {
                return false;
            }
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
        BloomFilterRan bloomFilter = new BloomFilterRan(10000, 10);
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
