import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI extends JFrame {
    private JPanel mainPn;
    public JTextField promptField;
    private JButton findBtn;
    private JTextPane resultPane;
    private JComboBox wordLengthBox;
    private JButton savedWordsBtn;
    private JButton bookmarkBtn;

    public ArrayList<String> dict = new ArrayList<>();
    private int length = 1000;

    private final BookmarkMenu bm = new BookmarkMenu(this);
    private final SavedWordsMenu swm = new SavedWordsMenu();

    public GUI() {
        setContentPane(mainPn);
        setTitle("Word Bomb Utility");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 500));
        pack();

        loadDict();
        init();
    }

    private void init() {
        wordLengthBox.addItem(10);
        wordLengthBox.addItem(15);
        wordLengthBox.addItem(20);
        wordLengthBox.addItem("Max");

        findBtn.addActionListener(_ -> find(promptField.getText()));
        // Jiz snima klavesy vcetne Enteru
        promptField.addActionListener(_ -> find(promptField.getText()));
        // Automaticky bere prednost pri spusteni aplikace
        promptField.requestFocus();
        bookmarkBtn.addActionListener(_ -> bookmarkMenu());
        savedWordsBtn.addActionListener(_ -> swm.setVisible(true));
    }

    private void checkLength() {
        String selectedLength = wordLengthBox.getSelectedItem().toString();
        switch (selectedLength) {
            case "10" -> length = 10;
            case "15" -> length = 15;
            case "20" -> length = 20;
            case "Max" -> length = 1000;
            case null -> length = 1000;
            default -> throw new IllegalStateException("Unexpected value: " + selectedLength);
        }
    }

    private void loadDict() {
        try {
            Scanner s = new Scanner(new FileReader("dict.txt"));
            while (s.hasNextLine()) {
                dict.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void find(String prompt) {
        int found = 0;
        ArrayList<String> list = new ArrayList<>();
        checkLength();

        resultPane.setText("");
        for (int i = dict.size() - 1; i >= 0; i--) {
            if (dict.get(i).contains(prompt) && dict.get(i).length() <= length) {
                list.add(dict.get(i));
                found++;
            }
            if (found == 15) {
                break;
            }
        }
        // AI formatovani, jupiiiii
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body style='font-family: monospace; color: white;'>");

        for (String word : list) {
            String highlightedWord = word.replace(prompt, "</font><font color='#ff9999'>" + prompt + "</font><font color='white'>");

            htmlContent.append("<font color='white'>").append(highlightedWord).append("</font><br>");
        }

        htmlContent.append("</body></html>");

        resultPane.setContentType("text/html");
        resultPane.setText(htmlContent.toString());
    }

    private void bookmarkMenu() {
        bm.promptField.setText(promptField.getText());
        bm.wordField.setText("");
        bm.setVisible(true);
        bm.promptField.requestFocus();
    }

    void main() {
        setVisible(true);
    }
}
