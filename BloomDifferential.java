
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BloomDifferential {
	private BloomFilterRan ranFilter;

//	public BloomDifferential(String differentialFilePath) {
//         // Create a BloomFilterRan instance
//		createFilter(differentialFilePath);
//        
//    }
	
	public void createFilter(String differentialFilePath) {
		Set<String> differentialKeys = loadDifferentialKeys(differentialFilePath);
        ranFilter = new BloomFilterRan(differentialKeys.size(), 8);
		for (String key : differentialKeys) {
			//System.out.println(key);
            ranFilter.add(key); // Add the key to the BloomFilterRan as well
        }
	}
	
	public String retrieveRecord(String key) {
        if (ranFilter.appears(key)) {
            // Consult differential.txt for the record and return it
            // Add your code to retrieve the record here
        } else {
            // Key does not exist in differential.txt; access database.txt directly
            // Add your code to access database.txt here
        }
        return null; // Return null if not found
    }

    private Set<String> loadDifferentialKeys(String filePath) {
        Set<String> keys = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the differential file is a key
                keys.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return keys;
    }
    
    public static void main(String[] args) {
    	String filePath = "/Users/srijitachandra/Documents/Sem5/PA1/pa1Data/DiffFile.txt";
//        int setSize = 1200000;
//        int bitsPerElement = 8;

        //BloomFilterRan ranFilter = new BloomFilterRan(setSize, bitsPerElement);

        BloomDifferential differential = new BloomDifferential();
        differential.createFilter(filePath);
    }
}

