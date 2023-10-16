//import java.util.*;

public class Statistics {

	public static void main(String[] args) {
		int setSize = 100000;
        int bitsPerElement = 10;
        
		BloomFilterFNV fnvFilter1 = new BloomFilterFNV(setSize, bitsPerElement);
		BloomFilterFNV fnvFilter2 = new BloomFilterFNV(setSize, bitsPerElement);
	}
	
	public static int estimateSetSize(BloomFilterFNV f) {
		int elementEstimate = 0;
		
		return elementEstimate;
	}
	
	public static int estimateIntersectSize(BloomFilterFNV f1, BloomFilterFNV f2) {
		int intersectionSize = 0;
		
		return intersectionSize;
	}

}
