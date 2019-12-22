import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static String readFileAsString(String pathToFile) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathToFile)));
    }

    public static void main(String[] args) {
        /*if (args.length == 0) {
            System.out.println("Please provide input file");
            System.exit(0);
        }*/

        // String pathToFile = args[0];
        String pathToFile = "./data/final.txt";
        try {
            // Read file as String
            String text = readFileAsString(pathToFile);

            // Count words
            String textForWords = text.replaceAll("[^a-zA-Z]", " ");
            String[] words = textForWords.split("[\\s]+");
            int wordsCnt = words.length;

            // Count sentences
            String[] sentences = text.split("[.!?][\\s]");
            int sentCnt = sentences.length;

            // Count characters
            String chars = text.replaceAll("\\s", "");
            int charsCnt = chars.length();

            // Count syllables
            int[] syllPerWordCnt = new int[wordsCnt];
            for (int i = 0; i < wordsCnt; i++) {
                syllPerWordCnt[i] = 0;

                // Count the number of vowels in the word
                char[] ch = words[i].toLowerCase().toCharArray();
                int len = ch.length;
                boolean isPreviousCharVowel = false;
                for (int j = 0; j < len; j++) {
                    //  Do not count double-vowels
                    if (isPreviousCharVowel) {
                        if (ch[j] != 'a' && ch[j] != 'e' && ch[j] != 'i' && ch[j] != 'o' && ch[j] != 'u' && ch[j] != 'y') {
                            isPreviousCharVowel = false;
                        }
                        continue;
                    } else if (ch[j] == 'a' || ch[j] == 'e' || ch[j] == 'i' || ch[j] == 'o' || ch[j] == 'u' || ch[j] == 'y') {
                        syllPerWordCnt[i]++;
                        isPreviousCharVowel = true;
                    }
                }
                // If the last letter in the word is 'e' do not count it as a vowel
                if (ch[len - 1] == 'e') {
                    syllPerWordCnt[i]--;
                }
                // If there are no vowels in a word then this word considered as one-syllable
                if (syllPerWordCnt[i] == 0) {
                    syllPerWordCnt[i] = 1;
                }
            }
            int syllCnt = 0;
            for (int syll : syllPerWordCnt) {
                syllCnt += syll;
            }

            // Count polysyllables
            int polysyllCnt = 0;
            for (int i = 0; i < wordsCnt; i++) {
                if (syllPerWordCnt[i] > 2) {
                    polysyllCnt++;
                }
            }

            // Calculate Automated readability index and level
            double ARI = ((4.71 * (charsCnt / (double) wordsCnt)) + (0.5 * (wordsCnt / (double) sentCnt))) - 21.43;
            long ARILevel = Math.round(ARI) + 5;

            // Calculate Flesch–Kincaid readability tests and level
            double FKRT = (0.39 * (wordsCnt / (double) sentCnt) + 11.8 * (syllCnt / (double) wordsCnt) - 15.59);
            long FKRTLevel = Math.round(FKRT) + 5;

            // Calculate SMOG index and level
            double SMOG = (1.043 * Math.sqrt(polysyllCnt * 30 / (double) sentCnt) + 3.1291);
            long SMOGLevel = Math.round(SMOG) + 5;

            // Calculate Coleman–Liau index and level
            double CLI = (0.0588 * (charsCnt / (double) wordsCnt * 100) - 0.296 * (sentCnt / (double) wordsCnt * 100) - 15.8);
            long CLILevel = Math.round(CLI) + 5;

            // Calculate average level
            double Level = (ARILevel + FKRTLevel + SMOGLevel + CLILevel) / 4.0;

            // Print output
            System.out.println("The text is:");
            System.out.println(text);
            System.out.println("Words: " + wordsCnt);
            System.out.println("Sentences: " + sentCnt);
            System.out.println("Characters: " + charsCnt);
            System.out.println("Syllables: " + syllCnt);
            System.out.println("Polysyllables: " + polysyllCnt);

            System.out.printf("Automated Readability Index: %.2f (about %d year olds).%n", ARI, ARILevel);
            System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d year olds).%n", FKRT, FKRTLevel);
            System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d year olds).%n", SMOG, SMOGLevel);
            System.out.printf("Coleman–Liau index: %.2f (about %d year olds).%n", CLI, CLILevel);
            System.out.printf("This text should be understood in average by %.1f year olds.", Level);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
    }
}