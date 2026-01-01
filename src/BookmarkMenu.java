import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class BookmarkMenu extends JFrame {

    private JPanel mainPn;
    public JTextField promptField;
    public JTextField wordField;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JButton clearBtn;
    private JButton savedWordsBtn;

    private final GUI menu;

    public BookmarkMenu(GUI menu) {
        this.menu = menu;
        setContentPane(mainPn);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Bookmarks");
        setPreferredSize(new Dimension(400,300));
        pack();

        cancelBtn.addActionListener(_ -> setVisible(false));
        saveBtn.addActionListener(_ -> saveWord());
        clearBtn.addActionListener(_ -> clearBookmarks());
        savedWordsBtn.addActionListener(_ -> menu.swm.setVisible(true));

        promptField.addActionListener(_ -> wordField.requestFocus());
        wordField.addActionListener(_ -> saveWord());
    }

    private void saveWord() {
        String prompt = promptField.getText();
        String word = wordField.getText();
        File promptFile = new File("savedPrompts.txt");
        File wordFile = new File("savedWords.txt");

        try {
            if (prompt.isEmpty() || word.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill out both text fields.");
            } else if (!word.contains(prompt)) {
                JOptionPane.showMessageDialog(this, "Your word does not contain the prompt. Not saving.");
            } else if (!menu.dict.contains(word)) {
                JOptionPane.showMessageDialog(this, "Your word is not in the Word Bomb dictionary. Not saving.");
            } else if (menu.swm.wordList.contains(word)) {
                JOptionPane.showMessageDialog(this, "Your word is already bookmarked. Not saving.");
            } else if (promptFile.exists() && !promptFile.isDirectory() && wordFile.exists() && !wordFile.isDirectory()) {
                BufferedWriter promptBW = new BufferedWriter(new FileWriter(promptFile, true));
                promptBW.write(prompt + ";");
                promptBW.close();
                BufferedWriter wordBW = new BufferedWriter(new FileWriter(wordFile, true));
                wordBW.write(word + ";");
                wordBW.close();
                JOptionPane.showMessageDialog(this, "Saved! :D\nPrompt: " + prompt + "\nWord: " + word);
            } else {
                BufferedWriter promptBW = new BufferedWriter(new FileWriter(promptFile, StandardCharsets.UTF_8));
                promptBW.write(prompt + ";");
                promptBW.close();
                BufferedWriter wordBW = new BufferedWriter(new FileWriter(wordFile, StandardCharsets.UTF_8));
                wordBW.write(word + ";");
                wordBW.close();
                JOptionPane.showMessageDialog(this, "Saved! :D\nPrompt: " + prompt + "\nWord: " + word);
            }

            menu.swm.loadList();
            wordField.setText("");
            promptField.requestFocus();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearBookmarks() {
        try {
            PrintWriter pws = new PrintWriter("savedWords.txt", StandardCharsets.UTF_8);
            PrintWriter pwp = new PrintWriter("savedPrompts.txt", StandardCharsets.UTF_8);
            pws.write("");
            pwp.write("");
            pws.close();
            pwp.close();
            JOptionPane.showMessageDialog(this, "Bookmarks have been cleared.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
