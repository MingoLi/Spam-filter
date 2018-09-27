// The "value" part in hashmap's key-value pair
// Stores the count and probability of that word
class Word {
    public int hamCount;
    public int spamCount;

    public double probHam;     // P(W|Ham)
    public double probSpam;    // P(W|Spam)

    public Word(int h, int s) {
        hamCount = h;
        spamCount = s;
    }

    // for debug usage
    public String toString() {
        return "HamCount: " + hamCount + "   SpamCount: " + spamCount;
    }
}
