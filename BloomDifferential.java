
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BloomDifferential {
	private BloomFilterRan ranFilter;

	public BloomDifferential(String differentialFilePath) {
         // Create a BloomFilterRan instance
		createFilter(differentialFilePath);
        
    }
	
	public void createFilter(String differentialFilePath) {
		Set<String> differentialKeys = loadDifferentialKeys(differentialFilePath);
        ranFilter = new BloomFilterRan(differentialKeys.size(), 8);
		for (String key : differentialKeys) {
			//System.out.println(key);
            ranFilter.add(key); // Add the key to the BloomFilterRan as well
        }
	}

    private Set<String> loadDifferentialKeys(String filePath) {
        Set<String> keys = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the differential file is a key
            	String[] updatedKey = stringSplit(line);
            	//System.out.println("Keys here :: " + updatedKey[0]);
                keys.add(updatedKey[0]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return keys;
    }
    
    public String[] stringSplit(String input) {
    	String[] parts = input.split(" ", 5);
    	List<String> response = new ArrayList<String>();
    	if (parts.length >= 5) {
            String part1 = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3];
            part1.trim();
            response.add(part1);
        } else {
            System.out.println("String does not have enough words to split.");
        }
    	String[] output = new String[response.size()];
    	response.toArray(output);
    	return output;
    }
    
    public String retrieveRecord(String key) {
        if (ranFilter.appears(key)) {
        	System.out.println("Record is present");
            // Consult differential.txt for the record and return it
            // Add your code to retrieve the record here
        } else {
        	System.out.println("Record is NOT present");
            // Key does not exist in differential.txt; access database.txt directly
            // Add your code to access database.txt here
        }
        return null; // Return null if not found
    }
    
    public static void main(String[] args) {
    	String filePath = "/Users/srijitachandra/Documents/Sem5/PA1/pa1Data/trial.txt";
    	//String databaseFilePath = "/Users/srijitachandra/Documents/Sem5/PA1/pa1Data/grams.txt";
//        int setSize = 1200000;
//        int bitsPerElement = 8;

        //BloomFilterRan ranFilter = new BloomFilterRan(setSize, bitsPerElement);

          BloomDifferential differential = new BloomDifferential(filePath);
//          String f = differential.retrieveRecord("ARTICLE_DET 1_NUM Section_NOUN 1_UM");
//          System.out.println("Appears or not : " + f);
//        BloomDifferential database = new BloomDifferential();
//        database.createFilter(databaseFilePath);
          
          String record = "";
          Set<String> keys = new HashSet<>();
          keys.add("ARTICLE_DET 1_NUM Section_NOUN 1_NUM");      // This key is present in DiffFile.txt
          keys.add("Arab agitation . _END_ "); // This key is present in database.txt
          keys.add("this key not there "); 						  // This key is not present in either of the files
          keys.add("arts are different species"); 
          
          for (String key : keys) {
        	  key = key.trim();
          	  record = differential.retrieveRecord(key);
//          	if (record==null) {
//              	record = differential.retrieveRecord(key);
//              }
          	  System.out.println("Key: " + key + " \nValue is: " + record);
          }
    }
}

