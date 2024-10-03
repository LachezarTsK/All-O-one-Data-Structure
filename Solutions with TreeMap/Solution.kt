
import java.util.TreeMap
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class AllOne() {

    private companion object {
        const val NOT_FOUND = ""
    }

    private val wordToFrequency = HashMap<String, Int>()
    private val frequencyToWords = TreeMap<Int, HashSet<String>>()

    fun inc(word: String) {
        val previousFrequency = wordToFrequency.getOrDefault(word, 0)
        val newFrequency = previousFrequency + 1

        wordToFrequency[word] = newFrequency
        frequencyToWords.computeIfAbsent(newFrequency) { HashSet<String>() }.add(word)
        if (previousFrequency == 0) {
            return
        }

        frequencyToWords[previousFrequency]!!.remove(word)
        if (frequencyToWords[previousFrequency]!!.isEmpty()) {
            frequencyToWords.remove(previousFrequency)
        }
    }

    fun dec(word: String) {
        val previousFrequency = wordToFrequency[word]!!
        val newFrequency = previousFrequency - 1

        if (newFrequency > 0) {
            wordToFrequency[word] = newFrequency
            frequencyToWords.computeIfAbsent(newFrequency) { HashSet<String>() }.add(word)
        } else {
            wordToFrequency.remove(word)
        }

        frequencyToWords[previousFrequency]!!.remove(word)
        if (frequencyToWords[previousFrequency]!!.isEmpty()) {
            frequencyToWords.remove(previousFrequency)
        }
    }

    fun getMaxKey(): String {
        if (frequencyToWords.isEmpty()) {
            return NOT_FOUND
        }
        val maxFrequency = frequencyToWords.lastKey()
        return frequencyToWords[maxFrequency]!!.first()
    }

    fun getMinKey(): String {
        if (frequencyToWords.isEmpty()) {
            return NOT_FOUND
        }
        val minFrequency = frequencyToWords.firstKey()
        return frequencyToWords[minFrequency]!!.first()
    }
}
