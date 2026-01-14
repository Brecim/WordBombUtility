import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class SavedWordsLogic {

    public final ArrayList<String> wordList = new ArrayList<>();

    private final GUI menu;

    public SavedWordsLogic(GUI menu) {
        this.menu = menu;

        menu.searchSavedWordsBtn.addActionListener(_ -> search(menu.bookmarkSearchField.getText()));
        menu.bookmarkSearchField.addActionListener(_ -> search(menu.bookmarkSearchField.getText()));

        menu.bookmarkSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(menu.bookmarkSearchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(menu.bookmarkSearchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(menu.bookmarkSearchField.getText());
            }
        });
    }

    public void loadList() {
        try {
            Scanner sw = new Scanner(new FileReader(menu.wordFile));
            sw.useDelimiter(";");
            while (sw.hasNext()) {
                wordList.add(sw.next());
            }
            sw.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(menu, "An error has occured: " + e);
        }
    }

    private void search(String prompt) {
        menu.savesPane.setText("");

        ArrayList<String> foundWords = new ArrayList<>();

        try {
            Scanner sw = new Scanner(menu.wordFile);
            sw.useDelimiter(";");

            while (sw.hasNext()) {
                String tempWord = sw.next();
                if (tempWord.contains(prompt)) {
                    foundWords.add(tempWord);
                }
            }
            sw.close();
            foundWords.sort(Comparator.comparingInt(String::length).reversed().thenComparing(Comparator.naturalOrder()));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(menu, "An error has occured: " + e);
        }

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body style='font-family: monospace; color: white;'>");

        for (String word : foundWords) {
            String highlightedWord = word.replace(prompt, "</font><font color='#ff9999'>" + prompt + "</font><font color='white'>");

            htmlContent.append("<font color='white'>").append(highlightedWord).append("</font><br>");
        }

        htmlContent.append("</body></html>");

        menu.savesPane.setContentType("text/html");
        menu.savesPane.setText(htmlContent.toString());
        menu.scrollPaneS.getViewport().setViewPosition(new Point(0,0));
    }
}
