
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AllOne {

    private static final String NOT_FOUND = "";
    private static final int INCREASE = 1;
    private static final int DECREASE = -1;

    private final Map<String, WordNode> wordToNode = new HashMap<>();
    private final LinkedListSortedByFrequency listFrequencies = new LinkedListSortedByFrequency();

    public void inc(String word) {
        Optional<WordNode> current = wordToNode.containsKey(word) ? Optional.of(wordToNode.remove(word)) : Optional.empty();
        WordNode newWordNode = listFrequencies.updateList(current, word, INCREASE).get();
        wordToNode.put(word, newWordNode);
    }

    public void dec(String word) {
        Optional<WordNode> current = wordToNode.containsKey(word) ? Optional.of(wordToNode.remove(word)) : Optional.empty();
        Optional<WordNode> newWordNode = listFrequencies.updateList(current, word, DECREASE);
        if (newWordNode.isPresent()) {
            wordToNode.put(word, newWordNode.get());
        }
    }

    public String getMaxKey() {
        return !listFrequencies.isEmpty() ? listFrequencies.getBackValue() : NOT_FOUND;
    }

    public String getMinKey() {
        return !listFrequencies.isEmpty() ? listFrequencies.getFrontValue() : NOT_FOUND;
    }
}

class WordNode {

    int frequency;
    WordNode previous;
    WordNode next;
    Set<String> words = new HashSet<>();

    WordNode(String word) {
        words.add(word);
    }

    WordNode(int frequency) {
        this.frequency = frequency;
    }
}

class LinkedListSortedByFrequency {

    private final WordNode sentinelHead = new WordNode(Integer.MIN_VALUE);
    private final WordNode sentinelTail = new WordNode(Integer.MAX_VALUE);

    LinkedListSortedByFrequency() {
        sentinelHead.next = sentinelTail;
        sentinelTail.previous = sentinelHead;
    }

    Optional<WordNode> updateList(Optional<WordNode> current, String word, int changeInFrequency) {

        reviseForPreviousFrequency(current, word);
        int previouFrequency = !current.isEmpty() ? current.get().frequency : 0;
        int newFrequency = previouFrequency + changeInFrequency;

        if (newFrequency <= 0) {
            return Optional.empty();
        }
        if (current.isEmpty() && sentinelHead.next.frequency == newFrequency) {
            sentinelHead.next.words.add(word);
            return Optional.of(sentinelHead.next);
        }
        if (current.isPresent() && current.get().next.frequency == newFrequency) {
            current.get().next.words.add(word);
            return Optional.of(current.get().next);
        }
        if (current.isPresent() && current.get().previous.frequency == newFrequency) {
            current.get().previous.words.add(word);
            return Optional.of(current.get().previous);
        }

        return insertNode(current, word, newFrequency);
    }

    private void reviseForPreviousFrequency(Optional<WordNode> current, String word) {
        if (current.isEmpty()) {
            return;
        }
        current.get().words.remove(word);
        if (current.get().words.isEmpty()) {
            current.get().next.previous = current.get().previous;
            current.get().previous.next = current.get().next;
        }
    }

    private Optional<WordNode> insertNode(Optional<WordNode> current, String word, int newFrequency) {
        WordNode nodeToPreceedNewNode = null;
        if (current.isEmpty()) {
            nodeToPreceedNewNode = sentinelHead;
        } else if (current.get().words.isEmpty() || current.get().frequency > newFrequency) {
            nodeToPreceedNewNode = current.get().previous;
        } else if (current.get().frequency < newFrequency) {
            nodeToPreceedNewNode = current.get();
        }

        WordNode newNode = new WordNode(word);
        newNode.frequency = newFrequency;

        newNode.next = nodeToPreceedNewNode.next;
        newNode.previous = nodeToPreceedNewNode;
        newNode.next.previous = newNode;
        newNode.previous.next = newNode;

        return Optional.of(newNode);
    }

    String getFrontValue() {
        return sentinelHead.next.words.iterator().next();
    }

    String getBackValue() {
        return sentinelTail.previous.words.iterator().next();
    }

    boolean isEmpty() {
        return sentinelHead.next == sentinelTail && sentinelTail.previous == sentinelHead;
    }
}
