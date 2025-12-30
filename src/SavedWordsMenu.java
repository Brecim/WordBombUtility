import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SavedWordsMenu extends JFrame {

    private JPanel mainPn;
    private JTextField searchField;
    private JTextPane resultPane;
    private JButton searchBtn;

    private final ArrayList<String> promptList = new ArrayList<>();
    private final ArrayList<String> wordList = new ArrayList<>();

    public SavedWordsMenu() {
        setContentPane(mainPn);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Saved Words");
        setPreferredSize(new Dimension(500,500));
        pack();

        loadList();
        searchBtn.addActionListener(_ -> search(searchField.getText()));
    }

    private void loadList() {
        try {
            Scanner sp = new Scanner(new FileReader("savedPrompts.txt"));
            Scanner sw = new Scanner(new FileReader("savedWords.txt"));
            sp.useDelimiter(";");
            sw.useDelimiter(";");
            while (sp.hasNext() && sw.hasNext()) {
                promptList.add(sp.next());
                wordList.add(sw.next());
            }
            sp.close();
            sw.close();
            System.out.println(promptList);
            System.out.println(wordList);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void search(String prompt) {
        resultPane.setText("");

        // Create a temporary list for results. Do not use the class 'wordList'.
        ArrayList<String> foundWords = new ArrayList<>();

        try {
            Scanner sp = new Scanner(new FileReader("savedPrompts.txt"));
            Scanner sw = new Scanner(new FileReader("savedWords.txt"));
            sp.useDelimiter(";");
            sw.useDelimiter(";");
            while (sp.hasNext() && sw.hasNext()) {
                String filePrompt = sp.next();
                if (sw.hasNext()) {
                    String fileWord = sw.next();
                    if (filePrompt.equals(prompt)) {
                        foundWords.add(fileWord);
                    }
                }
            }
            sp.close();
            sw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // AI formatovani, jupiiiii
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body style='font-family: monospace; color: white;'>");

        for (String word : foundWords) {
            String highlightedWord = word.replace(prompt, "</font><font color='#ff9999'>" + prompt + "</font><font color='white'>");

            htmlContent.append("<font color='white'>").append(highlightedWord).append("</font><br>");
        }

        htmlContent.append("</body></html>");

        resultPane.setContentType("text/html");
        resultPane.setText(htmlContent.toString());
    }
}
