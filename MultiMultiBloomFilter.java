import java.util.*;

public class MultiMultiBloomFilter {
    private int setSize;
    private int bitsPerElement;
    private int numHashes;
    private int dataSize;
    private List<BitSet> filterArrays;
    private Random random;
    private int primeP;
    private int[] hashCoeffA;
    private int[] hashCoeffB;
    private int[] hashCoeffC;

    public MultiMultiBloomFilter(int setSize, int bitsPerElement, int numArrays) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
        this.numHashes = (int) Math.ceil((Math.log(2) * bitsPerElement * setSize) / setSize);
        this.dataSize = 0;
        this.filterArrays = new ArrayList<>(numArrays);
        this.random = new Random();
        this.primeP = generatePrime(bitsPerElement + 1); // Slightly larger than bitsPerElement
        this.hashCoeffA = new int[numHashes];
        this.hashCoeffB = new int[numHashes];
        this.hashCoeffC = new int[numHashes];

        for (int i = 0; i < numArrays; i++) {
            filterArrays.add(new BitSet(setSize));
        }

        for (int i = 0; i < numHashes; i++) {
            hashCoeffA[i] = random.nextInt(primeP);
            hashCoeffB[i] = random.nextInt(primeP);
            hashCoeffC[i] = random.nextInt(primeP);
        }
    }

    private int generatePrime(int min) {
        int prime = min;
        while (!isPrime(prime)) {
            prime++;
        }
        return prime;
    }

    private boolean isPrime(int n) {
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

    private int randomHashFunction(String s, int hashIndex) {
    	// Update value of x
        int x = s.hashCode();
        int hash = (hashCoeffA[hashIndex] * x * x + hashCoeffB[hashIndex] * x + hashCoeffC[hashIndex]) % primeP;
        return Math.abs(hash % setSize);
    }

    public void add(String s) {
        for (int i = 0; i < numHashes; i++) {
            int index = randomHashFunction(s, i);
            for (int j = 0; j < filterArrays.size(); j++) {
                filterArrays.get(j).set(index, true);
            }
        }
        dataSize++;
    }

    public boolean appears(String s) {
        for (int i = 0; i < numHashes; i++) {
            int index = randomHashFunction(s, i);
            for (int j = 0; j < filterArrays.size(); j++) {
                if (!filterArrays.get(j).get(index)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int filterSize() {
        return setSize * bitsPerElement * filterArrays.size();
    }

    public int dataSize() {
        return dataSize;
    }

    public int numHashes() {
        return numHashes;
    }

    public boolean getBit(int j, int arrayIndex) {
        return filterArrays.get(arrayIndex).get(j);
    }

    public static void main(String[] args) {
    	// Number of hash functions = number of filters
    	// Update code to have k filters instead of one n*k size filter  
        MultiMultiBloomFilter bloomFilter = new MultiMultiBloomFilter(10000, 10, 3);
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
