import java.util.*;

public class MultiMultiBloomFilter {
    private int setSize;
    private int bitsPerElement;
    private int numHashes;
    private int dataSize;
    private List<BitSet> filterArrays;
    private int numFilters;
    private int filterSize;

    public MultiMultiBloomFilter(int setSize, int bitsPerElement) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
        this.numHashes = (int) Math.ceil((Math.log(2) * bitsPerElement * setSize) / setSize);
        this.dataSize = 0;
        this.numFilters = (int) Math.ceil((Math.log(2) * bitsPerElement * setSize) / setSize);
        this.filterArrays = new ArrayList<>(numFilters);
        this.filterSize = (int) setSize * bitsPerElement;

        for (int i = 0; i < numFilters; i++) {
            filterArrays.add(new BitSet(setSize));
        }

    }
    
 // FNV 64-bit hash function
    public long fnvHash(String s) {
        long FNV64INIT = 95981039346656037L;
    	long FNV64PRIME = 109951168211L;
    	long hash = FNV64INIT;
        for ( int c=0 ; c<s.length(); c++) {
            hash ^= s.charAt(c);
            hash = (hash * FNV64PRIME); // % (1L << 64)
        }
        //System.out.println("Hash:: " + hash);
        return hash;
    }



    public void add(String s) {
    	s = s.toLowerCase(); // to make the code case insensitive
    	long hash = fnvHash(s);
        for (int i = 0; i < numHashes; i++) {
        	int index = (int) (Math.abs(hash) % filterSize);
            //int index = randomHashFunction(s, i);
//            for (int j = 0; j < filterArrays.size(); j++) {
//                filterArrays.get(j).set(index, true);
//            }
            filterArrays.get(i).set(index, true);
            hash = hash + (i);
        }
        dataSize++;
    }

    public boolean appears(String s) {
    	s = s.toLowerCase(); // to make the code case insensitive
    	long hash = fnvHash(s);
        for (int i = 0; i < numHashes; i++) {
        	int index = (int) (Math.abs(hash) % filterSize);
            //int index = randomHashFunction(s, i);
//            for (int j = 0; j < filterArrays.size(); j++) {
//                if (!filterArrays.get(j).get(index)) {
//                    return false;
//                }
//            }
            if (!filterArrays.get(i).get(index)) {
	            return false;
	        }
            hash = hash + (i);
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
    	MultiMultiBloomFilter bloomFilter = new MultiMultiBloomFilter(10000, 10);
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
