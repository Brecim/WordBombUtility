import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class BookmarkLogic {

    private final GUI menu;

    public BookmarkLogic(GUI menu) {
        this.menu = menu;

        menu.bookmarkPromptField.addActionListener(_ -> menu.bookmarkWordField.requestFocus());
        menu.bookmarkWordField.addActionListener(_ -> saveWord());
        menu.clearBookmarkBtn.addActionListener(_ -> clearBookmarks());
    }

    public void saveWord() {
        String prompt = menu.bookmarkPromptField.getText();
        String word = menu.bookmarkWordField.getText();
        File promptFile = new File("savedPrompts.txt");
        File wordFile = new File("savedWords.txt");

        try {
            if (prompt.isEmpty() || word.isEmpty()) {
                JOptionPane.showMessageDialog(menu, "Please fill out both text fields.");
            } else if (!word.contains(prompt)) {
                JOptionPane.showMessageDialog(menu, "Your word does not contain the prompt. Not saving.");
            } else if (!menu.dict.contains(word)) {
                JOptionPane.showMessageDialog(menu, "Your word is not in the Word Bomb dictionary. Not saving.");
            } else if (menu.swl.wordList.contains(word)) {
                JOptionPane.showMessageDialog(menu, "Your word is already bookmarked. Not saving.");
            } else if (promptFile.exists() && !promptFile.isDirectory() && wordFile.exists() && !wordFile.isDirectory()) {
                BufferedWriter promptBW = new BufferedWriter(new FileWriter(promptFile, true));
                promptBW.write(prompt + ";");
                promptBW.close();
                BufferedWriter wordBW = new BufferedWriter(new FileWriter(wordFile, true));
                wordBW.write(word + ";");
                wordBW.close();
                JOptionPane.showMessageDialog(menu, "Saved! :D\nPrompt: " + prompt + "\nWord: " + word);
            } else {
                BufferedWriter promptBW = new BufferedWriter(new FileWriter(promptFile, StandardCharsets.UTF_8));
                promptBW.write(prompt + ";");
                promptBW.close();
                BufferedWriter wordBW = new BufferedWriter(new FileWriter(wordFile, StandardCharsets.UTF_8));
                wordBW.write(word + ";");
                wordBW.close();
                JOptionPane.showMessageDialog(menu, "Saved! :D\nPrompt: " + prompt + "\nWord: " + word);
            }

            menu.swl.loadList();
            menu.bookmarkWordField.setText("");
            menu.bookmarkPromptField.requestFocus();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearBookmarks() {
        try {
            PrintWriter pws = new PrintWriter("savedWords.txt", StandardCharsets.UTF_8);
            PrintWriter pwp = new PrintWriter("savedPrompts.txt", StandardCharsets.UTF_8);
            pws.write("");
            pwp.write("");
            pws.close();
            pwp.close();
            menu.promptBox.removeAllItems();
            menu.savesPane.setText("Please save a word first!");
            JOptionPane.showMessageDialog(menu, "Bookmarks have been cleared.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
