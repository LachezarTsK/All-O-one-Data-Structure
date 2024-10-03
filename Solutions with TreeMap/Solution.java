
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class AllOne {

    private static final String NOT_FOUND = "";

    private final Map<String, Integer> wordToFrequency = new HashMap<>();
    private final TreeMap<Integer, HashSet<String>> frequencyToWords = new TreeMap<>();

    public void inc(String word) {
        int previousFrequency = wordToFrequency.getOrDefault(word, 0);
        int newFrequency = previousFrequency + 1;

        wordToFrequency.put(word, newFrequency);
        frequencyToWords.computeIfAbsent(newFrequency, setWords -> new HashSet<>()).add(word);
        if (previousFrequency == 0) {
            return;
        }

        frequencyToWords.get(previousFrequency).remove(word);
        if (frequencyToWords.get(previousFrequency).isEmpty()) {
            frequencyToWords.remove(previousFrequency);
        }
    }

    public void dec(String word) {
        int previousFrequency = wordToFrequency.get(word);
        int newFrequency = previousFrequency - 1;

        if (newFrequency > 0) {
            wordToFrequency.put(word, newFrequency);
            frequencyToWords.computeIfAbsent(newFrequency, setWords -> new HashSet<>()).add(word);
        } else {
            wordToFrequency.remove(word);
        }

        frequencyToWords.get(previousFrequency).remove(word);
        if (frequencyToWords.get(previousFrequency).isEmpty()) {
            frequencyToWords.remove(previousFrequency);
        }
    }

    public String getMaxKey() {
        if (frequencyToWords.isEmpty()) {
            return NOT_FOUND;
        }
        int maxFrequency = frequencyToWords.lastKey();
        return frequencyToWords.get(maxFrequency).iterator().next();
    }

    public String getMinKey() {
        if (frequencyToWords.isEmpty()) {
            return NOT_FOUND;
        }
        int minFrequency = frequencyToWords.firstKey();
        return frequencyToWords.get(minFrequency).iterator().next();
    }
}
