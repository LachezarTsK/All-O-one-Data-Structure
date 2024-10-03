
class AllOne {

    static #NOT_FOUND = "";
    static #INCREASE = 1;
    static #DECREASE = -1;

    #wordToNode = new Map();
    #listFrequencies = new LinkedListSortedByFrequency();

    /** 
     * @param {string} word
     * @return {void}
     */
    inc(word) {
        let current = null;
        if (this.#wordToNode.has(word)) {
            current = this.#wordToNode.get(word);
            this.#wordToNode.delete(word);
        }
        const newWordNode = this.#listFrequencies.updateList(current, word, AllOne.#INCREASE);
        this.#wordToNode.set(word, newWordNode);
    }

    /** 
     * @param {string} word
     * @return {void}
     */
    dec = function (word) {
        let current = null;
        if (this.#wordToNode.has(word)) {
            current = this.#wordToNode.get(word);
            this.#wordToNode.delete(word);
        }

        const newWordNode = this.#listFrequencies.updateList(current, word, AllOne.#DECREASE);
        if (newWordNode !== null) {
            this.#wordToNode.set(word, newWordNode);
        }
    }

    /**
     * @return {string}
     */
    getMaxKey() {
        return !this.#listFrequencies.isEmpty() ? this.#listFrequencies.getBackValue() : AllOne.#NOT_FOUND;
    }

    /**
     * @return {string}
     */
    getMinKey() {
        return !this.#listFrequencies.isEmpty() ? this.#listFrequencies.getFrontValue() : AllOne.#NOT_FOUND;
    }
}

class WordNode {

    /** 
     * @param {string | null} word
     */
    constructor(word = null) {
        this.frequency = 0;
        this.previous = null;
        this.next = null;
        this.words = new Set();
        this.words.add(word);
    }
}

class LinkedListSortedByFrequency {

    #sentinelHead = new WordNode();
    #sentinelTail = new WordNode();

    constructor() {
        this.#sentinelHead.frequency = Number.MIN_SAFE_INTEGER;
        this.#sentinelTail.frequency = Number.MAX_SAFE_INTEGER;

        this.#sentinelHead.next = this.#sentinelTail;
        this.#sentinelTail.previous = this.#sentinelHead;
    }

    /** 
     * @param {WordNode | null} current
     * @param {string} word
     * @param {number} changeInFrequency
     * @return {WordNode | null}
     */
    updateList(current, word, changeInFrequency) {
        this.#reviseForPreviousFrequency(current, word);
        const previouFrequency = (current !== null) ? current.frequency : 0;
        const newFrequency = previouFrequency + changeInFrequency;

        if (newFrequency <= 0) {
            return null;
        }
        if (current === null && this.#sentinelHead.next.frequency === newFrequency) {
            this.#sentinelHead.next.words.add(word);
            return this.#sentinelHead.next;
        }
        if (current !== null && current.next.frequency === newFrequency) {
            current.next.words.add(word);
            return current.next;
        }
        if (current !== null && current.previous.frequency === newFrequency) {
            current.previous.words.add(word);
            return current.previous;
        }

        return this.#insertNode(current, word, newFrequency);
    }

    /** 
     * @param {WordNode | null} current
     * @param {string} word
     * @return {void}
     */
    #reviseForPreviousFrequency(current, word) {
        if (current === null) {
            return;
        }
        current.words.delete(word);
        if (current.words.size === 0) {
            current.next.previous = current.previous;
            current.previous.next = current.next;
        }
    }

    /** 
     * @param {WordNode | null} current
     * @param {string} word
     * @param {number} newFrequency
     * @return {WordNode | null}
     */
    #insertNode(current, word, newFrequency) {
        let nodeToPreceedNewNode = null;
        if (current === null) {
            nodeToPreceedNewNode = this.#sentinelHead;
        } else if (current.words.size === 0 || current.frequency > newFrequency) {
            nodeToPreceedNewNode = current.previous;
        } else if (current.frequency < newFrequency) {
            nodeToPreceedNewNode = current;
        }

        const newNode = new WordNode(word);
        newNode.frequency = newFrequency;

        newNode.next = nodeToPreceedNewNode.next;
        newNode.previous = nodeToPreceedNewNode;
        newNode.next.previous = newNode;
        newNode.previous.next = newNode;

        return newNode;
    }

    /** 
     * @return {string}
     */
    getFrontValue() {
        return this.#sentinelHead.next.words.values().next().value;
    }

    /** 
     * @return {string}
     */
    getBackValue() {
        return this.#sentinelTail.previous.words.values().next().value;
    }

    /** 
     * @return {boolean}
     */
    isEmpty() {
        return this.#sentinelHead.next === this.#sentinelTail && this.#sentinelTail.previous === this.#sentinelHead;
    }
}
