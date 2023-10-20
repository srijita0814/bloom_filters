import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.util.Arrays;
import java.util.*;

public class NaiveDifferential {
    private Map<String, String> differentialMap;

    public NaiveDifferential(String differentialFilePath) {
        differentialMap = loadDifferentialData(differentialFilePath);
    }

    public String retrieveRecord(String key) {
    	key = key.trim();
    	//System.out.println(key);
        return differentialMap.get(key);
    }
    
    public String[] stringSplit(String input) {
    	String[] parts = input.split(" ", 5);
    	List<String> response = new ArrayList<String>();
    	if (parts.length >= 5) {
            String part1 = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3];
            String part2 = input.substring(part1.length()).trim();
            response.add(part1);
            response.add(part2);
        } else {
            System.out.println("String does not have enough words to split.");
        }
    	
    	String[] output = new String[response.size()];
    	response.toArray(output);
    	return output;
    }

    private Map<String, String> loadDifferentialData(String filePath) {
        Map<String, String> data = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line contains key-value pairs separated by a space
                String[] keyValPair = stringSplit(line);
                if (keyValPair.length == 2) {
                    data.put(keyValPair[0], keyValPair[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
        String filePath = "/Users/srijitachandra/Documents/Sem5/PA1/pa1Data/DiffFile.txt";
        String databaseFilePath = "/Users/srijitachandra/Documents/Sem5/PA1/pa1Data/database.txt";

        NaiveDifferential naive = new NaiveDifferential(filePath);
        NaiveDifferential database = new NaiveDifferential(databaseFilePath);

        String record = "";
        Set<String> keys = new HashSet<>();
        keys.add("are_VERB admirable_ADJ Things_NOUN ,_. ");      // This key is present in DiffFile.txt
        keys.add("article_NOUN about_ADP the_DET Society_NOUN "); // This key is present in database.txt
        keys.add("this key not there "); 						  // This key is not present in either of the files
        keys.add("arts are different species"); 				  // This key is present in DiffFile.txt
        
        for (String key : keys) {
        	record = naive.retrieveRecord(key);
        	if (record==null) {
            	record = database.retrieveRecord(key);
            }
        	System.out.println("Key: " + key + " is: " + record);
        }
        
    }
}
