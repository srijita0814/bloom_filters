import java.util.*;
//import java.util.concurrent.TimeUnit;

public class EmpericalComparison {
	//List<String> sampleQueries;
	private static final int minWordLength = 3;
    private static final int maxWordLength = 8;
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int numWords = 4;
	
//	public EmpericalComparison() {
//		this.sampleQueries = new ArrayList<>();
//	}

	public static void main(String[] args) {
		int numTests = 10;
		
		// Initialize BloomDifferential
        BloomDifferential bloomDifferential = new BloomDifferential("./pa1Data/trial.txt", "./pa1Data/difftrialkeys.txt", "./pa1Data/difftrial.txt");

        // Initialize NaiveDifferential
        NaiveDifferential naiveDifferential = new NaiveDifferential("./pa1Data/trial.txt", "./pa1Data/difftrial.txt");

        // Define sample queries
        //List<String> sampleQueries = generateSampleQueries(sampleQueries);
        List<String> sampleQueries = generateSampleQueries(numTests);

        // Test and compare the performance of both methods
        comparePerformance(bloomDifferential, naiveDifferential, sampleQueries, numTests);
        
	}
	
	public static String generateRandomString(int numWords) {
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < numWords; i++) {
            if (i > 0) {
                randomString.append(" "); // Separate words with a space
            }
            String randomWord = generateRandomWord(random);
            randomString.append(randomWord);
        }

        return randomString.toString();
    }

    private static String generateRandomWord(Random random) {
        int wordLength = minWordLength + random.nextInt(maxWordLength - minWordLength + 1);
        StringBuilder randomWord = new StringBuilder();

        for (int i = 0; i < wordLength; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            randomWord.append(randomChar);
        }

        return randomWord.toString();
    }

	
	private static List<String> generateSampleQueries(int numTests) {
        List<String> sampleQueries = new ArrayList<>();
        for (int i = 0; i < (numTests-4); i++) {
            String randomString = generateRandomString(numWords);
            //System.out.println(randomString);
            sampleQueries.add(randomString);
        }
        
        sampleQueries.add("ARTICLE_DET 1_NUM Section_NOUN 1_NUM");      // This key is present in trial.txt
        sampleQueries.add("Arabs in equal numbers"); 					 // This key is present in difftrialkeys.txt
        sampleQueries.add("this key not there "); 						 // This key is not present in either of the files
        sampleQueries.add("Arabs and the Chinese");				     // This key is present in difftrialkeys.txt
        
        return sampleQueries;
    }
	
	public static int naiveNullValues(List<String> testKeys, NaiveDifferential naiveDifferential) {
        //List<String> falsePositives = new HashSet<>();
		int nullVal = 0;
        
        for (String key : testKeys) {
            String record = naiveDifferential.retrieveRecord(key);
            if (record == null) {
                //falsePositives.add(key);
            	nullVal++;
            }
        }
        
        return nullVal;
    }
	
	private static int testBloomFilter(BloomDifferential bloomFilter, int numTests, List<String> testStrings) {
        int nullVal = 0;
        for (String testString: testStrings) {
        	String val = bloomFilter.retrieveRecord(testString);
        	//System.out.println(val);
        	if (val == null) {
        		nullVal++;
            }
        }
        return nullVal;
    }
	
	private static void comparePerformance(BloomDifferential bloomDifferential, NaiveDifferential naiveDifferential, List<String> queries, int numTests) {
        // Measure the time taken for BloomDifferential and NaiveDifferential for each query
		long bloomDiffAvgTime = 0;
		long naiveDiffAvgTime = 0;
        for (String query : queries) {
            long startTime = System.nanoTime();
        	//long startTime = System.currentTimeMillis();
            bloomDifferential.retrieveRecord(query);
            long bloomDifferentialTime = System.nanoTime() - startTime;
            //long bloomDifferentialTime = (System.currentTimeMillis() - startTime);
            bloomDiffAvgTime += bloomDifferentialTime;
            //System.out.println("bloomDifferentialTime::: " + bloomDifferentialTime);

            startTime = System.nanoTime();
            //startTime = System.currentTimeMillis();
            naiveDifferential.retrieveRecord(query);
            long naiveDifferentialTime = (System.nanoTime() - startTime);
            naiveDiffAvgTime += naiveDifferentialTime;
            //System.out.println("naiveDifferentialTime::: " + naiveDifferentialTime);

        }
        
        System.out.println("Average time Bloom Differential : " + (bloomDiffAvgTime / queries.size()) + ".nanoseconds per query");
        System.out.println("Average time Naive Differential : " + (naiveDiffAvgTime / queries.size()) + ".nanoseconds per query");
        
        int ndNulValues = naiveNullValues(queries, naiveDifferential);
        System.out.println("Naive Differential Null Values : " + ndNulValues);
        
        int bdNulValues = testBloomFilter(bloomDifferential, numTests, queries);
        System.out.println("Bloom Differential Null Values : " + bdNulValues);
    }

}
