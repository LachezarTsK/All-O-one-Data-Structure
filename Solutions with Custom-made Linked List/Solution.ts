
class AllOne {

    private static NOT_FOUND = "";
    private static INCREASE = 1;
    private static DECREASE = -1;

    private wordToNode: Map<string, WordNode> = new Map();
    private listFrequencies = new LinkedListSortedByFrequency();


    inc(word: string): void {
        let current: WordNode | null = null;
        if (this.wordToNode.has(word)) {
            current = this.wordToNode.get(word);
            this.wordToNode.delete(word);
        }
        const newWordNode: WordNode | null = this.listFrequencies.updateList(current, word, AllOne.INCREASE);
        this.wordToNode.set(word, newWordNode);
    }

    dec(word: string): void {
        let current: WordNode | null = null;
        if (this.wordToNode.has(word)) {
            current = this.wordToNode.get(word);
            this.wordToNode.delete(word);
        }

        const newWordNode: WordNode | null = this.listFrequencies.updateList(current, word, AllOne.DECREASE);
        if (newWordNode !== null) {
            this.wordToNode.set(word, newWordNode);
        }
    }

    getMaxKey(): string {
        return !this.listFrequencies.isEmpty() ? this.listFrequencies.getBackValue() : AllOne.NOT_FOUND;
    }

    getMinKey(): string {
        return !this.listFrequencies.isEmpty() ? this.listFrequencies.getFrontValue() : AllOne.NOT_FOUND;
    }
}

class WordNode {

    frequency: number = 0;
    previous: WordNode = null;
    next: WordNode = null;
    words: Set<string> = new Set();

    constructor(word: string | null) {
        this.words.add(word);
    }
}

class LinkedListSortedByFrequency {

    private sentinelHead = new WordNode(null);
    private sentinelTail = new WordNode(null);

    constructor() {
        this.sentinelHead.frequency = Number.MIN_SAFE_INTEGER;
        this.sentinelTail.frequency = Number.MAX_SAFE_INTEGER;

        this.sentinelHead.next = this.sentinelTail;
        this.sentinelTail.previous = this.sentinelHead;
    }

    updateList(current: WordNode | null, word: string, changeInFrequency: number): WordNode | null {
        this.reviseForPreviousFrequency(current, word);
        const previouFrequency = (current !== null) ? current.frequency : 0;
        const newFrequency = previouFrequency + changeInFrequency;

        if (newFrequency <= 0) {
            return null;
        }
        if (current === null && this.sentinelHead.next.frequency === newFrequency) {
            this.sentinelHead.next.words.add(word);
            return this.sentinelHead.next;
        }
        if (current !== null && current.next.frequency === newFrequency) {
            current.next.words.add(word);
            return current.next;
        }
        if (current !== null && current.previous.frequency === newFrequency) {
            current.previous.words.add(word);
            return current.previous;
        }

        return this.insertNode(current, word, newFrequency);
    }

    private reviseForPreviousFrequency(current: WordNode | null, word: string): void {
        if (current === null) {
            return;
        }
        current.words.delete(word);
        if (current.words.size === 0) {
            current.next.previous = current.previous;
            current.previous.next = current.next;
        }
    }


    private insertNode(current: WordNode | null, word: string, newFrequency: number): WordNode | null {
        let nodeToPreceedNewNode = null;
        if (current === null) {
            nodeToPreceedNewNode = this.sentinelHead;
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


    getFrontValue(): string {
        return this.sentinelHead.next.words.values().next().value;
    }

    getBackValue(): string {
        return this.sentinelTail.previous.words.values().next().value;
    }

    isEmpty(): boolean {
        return this.sentinelHead.next === this.sentinelTail && this.sentinelTail.previous === this.sentinelHead;
    }
}
