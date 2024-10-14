import java.util.Scanner;

/**
 * Represents an element in a sentence, such as a word or punctuation mark.
 */
interface SentenceElement {
    /**
     * Returns the string representation of the sentence element.
     *
     * @return the string representation of the sentence element
     */
    String toString();
}

/**
 * Represents a letter in a word.
 */
class Letter {
    private char character;

    /**
     * Constructs a Letter with the given character.
     *
     * @param character the character that represents the letter
     */
    public Letter(char character) {
        this.character = character;
    }

    /**
     * Returns the character of this letter.
     *
     * @return the character
     */
    public char getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return Character.toString(character);
    }
}

/**
 * Represents a word composed of letters.
 */
class Word implements SentenceElement {
    private Letter[] letters;

    /**
     * Constructs a Word from a given string.
     *
     * @param word the string representing the word
     */
    public Word(String word) {
        letters = new Letter[word.length()];
        for (int i = 0; i < word.length(); i++) {
            letters[i] = new Letter(word.charAt(i));
        }
    }

    /**
     * Returns the array of letters in this word.
     *
     * @return the array of letters
     */
    public Letter[] getLetters() {
        return letters;
    }

    @Override
    public String toString() {
        StringBuilder wordStr = new StringBuilder();
        for (Letter letter : letters) {
            wordStr.append(letter.getCharacter());
        }
        return wordStr.toString();
    }
}

/**
 * Represents a punctuation mark.
 */
class PunctuationMark implements SentenceElement {
    private char mark;

    /**
     * Constructs a PunctuationMark with the given character.
     *
     * @param mark the punctuation character
     */
    public PunctuationMark(char mark) {
        this.mark = mark;
    }

    /**
     * Returns the punctuation mark.
     *
     * @return the punctuation mark
     */
    public char getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return Character.toString(mark);
    }
}

/**
 * Represents a sentence composed of words and punctuation marks.
 */
class Sentence {
    private SentenceElement[] elements;

    /**
     * Constructs a Sentence with the given elements.
     *
     * @param elements the array of SentenceElement (words, punctuation marks)
     */
    public Sentence(SentenceElement[] elements) {
        this.elements = elements;
    }

    /**
     * Returns the elements of the sentence.
     *
     * @return the array of SentenceElement
     */
    public SentenceElement[] getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder sentenceStr = new StringBuilder();
        for (SentenceElement element : elements) {
            sentenceStr.append(element.toString());
            if (element instanceof Word) {
                sentenceStr.append(" ");
            }
        }
        return sentenceStr.toString().trim();
    }
}

/**
 * Represents a text composed of sentences.
 */
class Text {
    private Sentence[] sentences;

    /**
     * Constructs a Text from an array of sentences.
     *
     * @param sentences the array of sentences
     */
    public Text(Sentence[] sentences) {
        this.sentences = sentences;
    }

    /**
     * Returns the sentences of the text.
     *
     * @return the array of sentences
     */
    public Sentence[] getSentences() {
        return sentences;
    }

    @Override
    public String toString() {
        StringBuilder textStr = new StringBuilder();
        for (Sentence sentence : sentences) {
            textStr.append(sentence).append(" ");
        }
        return textStr.toString().trim();
    }
}

/**
 * Main class that contains methods to process text, split it into sentences and words, 
 * and count word occurrences in sentences.
 */
public class Main {

