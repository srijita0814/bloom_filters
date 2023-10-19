import java.util.*;

public class Statistics {

	public static void main(String[] args) {
		int setSize = 10000;
        int bitsPerElement = 10;
        
        Set<String> set1 = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < setSize; i++) {
            set1.add(generateRandomString(random, 10));
        }
        
        Set<String> set2 = new HashSet<>();
        for (int i = 0; i < setSize; i++) {
            set2.add(generateRandomString(random, 10));
        }
        
		BloomFilterFNV fnvFilter1 = new BloomFilterFNV(setSize, bitsPerElement);
		BloomFilterFNV fnvFilter2 = new BloomFilterFNV(setSize, bitsPerElement);
		
		for (String name : set1) {
			fnvFilter1.add(name);
        }
		
		for (String name : set2) {
			fnvFilter2.add(name);
        }
		
		estimateSetSize(fnvFilter1);
		estimateSetSize(fnvFilter2);
		
		estimateIntersectSize(fnvFilter1, fnvFilter2);
	}
	
	
	private static String generateRandomString(Random random, int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }
	
	public static int estimateSetSize(BloomFilterFNV f) {
		int m = f.filterSize();
		int k = f.numHashes();
		int zeroBits = 0;
		for (int g=0; g<m; g++) {
			if (!f.getBit(g)) {
				zeroBits++;
			}
		}
		
		System.out.println(zeroBits);
		
		double p = (Math.log(zeroBits/ (double) m)) / ((double)k*(Math.log(1- (1/(double)m))));
		System.out.println("New : " + (int) Math.round(p));
		double setSizeEstimate = -(m / (double) k) * Math.log(1 - (zeroBits / (double) m));
		System.out.println((int) Math.round(setSizeEstimate));
		
		return (int) Math.round(setSizeEstimate);
	}
	
	public static int estimateIntersectSize(BloomFilterFNV f1, BloomFilterFNV f2) {
		int m = f1.filterSize();
		int k = f1.numHashes();
		int filter1Zero = 0;
		int filter2Zero = 0;
		int intersectionBits = 0;
		
		for (int g=0; g<m; g++) {
			if (!f1.getBit(g)) {
				filter1Zero++;
			}
			if (!f2.getBit(g)) {
				filter2Zero++;
			}
			if (!f1.getBit(g) && !f2.getBit(g)) {
				intersectionBits++;
			}
		}
		
		double t = -(m / (double) k) * Math.log(1 - (intersectionBits / (double) m));
        double intersectionSize = filter1Zero + filter2Zero - intersectionBits - t;
        
        //System.out.println("Intersection between f1 and f2 = " + (int) Math.round(intersectionSize));
		
		return (int) Math.round(intersectionSize);
	}

}


