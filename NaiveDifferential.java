import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NaiveDifferential {
    private Map<String, String> differentialMap;

//    public NaiveDifferential(String differentialFilePath) {
//        differentialMap = loadDifferentialData(differentialFilePath);
//    }

    public String retrieveRecord(String key) {
        return differentialMap.get(key);
    }

    private Map<String, String> loadDifferentialData(String filePath) {
        Map<String, String> data = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line contains key-value pairs separated by a space
                String[] parts = line.split(" ", 2);
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
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
//        int setSize = 1200000;
//        int bitsPerElement = 8;

        //BloomFilterRan ranFilter = new BloomFilterRan(setSize, bitsPerElement);

        NaiveDifferential naive = new NaiveDifferential();
        naive.loadDifferentialData(filePath);
    }
}