    /**
     * Entry point of the program.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Введення тексту
        StringBuffer text = new StringBuffer();
        while (text.length() == 0) {
            System.out.println("Please enter the text:");
            String inputText = scanner.nextLine().trim();
            if (!inputText.isEmpty()) {
                text = new StringBuffer(inputText);
                if (!endsWithPunctuation(text)) {
                    text.append('.');
                    System.out.println("Text was missing a sentence-ending punctuation, a period was added.");
                }
            } else {
                System.out.println("Text cannot be empty. Please try again.");
            }
        }

        // Введення кількості слів
        int wordCount = 0;
        while (wordCount <= 0) {
            System.out.println("Enter the number of words to search for (positive integer):");
            if (scanner.hasNextInt()) {
                wordCount = scanner.nextInt();
                if (wordCount <= 0) {
                    System.out.println("The number of words must be positive. Please try again.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next(); 
            }
        }

        scanner.nextLine(); // Чистимо вхідний потік

        // Введення слів для пошуку
        Word[] words = new Word[wordCount];
        for (int i = 0; i < wordCount; i++) {
            while (true) {
                System.out.println("Enter word #" + (i + 1) + ":");
                String inputWord = scanner.nextLine().trim();
                if (!inputWord.isEmpty()) {
                    words[i] = new Word(inputWord);
                    break;
                } else {
                    System.out.println("Word cannot be empty. Please enter again.");
                }
            }
        }

        // Розбиття тексту на речення та аналіз
        Sentence[] sentences = splitIntoSentences(text.toString());
        Text fullText = new Text(sentences);

        // Підрахунок кількості входжень слів у реченнях
        countWordOccurrencesInSentences(fullText, words);

        scanner.close();
    }

    /**
     * Checks if the given text ends with a punctuation mark.
     * 
     * @param text the text to check
     * @return true if the text ends with punctuation, false otherwise
     */
    public static boolean endsWithPunctuation(StringBuffer text) {
        char lastChar = text.charAt(text.length() - 1);
        return lastChar == '.' || lastChar == '!' || lastChar == '?';
    }

    /**
     * Splits the given text into sentences.
     * 
     * @param text the text to split
     * @return an array of Sentence objects
     */
    public static Sentence[] splitIntoSentences(String text) {
        String[] sentenceStrings = text.split("(?<=[.!?])\\s*");
        Sentence[] sentences = new Sentence[sentenceStrings.length];
    
        for (int i = 0; i < sentenceStrings.length; i++) {
            String sentenceStr = sentenceStrings[i].trim();
            
            // Регулярний вираз для розбиття рядка на слова та розділові знаки
            String[] wordStrings = sentenceStr.split("(?<=[.,!?\"'])|(?=[.,!?\"'])|\\s+");
            SentenceElement[] elements = new SentenceElement[wordStrings.length];
    
            for (int j = 0; j < wordStrings.length; j++) {
                String word = wordStrings[j].trim();
                if (!word.isEmpty()) {
                    if (isPunctuationMark(word)) {
                        elements[j] = new PunctuationMark(word.charAt(0));
                    } else {
                        elements[j] = new Word(word);
                    }
                }
            }
    
            sentences[i] = new Sentence(elements);
        }
    
        return sentences;
    }

    /**
     * Helper method to check if a string is a punctuation mark.
     * 
     * @param str the string to check
     * @return true if the string is a punctuation mark, false otherwise
     */
    private static boolean isPunctuationMark(String str) {
        return str.length() == 1 && ".,!?\"'".indexOf(str.charAt(0)) != -1;
    }

    /**
     * Counts and prints the number of sentences each word appears in.
     * 
     * @param text the Text object containing sentences
     * @param words the array of words to search for
     */
    public static void countWordOccurrencesInSentences(Text text, Word[] words) {
        Sentence[] sentences = text.getSentences();

        System.out.println("\nWord(s):");
        for (Word word : words) {
            int count = 0;
            for (Sentence sentence : sentences) {
                if (containsWord(sentence, word)) {
                    count++;
                }
            }
            System.out.println("\"" + word + "\" is in " + count + " sentence(s)");
        }
    }

    /**
     * Checks if a sentence contains the specified word, case insensitive.
     * 
     * @param sentence the Sentence to check
     * @param word the Word to look for
     * @return true if the sentence contains the word, false otherwise
     */
    public static boolean containsWord(Sentence sentence, Word word) {
        SentenceElement[] elements = sentence.getElements();
        for (SentenceElement element : elements) {
            if (element instanceof Word && ((Word) element).toString().equalsIgnoreCase(word.toString())) {
                return true;
            }
        }
        return false;
    }
}

