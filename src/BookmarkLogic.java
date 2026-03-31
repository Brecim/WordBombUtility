import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class BookmarkLogic {

    private final GUI menu;

    public BookmarkLogic(GUI menu) {
        this.menu = menu;

        menu.bookmarkWordField.addActionListener(_ -> saveWord());
        menu.clearBookmarkBtn.addActionListener(_ -> clearBookmarks());
    }

    public void saveWord() {
        String word = menu.bookmarkWordField.getText();

        try {
            if (word.isEmpty()) {
                JOptionPane.showMessageDialog(menu, "Please enter a word.");
            } else if (!menu.dict.contains(word)) {
                JOptionPane.showMessageDialog(menu, "Your word is not in the Word Bomb dictionary. Not saving.");
            } else if (menu.swl.wordList.contains(word)) {
                JOptionPane.showMessageDialog(menu, "Your word is already bookmarked. Not saving.");
            } else if (menu.wordFile.exists() && !menu.wordFile.isDirectory()) {
                BufferedWriter wordBW = new BufferedWriter(new FileWriter(menu.wordFile, true));
                wordBW.write(word + ";");
                wordBW.close();
                JOptionPane.showMessageDialog(menu, "Saved! :D\nWord: " + word);
            } else {
                BufferedWriter wordBW = new BufferedWriter(new FileWriter(menu.wordFile, StandardCharsets.UTF_8));
                wordBW.write(word + ";");
                wordBW.close();
                JOptionPane.showMessageDialog(menu, "Saved! :D\nWord: " + word);
            }

            menu.swl.loadList();
            menu.bookmarkWordField.setText("");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(menu, "An error has occured: " + e);
            System.exit(69);
        }
    }

    public void clearBookmarks() {
        try {
            PrintWriter pws = new PrintWriter(menu.wordFile, StandardCharsets.UTF_8);
            pws.write("");
            pws.close();
            menu.savesPane.setText("Please save a word first!");
            JOptionPane.showMessageDialog(menu, "Bookmarks have been cleared.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(menu, "An error has occured: " + e);
            System.exit(69);
        }

    }
}