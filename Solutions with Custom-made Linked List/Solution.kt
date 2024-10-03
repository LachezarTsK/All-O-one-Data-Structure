
import kotlin.collections.HashMap

class AllOne() {

    private companion object {
        const val NOT_FOUND = ""
        const val INCREASE = 1
        const val DECREASE = -1
    }

    private val wordToNode = HashMap<String, WordNode>()
    private val listFrequencies = LinkedListSortedByFrequency()

    fun inc(word: String) {
        val current: WordNode? = if (wordToNode.containsKey(word)) wordToNode.remove(word) else null
        val newWordNode: WordNode = listFrequencies.updateList(current, word, INCREASE)!!
        wordToNode[word] = newWordNode
    }

    fun dec(word: String) {
        val current: WordNode? = if (wordToNode.containsKey(word)) wordToNode.remove(word) else null
        val newWordNode: WordNode? = listFrequencies.updateList(current, word, DECREASE)
        if (newWordNode != null) {
            wordToNode[word] = newWordNode
        }
    }

    fun getMaxKey(): String {
        return if (!listFrequencies.isEmpty()) listFrequencies.getBackValue() else NOT_FOUND
    }

    fun getMinKey(): String {
        return if (!listFrequencies.isEmpty()) listFrequencies.getFrontValue() else NOT_FOUND
    }
}

class WordNode() {

    var frequency = 0
    var previous: WordNode? = null
    var next: WordNode? = null
    val words = HashSet<String>()

    constructor(word: String) : this() {
        words.add(word)
    }

    constructor(frequency: Int) : this() {
        this.frequency = frequency
    }
}

class LinkedListSortedByFrequency {

    private val sentinelHead: WordNode = WordNode(Int.MIN_VALUE)
    private val sentinelTail = WordNode(Int.MAX_VALUE)

    init {
        sentinelHead.next = sentinelTail
        sentinelTail.previous = sentinelHead
    }

    fun updateList(current: WordNode?, word: String, changeInFrequency: Int): WordNode? {

        reviseForPreviousFrequency(current, word)
        val previouFrequency = current?.frequency ?: 0
        val newFrequency = previouFrequency + changeInFrequency

        if (newFrequency <= 0) {
            return null
        }
        if (current == null && sentinelHead.next!!.frequency == newFrequency) {
            sentinelHead.next!!.words.add(word)
            return sentinelHead.next
        }
        if (current != null && current.next!!.frequency == newFrequency) {
            current.next!!.words.add(word)
            return current.next
        }
        if (current != null && current.previous!!.frequency == newFrequency) {
            current.previous!!.words.add(word)
            return current.previous
        }

        return insertNode(current, word, newFrequency)
    }

    private fun insertNode(current: WordNode?, word: String, newFrequency: Int): WordNode {
        var nodeToPreceedNewNode: WordNode? = null
        if (current == null) {
            nodeToPreceedNewNode = sentinelHead
        } else if (current.words.isEmpty() || current.frequency > newFrequency) {
            nodeToPreceedNewNode = current.previous
        } else if (current.frequency < newFrequency) {
            nodeToPreceedNewNode = current
        }

        val newNode = WordNode(word)
        newNode.frequency = newFrequency

        newNode.next = nodeToPreceedNewNode!!.next
        newNode.previous = nodeToPreceedNewNode
        newNode.next!!.previous = newNode
        newNode.previous!!.next = newNode

        return newNode
    }

    private fun reviseForPreviousFrequency(current: WordNode?, word: String) {
        if (current == null) {
            return
        }
        current.words.remove(word)
        if (current.words.isEmpty()) {
            current.next!!.previous = current.previous
            current.previous!!.next = current.next
        }
    }

    fun getFrontValue(): String {
        return sentinelHead.next!!.words.first()
    }

    fun getBackValue(): String {
        return sentinelTail.previous!!.words.first()
    }

    fun isEmpty(): Boolean {
        return sentinelHead.next == sentinelTail && sentinelTail.previous == sentinelHead
    }
}
