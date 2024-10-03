
using System;
using System.Collections.Generic;

public class AllOne
{
    private static readonly string NOT_FOUND = "";
    private static readonly int INCREASE = 1;
    private static readonly int DECREASE = -1;

    private readonly Dictionary<String, WordNode> wordToNode = new Dictionary<String, WordNode>();
    private readonly LinkedListSortedByFrequency listFrequencies = new LinkedListSortedByFrequency();

    public void Inc(string word)
    {
        WordNode? current = null;
        if (wordToNode.ContainsKey(word))
        {
            current = wordToNode[word];
            wordToNode.Remove(word);
        }
        WordNode newWordNode = listFrequencies.UpdateList(current, word, INCREASE);
        wordToNode.Add(word, newWordNode);
    }

    public void Dec(string word)
    {
        WordNode? current = null;
        if (wordToNode.ContainsKey(word))
        {
            current = wordToNode[word];
            wordToNode.Remove(word);
        }

        WordNode? newWordNode = listFrequencies.UpdateList(current, word, DECREASE);
        if (newWordNode != null)
        {
            wordToNode.Add(word, newWordNode);
        }
    }

    public string GetMaxKey()
    {
        return !listFrequencies.IsEmpty() ? listFrequencies.GetBackValue() : NOT_FOUND;
    }

    public string GetMinKey()
    {
        return !listFrequencies.IsEmpty() ? listFrequencies.GetFrontValue() : NOT_FOUND;
    }
}

class WordNode
{
    public int frequency;
    public WordNode? previous = null;
    public WordNode? next = null;
    public HashSet<string> words = new HashSet<string>();

    public WordNode(string word)
    {
        words.Add(word);
    }

    public WordNode(int frequency)
    {
        this.frequency = frequency;
    }
}

class LinkedListSortedByFrequency
{

    private readonly WordNode sentinelHead = new WordNode(int.MinValue);
    private readonly WordNode sentinelTail = new WordNode(int.MaxValue);

    public LinkedListSortedByFrequency()
    {
        sentinelHead.next = sentinelTail;
        sentinelTail.previous = sentinelHead;
    }

    public WordNode? UpdateList(WordNode current, string word, int changeInFrequency)
    {
        ReviseForPreviousFrequency(current, word);
        int previouFrequency = current != null ? current.frequency : 0;
        int newFrequency = previouFrequency + changeInFrequency;

        if (newFrequency <= 0)
        {
            return null;
        }
        if (current == null && sentinelHead.next.frequency == newFrequency)
        {
            sentinelHead.next.words.Add(word);
            return sentinelHead.next;
        }
        if (current != null && current.next.frequency == newFrequency)
        {
            current.next.words.Add(word);
            return current.next;
        }
        if (current != null && current.previous.frequency == newFrequency)
        {
            current.previous.words.Add(word);
            return current.previous;
        }

        return InsertNode(current, word, newFrequency);
    }

    private void ReviseForPreviousFrequency(WordNode? current, String word)
    {
        if (current == null)
        {
            return;
        }

        current.words.Remove(word);
        if (current.words.Count == 0)
        {
            current.next.previous = current.previous;
            current.previous.next = current.next;
        }
    }

    private WordNode InsertNode(WordNode current, String word, int newFrequency)
    {
        WordNode? nodeToPreceedNewNode = null;
        if (current == null)
        {
            nodeToPreceedNewNode = sentinelHead;
        }
        else if (current.words.Count == 0 || current.frequency > newFrequency)
        {
            nodeToPreceedNewNode = current.previous;
        }
        else if (current.frequency < newFrequency)
        {
            nodeToPreceedNewNode = current;
        }

        WordNode newNode = new WordNode(word);
        newNode.frequency = newFrequency;

        newNode.next = nodeToPreceedNewNode.next;
        newNode.previous = nodeToPreceedNewNode;
        newNode.next.previous = newNode;
        newNode.previous.next = newNode;

        return newNode;
    }

    public string GetFrontValue()
    {
        return sentinelHead.next.words.First();
    }

    public string GetBackValue()
    {
        return sentinelTail.previous.words.First();
    }

    public bool IsEmpty()
    {
        return sentinelHead.next == sentinelTail && sentinelTail.previous == sentinelHead;
    }
}
