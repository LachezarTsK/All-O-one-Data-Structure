
#include <memory>
#include <string>
#include <limits>
#include <unordered_set>
#include <unordered_map>
using namespace std;

/*
 The code will run faster with ios::sync_with_stdio(0).
 However, this should not be used in production code and interactive problems.
 In this particular problem, it is ok to apply ios::sync_with_stdio(0).

 Many of the top-ranked C++ solutions for time on leetcode apply this trick,
 so, for a fairer assessment of the time percentile of my code
 you could outcomment the lambda expression below for a faster run.
*/

/*
 const static auto speedup = [] {
	ios::sync_with_stdio(0);
	return nullptr;
 }();
*/

struct WordNode {

    int frequency = 0;
    shared_ptr<WordNode> previous;
    shared_ptr<WordNode> next;
    unordered_set<string> words;

    explicit WordNode(const string& word) {
        words.insert(word);
    }

    explicit WordNode(int frequency): frequency{ frequency } {}
};

class LinkedListSortedByFrequency {

    const shared_ptr<WordNode> sentinelHead = make_shared<WordNode>(numeric_limits<int>::min());
    const shared_ptr<WordNode> sentinelTail = make_shared<WordNode>(numeric_limits<int>::max());

public:
    LinkedListSortedByFrequency() {
        sentinelHead->next = sentinelTail;
        sentinelTail->previous = sentinelHead;
    }

    shared_ptr<WordNode> updateList(shared_ptr<WordNode> current, const string& word, int changeInFrequency) const {

        reviseForPreviousFrequency(current, word);
        int previouFrequency = current != nullptr ? current->frequency : 0;
        int newFrequency = previouFrequency + changeInFrequency;

        if (newFrequency <= 0) {
            return nullptr;
        }
        if (current == nullptr && sentinelHead->next->frequency == newFrequency) {
            sentinelHead->next->words.insert(word);
            return sentinelHead->next;
        }
        if (current != nullptr && current->next->frequency == newFrequency) {
            current->next->words.insert(word);
            return current->next;
        }
        if (current != nullptr && current->previous->frequency == newFrequency) {
            current->previous->words.insert(word);
            return current->previous;
        }

        return insertNode(current, word, newFrequency);
    }

private:
    void reviseForPreviousFrequency(shared_ptr<WordNode> current, const string& word) const {
        if (current == nullptr) {
            return;
        }
        current->words.erase(word);
        if (current->words.empty()) {
            current->next->previous = current->previous;
            current->previous->next = current->next;
        }
    }

    shared_ptr<WordNode> insertNode(shared_ptr<WordNode> current, const string& word, int newFrequency) const {
        shared_ptr< WordNode> nodeToPreceedNewNode;
        if (current == nullptr) {
            nodeToPreceedNewNode = sentinelHead;
        }
        else if (current->words.empty() || current->frequency > newFrequency) {
            nodeToPreceedNewNode = current->previous;
        }
        else if (current->frequency < newFrequency) {
            nodeToPreceedNewNode = current;
        }

        shared_ptr<WordNode> newNode{ make_shared<WordNode>(word) };
        newNode->frequency = newFrequency;

        newNode->next = nodeToPreceedNewNode->next;
        newNode->previous = nodeToPreceedNewNode;
        newNode->next->previous = newNode;
        newNode->previous->next = newNode;

        return newNode;
    }

public:
    string getFrontValue() const {
        return *sentinelHead->next->words.cbegin();
    }

    string getBackValue() const {
        return *sentinelTail->previous->words.cbegin();
    }

    bool isEmpty() const {
        return sentinelHead->next == sentinelTail && sentinelTail->previous == sentinelHead;
    }
};

class AllOne {

    inline static const string NOT_FOUND;
    static const int INCREASE = 1;
    static const int DECREASE = -1;

    unordered_map<string, shared_ptr<WordNode>> wordToNode;
    LinkedListSortedByFrequency listFrequencies;

public:
    AllOne() = default;

    void inc(const string& word) {
        shared_ptr<WordNode> current;
        if (wordToNode.contains(word)) {
            current = wordToNode[word];
            wordToNode.erase(word);
        }

        shared_ptr<WordNode> newWordNode = listFrequencies.updateList(current, word, INCREASE);
        wordToNode[word] = newWordNode;
    }

    void dec(const string& word) {
        shared_ptr<WordNode> current;
        if (wordToNode.contains(word)) {
            current = wordToNode[word];
            wordToNode.erase(word);
        }

        shared_ptr<WordNode> newWordNode = listFrequencies.updateList(current, word, DECREASE);
        if (newWordNode != nullptr) {
            wordToNode[word] = newWordNode;
        }
    }

    string getMaxKey() const {
        return !listFrequencies.isEmpty() ? listFrequencies.getBackValue() : NOT_FOUND;
    }

    string getMinKey() const {
        return !listFrequencies.isEmpty() ? listFrequencies.getFrontValue() : NOT_FOUND;
    }
};
