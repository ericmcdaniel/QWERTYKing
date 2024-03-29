import java.io.FileReader;
import java.util.*;
public class Words
{
    private List<String>  dictionary;
    private List<String>  inGameWords;
    private List<String>  userTypedWords;
    private List<Integer> errors;
    private int           gameSize;
    private boolean       randomCaps;
    private boolean       activeGame = true;

    public Words(FileReader wordsFile, int size, boolean caps)
    {
        dictionary  = loadFile(wordsFile);
        inGameWords = new ArrayList<String>();
        gameSize    = size;
        randomCaps  = caps;
    }

    // Basic getters for quick info
    public int     getSize()           { return gameSize;          }
    public int     getDictionarySize() { return dictionary.size(); }
    public boolean randomCaps()        { return randomCaps;        }
    public boolean continueGame()      { return activeGame;        }

    // Copy the textfile's words into memory. Parse special characters
    private static List<String> loadFile(FileReader wordsFile)
    {
        // Instantiate reading objects
        Scanner      reader = new Scanner(wordsFile);
        List<String> words  = new ArrayList<String>();

        // Add words into array and return object
        while (reader.hasNext())
            words.add(reader.next().replaceAll("[^\\w\\s]", "").trim());
        return words;
    } // End loadFile()

    // Print a random word to stdout. Create a new line every six words
    public void fillAndPrintRandomWords()
    {
        System.out.printf("► ");
        for (int i = 0; i < gameSize; ++i)
        {
            if (i % 6 == 0 && i > 1)
                System.out.print("\n  ");
            inGameWords.add(getRandomWord());
            System.out.printf("%s ", inGameWords.get(i));
        }
        System.out.printf("\n    ↳ ");
    } // End fillAndPrintRandomWords()

    // Return a random words from dictionary, including random capitalization if specified
    private String getRandomWord()
    {
        // Return random word from file
        String word = dictionary.get(new Random().nextInt(dictionary.size())).toLowerCase();
        return (randomCaps && new Random().nextInt(3) != 0) ? (word.substring(0, 1).toUpperCase() + word.substring(1)) : word;
    }

    // Store user's input into object for comparison with dictionary
    public void getUserEntry()
    {
        userTypedWords = fillUserInputArray(new Scanner(System.in).nextLine());
        activeGame     = !quitGame();
    }

    // Read user's input and store into memory
    private static List<String> fillUserInputArray(String userTypedWords)
    {
        // Apply single line from user into ArrayList
        List<String> userInput = new ArrayList<String>();
        Scanner           scan = new Scanner(userTypedWords);
        while (scan.hasNext())
            userInput.add(scan.next().trim());
        return userInput;
    } // End fillUserInputArray

    // Verify if user's input word count matches number of words assigned
    public boolean validInput()
    {
        return (userTypedWords.size() == gameSize);
    }

    // Compare user's input with dictionary, add to list if misspelled
    private void getUserErrors()
    {
        // Helper method to store index values of the user error(s)
        errors = new ArrayList<Integer>();
        for (int i = 0; i < inGameWords.size(); ++i)
            if (!(inGameWords.get(i).equals(userTypedWords.get(i))))
                errors.add((Integer) i);
    } // End getUserErrors()

    // Print to stdout what errors the user mistyped
    public void printAnyErrors()
    {
        getUserErrors();
        if (errors.size() == 0)
            System.out.println("\nGreat job! You typed the selection perfectly!\n---------------------------------------------\n");
        else
        {
            System.err.printf("\n|     You accidentally made %-2d error%s    |\n|             Word |               Input |\n"
                            + "|------------------|---------------------|\n", errors.size(),  (errors.size() != 1) ? "s" : " ");
            for (int i = 0; i < errors.size(); ++i)
                System.err.printf("|%17s |%20s |\n", inGameWords.get(errors.get(i)), userTypedWords.get(errors.get(i)));
            System.err.printf("|------------------|---------------------|\n\n");
        }
    } // End printAnyErrors()

    // Require user to type "quit" exactly to quit the game
    private boolean quitGame()
    {
        return userTypedWords.get(0).trim().equalsIgnoreCase("quit");
    }

    // Clear the words from the dictionary for the next round
    public void clear()
    {
        // The statement resolves a weird bug where the object prints things
        // in improper order. I'm not sure why, however this solution works.
        try { Thread.sleep(1); } catch (InterruptedException ex) {}
        inGameWords.removeAll(inGameWords);
    }
}