
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BloomDifferential {
	private BloomFilterRan ranFilter;
	private BloomFilterRan gramFilter;
	String filePath;
	String dbkeysPath;
	String databasePath;

	public BloomDifferential(String differentialFilePath, String databaseKeysPath, String dbpath) {
        this.filePath = differentialFilePath;
		this.dbkeysPath = databaseKeysPath;
		this.databasePath = dbpath;
		this.ranFilter = createFilter(differentialFilePath);
		this.gramFilter = createFilter(databaseKeysPath);  
    }
	
	public BloomFilterRan createFilter(String differentialFilePath) {
		Set<String> differentialKeys = loadDifferentialKeys(differentialFilePath);
		BloomFilterRan ranFilter = new BloomFilterRan(differentialKeys.size(), 8);
		for (String key : differentialKeys) {
			key = key.trim();
            ranFilter.add(key); 
        }
		return ranFilter;
	}

    private Set<String> loadDifferentialKeys(String filePath) {
        Set<String> keys = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] updatedKey = stringSplit(line);
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
    	if (parts.length >= 4) {
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
        	//System.out.println("Record is present");
        	String s = findLineWithSubstring(filePath, key);
        	if (s != null) {
        		String[] parts = s.split(" ", 5);
        		String strValue = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3];
        		strValue = s.substring(strValue.length()).trim();
        		//System.out.println(strValue);
        		return strValue;
        	}
        } 

        else if (gramFilter.appears(key)) {
        	String f = findLineWithSubstring(databasePath, key);
        	if (f != null) {
        		String[] parts = f.split(" ", 5);
        		String strValue = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3];
        		strValue = f.substring(strValue.length()).trim();
        		return strValue;
        	}
        		
        }
            
        else {
        	System.out.println("Record is NOT present");
            // Key does not exist in differential.txt; access database.txt directly
            // Add your code to access database.txt here
        }
        return null; // Return null if not found
    }
    
    public static String findLineWithSubstring(String filePath, String targetSubstring) {
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                if (line.contains(targetSubstring)) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Substring not found in any file.
    }
    
    public static void main(String[] args) {
    	String filePath = "./pa1Data/DiffFile.txt";
    	String dbkeysPath = "./pa1Data/grams.txt";
    	String databasePath = "./pa1Data/database.txt";
    	
        BloomDifferential differential = new BloomDifferential(filePath, dbkeysPath, databasePath);
          
        String record = "";
        Set<String> keys = new HashSet<>();
        keys.add("are_VERB admirable_ADJ Things_NOUN ,_. ");      // This key is present in DiffFile.txt
        keys.add("article_NOUN about_ADP the_DET Society_NOUN "); // This key is present in database.txt
        keys.add("this key not there "); 						  // This key is not present in either of the files
        keys.add("arts are different species"); 				  // This key is present in DiffFile.txt
        
        // Testing Code
//    	String filePath = "./pa1Data/trial.txt";
//    	String dbkeysPath = "./pa1Data/difftrialkeys.txt";
//    	String databasePath = "./pa1Data/difftrial.txt";
//    
//        BloomDifferential differential = new BloomDifferential(filePath, dbkeysPath, databasePath);
//          
//        String record = "";
//        Set<String> keys = new HashSet<>();
//        keys.add("ARTICLE_DET 1_NUM Section_NOUN 1_NUM");      // This key is present in trial.txt
//        keys.add("Arabs in equal numbers"); 					 // This key is present in difftrialkeys.txt
//        keys.add("this key not there "); 						 // This key is not present in either of the files
//        keys.add("Arabs and the Chinese");				     // This key is present in difftrialkeys.txt
      
        for (String key : keys) {
    	    key = key.trim();
    	    System.out.println("Key: " + key );
      	    record = differential.retrieveRecord(key);
      	    System.out.println("Value is: " + record);
        }
    }
}

