import java.util.*;

public class BloomFilterPerformanceComparison {
    public static void main(String[] args) {
        int setSize = 100000;
        int bitsPerElement = 10;
        int numTests = 10000; // Number of test strings

        // Create a random set of strings
        Set<String> randomStrings = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < setSize; i++) {
            randomStrings.add(generateRandomString(random, 10));
        }

        // Create and test different Bloom Filters
        BloomFilterFNV fnvFilter = new BloomFilterFNV(setSize, bitsPerElement);
        BloomFilterRan ranFilter = new BloomFilterRan(setSize, bitsPerElement);
        BloomFilterRanPlus ranPlusFilter = new BloomFilterRanPlus(setSize, bitsPerElement);
        MultiMultiBloomFilter multiMultiFilter = new MultiMultiBloomFilter(setSize, bitsPerElement);

        testBloomFilter(fnvFilter, randomStrings, numTests);
        testBloomFilter(ranFilter, randomStrings, numTests);
        testBloomFilter(ranPlusFilter, randomStrings, numTests);
        testMultiMultiBloomFilter(multiMultiFilter, randomStrings, numTests);
    }

    // Generate a random string of a given length
    // Need to update code here to include numbers too and also make the strings variable length
    private static String generateRandomString(Random random, int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }

    // Test BloomFilterFNV for false positives
    private static void testBloomFilter(BloomFilterFNV bloomFilter, Set<String> randomStrings, int numTests) {
        int falsePositives = 0;
        
        for (String name : randomStrings) {
            bloomFilter.add(name);
        }
        
        for (int i = 0; i < numTests; i++) {
            String testString = generateRandomString(new Random(), 20);
            if (!randomStrings.contains(testString) && bloomFilter.appears(testString)) {
                falsePositives++;
            }
        }

        float falsePositiveRate = (float) falsePositives / numTests;
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positives: " + falsePositives);
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positive Rate: " + falsePositiveRate);
    }

    // Test BloomFilterRan for false positives
    private static void testBloomFilter(BloomFilterRan bloomFilter, Set<String> randomStrings, int numTests) {
        int falsePositives = 0;
        
        for (String name : randomStrings) {
            bloomFilter.add(name);
        }

        for (int i = 0; i < numTests; i++) {
            String testString = generateRandomString(new Random(), 10);
            if (!randomStrings.contains(testString) && bloomFilter.appears(testString)) {
                falsePositives++;
            }
        }

        double falsePositiveRate = (double) falsePositives / numTests;
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positives: " + falsePositives);
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positive Rate: " + falsePositiveRate);
    }

    // Test BloomFilterRanPlus for false positives
    private static void testBloomFilter(BloomFilterRanPlus bloomFilter, Set<String> randomStrings, int numTests) {
        int falsePositives = 0;
        
        for (String name : randomStrings) {
            bloomFilter.add(name);
        }

        for (int i = 0; i < numTests; i++) {
            String testString = generateRandomString(new Random(), 10);
            if (!randomStrings.contains(testString) && bloomFilter.appears(testString)) {
                falsePositives++;
            }
        }

        double falsePositiveRate = (double) falsePositives / numTests;
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positives: " + falsePositives);
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positive Rate: " + falsePositiveRate);
    }

    // Test MultiMultiBloomFilter for false positives
    private static void testMultiMultiBloomFilter(MultiMultiBloomFilter bloomFilter, Set<String> randomStrings, int numTests) {
        int falsePositives = 0;
        
        for (String name : randomStrings) {
            bloomFilter.add(name);
        }

        for (int i = 0; i < numTests; i++) {
            String testString = generateRandomString(new Random(), 10);
            if (!randomStrings.contains(testString) && bloomFilter.appears(testString)) {
                falsePositives++;
            }
        }

        double falsePositiveRate = (double) falsePositives / numTests;
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positives: " + falsePositives);
        System.out.println(bloomFilter.getClass().getSimpleName() + " False Positive Rate: " + falsePositiveRate);
    }

}
