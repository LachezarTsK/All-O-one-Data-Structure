
package main

import (
    "fmt"
    "math"
)

const NOT_FOUND = ""
const INCREASE = 1
const DECREASE = -1

type AllOne struct {
    wordToNode      map[string]*WordNode
    listFrequencies LinkedListSortedByFrequency
}

func Constructor() AllOne {
    allOne := AllOne{
        wordToNode:      map[string]*WordNode{},
        listFrequencies: NewLinkedListSortedByFrequency(),
    }
    return allOne
}

func (this *AllOne) Inc(word string) {
    var current *WordNode = nil
    if wordNode, contains := this.wordToNode[word]; contains {
        current = wordNode
        delete(this.wordToNode, word)
    }
    var newWordNode *WordNode = this.listFrequencies.updateList(current, word, INCREASE)
    this.wordToNode[word] = newWordNode
}

func (this *AllOne) Dec(word string) {
    var current *WordNode = nil
    if wordNode, contains := this.wordToNode[word]; contains {
        current = wordNode
        delete(this.wordToNode, word)
    }
    var newWordNode *WordNode = this.listFrequencies.updateList(current, word, DECREASE)
    if newWordNode != nil {
        this.wordToNode[word] = newWordNode
    }
}

func (this *AllOne) GetMaxKey() string {
    return Ternary(!this.listFrequencies.isEmpty(), this.listFrequencies.getBackValue(), NOT_FOUND)
}

func (this *AllOne) GetMinKey() string {
    return Ternary(!this.listFrequencies.isEmpty(), this.listFrequencies.getFrontValue(), NOT_FOUND)
}

type HashSet struct {
    container map[string]bool
}

func (hashSet *HashSet) firstElement() string {
    var word string
    for w := range hashSet.container {
        word = w
        break
    }
    return word
}

type WordNode struct {
    frequency int
    previous  *WordNode
    next      *WordNode
    words     HashSet
}

func NewWordNode(word string) *WordNode {
    wordNode := &WordNode{
        frequency: 0,
        previous:  nil,
        next:      nil,
        words:     HashSet{container: map[string]bool{}},
    }
    wordNode.words.container[word] = true
    return wordNode
}

type LinkedListSortedByFrequency struct {
    sentinelHead *WordNode
    sentinelTail *WordNode
}

func NewLinkedListSortedByFrequency() LinkedListSortedByFrequency {
    list := LinkedListSortedByFrequency{
        sentinelHead: &WordNode{
            frequency: math.MinInt32,
            previous:  nil,
            next:      nil,
            words:     HashSet{container: map[string]bool{}},
        },
        sentinelTail: &WordNode{
            frequency: math.MaxInt32,
            previous:  nil,
            next:      nil,
            words:     HashSet{container: map[string]bool{}},
        },
    }
    list.sentinelHead.next = list.sentinelTail
    list.sentinelTail.previous = list.sentinelHead
    return list
}

func (this *LinkedListSortedByFrequency) updateList(current *WordNode, word string, changeInFrequency int) *WordNode {

    reviseForPreviousFrequency(current, word)
    previouFrequency := 0
    if current != nil {
        previouFrequency = current.frequency
    }
    newFrequency := previouFrequency + changeInFrequency

    if newFrequency <= 0 {
        return nil
    }
    if current == nil && this.sentinelHead.next.frequency == newFrequency {
        this.sentinelHead.next.words.container[word] = true
        return this.sentinelHead.next
    }
    if current != nil && current.next.frequency == newFrequency {
        current.next.words.container[word] = true
        return current.next
    }
    if current != nil && current.previous.frequency == newFrequency {
        current.previous.words.container[word] = true
        return current.previous
    }

    return this.insertNode(current, word, newFrequency)
}

func (this *LinkedListSortedByFrequency) insertNode(current *WordNode, word string, newFrequency int) *WordNode {
    var nodeToPreceedNewNode *WordNode = nil
    if current == nil {
        nodeToPreceedNewNode = this.sentinelHead
    } else if len(current.words.container) == 0 || current.frequency > newFrequency {
        nodeToPreceedNewNode = current.previous
    } else if current.frequency < newFrequency {
        nodeToPreceedNewNode = current
    }

    newNode := NewWordNode(word)
    newNode.frequency = newFrequency

    newNode.next = nodeToPreceedNewNode.next
    newNode.previous = nodeToPreceedNewNode
    newNode.next.previous = newNode
    newNode.previous.next = newNode

    return newNode
}

func reviseForPreviousFrequency(current *WordNode, word string) {
    if current == nil {
        return
    }

    delete(current.words.container, word)
    if len(current.words.container) == 0 {
        current.next.previous = current.previous
        current.previous.next = current.next
    }
}

func (this *LinkedListSortedByFrequency) getFrontValue() string {
    return this.sentinelHead.next.words.firstElement()
}

func (this *LinkedListSortedByFrequency) getBackValue() string {
    return this.sentinelTail.previous.words.firstElement()
}

func (this *LinkedListSortedByFrequency) isEmpty() bool {
    return this.sentinelHead.next == this.sentinelTail && this.sentinelTail.previous == this.sentinelHead
}

func Ternary[T any](condition bool, first T, second T) T {
    if condition {
        return first
    }
    return second
}
