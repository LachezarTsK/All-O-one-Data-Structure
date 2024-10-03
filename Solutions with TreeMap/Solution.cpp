
#include <map>
#include <string>
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

class AllOne {

    inline  static const string NOT_FOUND = "";
    unordered_map<string, int> wordToFrequency;
    map<int, unordered_set<string>> frequencyToWords;

public:
    AllOne() = default;

    void inc(const string& word) {
        int previousFrequency = wordToFrequency.contains(word) ? wordToFrequency[word] : 0;
        int newFrequency = previousFrequency + 1;

        wordToFrequency[word] = newFrequency;
        frequencyToWords[newFrequency].insert(word);
        if (previousFrequency == 0) {
            return;
        }

        frequencyToWords[previousFrequency].erase(word);
        if (frequencyToWords[previousFrequency].empty()) {
            frequencyToWords.erase(previousFrequency);
        }
    }

    void dec(const string& word) {
        int previousFrequency = wordToFrequency[word];
        int newFrequency = previousFrequency - 1;

        if (newFrequency > 0) {
            wordToFrequency[word] = newFrequency;
            frequencyToWords[newFrequency].insert(word);
        }
        else {
            wordToFrequency.erase(word);
        }

        frequencyToWords[previousFrequency].erase(word);
        if (frequencyToWords[previousFrequency].empty()) {
            frequencyToWords.erase(previousFrequency);
        }
    }

    string getMaxKey() const {
        if (frequencyToWords.empty()) {
            return NOT_FOUND;
        }
        string wordWithMaxFrequency = *(--frequencyToWords.cend())->second.cbegin();
        return wordWithMaxFrequency;
    }

    string getMinKey() const {
        if (frequencyToWords.empty()) {
            return NOT_FOUND;
        }
        string wordWithMinFrequency = *frequencyToWords.cbegin()->second.cbegin();
        return wordWithMinFrequency;
    }
};
