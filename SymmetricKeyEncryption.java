import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class SymmetricKeyEncryption {
    public static void main(String[] args) {

        // Get the message to encrypt from the user
        Scanner messageScanner = new Scanner(System.in);
        System.out.println("Enter the message to be encrypted:");
        String message = String.valueOf(messageScanner.nextLine());
        String upperMessage = message.replaceAll("\\s+", "").toUpperCase();

        // Get the cypher key from the user
        // Should be an even amount of characters, and only range from 0-5
        // This will be assumed for now, but will be coded in the future
        Scanner keyScanner = new Scanner(System.in);
        System.out.println("Enter the cypher key -");
        System.out.println("(Must be an even number of digits, and only integers from 0-5):");
        String cypherKey = String.valueOf(keyScanner.next());

        // Obtain the value of the polybius key
        String columnarTransposition = polybiusKey(String.valueOf(cypherKey));
        char[][] messageSplit = messageArray(upperMessage);

        // Encrypt the message based off the message, message length, and cypher key
        String encryptedMessage = encrypt(messageSplit, upperMessage.length(), columnarTransposition);

        // Print out the results
        System.out.println("\nINPUTS:");
        System.out.println("Key: " + cypherKey);
        System.out.println("Plaintext Message: " + message);

        System.out.println("\nOutputs:");
        System.out.println("Columnar Transposition Key (using Polybius Square): " + columnarTransposition);
        System.out.println("Ciphertext Message (using Columnar Transposition): " + encryptedMessage);

        // -----------------------------------------------------------------
        // TEST CASE:
        //
        // User Input
        // Message: a secret message
        // Key: 220244222300
        //
        // SHOULD RETURN:
        // Columnar Transposition Key (using Polybius Square): ARCANE
        // Ciphertext Message (using Columnar Transposition): ATGCSEEEARSSME
        // -----------------------------------------------------------------
    }


    public static String polybiusKey(String index) {

        // Preset polybius key, to determine the columnar transposition
        char[][] key = {
                {'E','Y','O','P','D','9'},
                {'2','H','Q','X','1','I'},
                {'R','3','A','J','8','S'},
                {'F','0','N','4','G','5'},
                {'Z','B','U','V','C','T'},
                {'M','7','K','W','6','L'},
        };

        String[] splitIndex = index.split("");
        StringBuilder returnString = new StringBuilder();

        // Build the new string based off of the key
        for (int i = 0; i < splitIndex.length; i += 2) {
            int x = Integer.parseInt(splitIndex[i]);
            int y = Integer.parseInt(splitIndex[i + 1]);
            returnString.append(key[y][x]);
        }

        return returnString.toString();
    }


    public static char[][] messageArray(String key) {

        // Split the index into individual characters
        String[] splitKey = key.split("");

        // Obtain how many columns the split key will need
        int arrayLength;
        if (splitKey.length % 6 == 0) {
            arrayLength = (splitKey.length / 6);
        }
        else {
            arrayLength = (splitKey.length / 6) + 1;
        }

        // Create and add to the new character array
        char[][] charArray = new char[arrayLength][6];
        int counter = 0;
        for (int i = 0; i < splitKey.length; i++) {
            charArray[(i / 6)][counter % 6] = splitKey[i].charAt(0);
            counter++;
        }

        return charArray;
    }

    public static String encrypt(char[][] charArray, int wordLength, String cypher) {

        // Split the cypher into an array of individual strings
        String[] splitCypher = cypher.split("");

        // Create a 2d string array, and add the cypher, and its index
        String[][] key = new String[splitCypher.length][2];
        for (int i = 0; i < splitCypher.length; i++) {
            key[i][0] = splitCypher[i];
            key[i][1] = String.valueOf(i);
        }

        // Sort the 2d array based off the items in the column
        Arrays.sort(key, Comparator.comparing(o -> o[0]));

        // Obtain the column order from the second index of the key
        int[] order = new int[splitCypher.length];
        for (int i = 0; i < splitCypher.length; i++) {
            order[i] = Integer.parseInt(key[i][1]);
        }

        // Obtain the depth of the array
        int arrayDepth;
        if (wordLength % 6 == 0) {
            arrayDepth = (wordLength / 6);
        }
        else {
            arrayDepth = (wordLength / 6) + 1;
        }

        // Build the final encrypted string
        StringBuilder returnString = new StringBuilder();

        // Traverse the character array, going through depth before length
        for (int i = 0; i < splitCypher.length; i++) {
            for (int j = 0; j < arrayDepth; j++) {

                // If the character is "null" (returns 0), break
                if (charArray[j][order[i]] == 0) {
                    break;
                }

                // Else, append the string builder in the order determined in the "order" array
                else {
                    returnString.append(charArray[j][order[i]]);
                }
            }
        }

        return returnString.toString();
    }
}