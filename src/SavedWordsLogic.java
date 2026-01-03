import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SavedWordsLogic {

    private final ArrayList<String> promptList = new ArrayList<>();
    public final ArrayList<String> wordList = new ArrayList<>();

    private final GUI menu;

    public SavedWordsLogic(GUI menu) {
        this.menu = menu;
        menu.searchSavedWordsBtn.addActionListener(_ -> search((String) menu.promptBox.getSelectedItem()));
        menu.promptBox.addActionListener(_ -> search((String) menu.promptBox.getSelectedItem()));
    }

    public void loadList() {
        try {
            menu.promptBox.removeAllItems();
            Scanner sp = new Scanner(new FileReader(menu.promptFile));
            Scanner sw = new Scanner(new FileReader(menu.wordFile));
            sp.useDelimiter(";");
            sw.useDelimiter(";");
            while (sp.hasNext() && sw.hasNext()) {
                promptList.add(sp.next());
                wordList.add(sw.next());
            }
            sp.close();
            sw.close();

            // Create a temporary list to hold only the unique items
            ArrayList<String> uniquePrompts = new ArrayList<>();

            for (String prompt : promptList) {
                // If the temporary list does NOT already contain this prompt, add it
                if (!uniquePrompts.contains(prompt)) {
                    uniquePrompts.add(prompt);
                    // Only add to the ComboBox if it's unique
                    menu.promptBox.addItem(prompt);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(menu, "An error has occured: " + e);
        }
    }

    private void search(String prompt) {
        menu.savesPane.setText("");

        // Create a temporary list for results. Do not use the class 'wordList'.
        ArrayList<String> foundWords = new ArrayList<>();

        // DEBUG SEARCH, FROM TEXT FILES
        try {
            Scanner sp = new Scanner(menu.promptFile);
            Scanner sw = new Scanner(menu.wordFile);
            sp.useDelimiter(";");
            sw.useDelimiter(";");

            while (sp.hasNext() && sw.hasNext()) {
                String tempPrompt = sp.next();
                if (sw.hasNext()) {
                    String tempWord = sw.next();
                    if (tempPrompt.equals(prompt)) {
                        foundWords.add(tempWord);
                    }
                }
            }
            sp.close();
            sw.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(menu, "An error has occured: " + e);
        }

        // AI formatovani, jupiiiii
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body style='font-family: monospace; color: white;'>");

        for (String word : foundWords) {
            String highlightedWord = word.replace(prompt, "</font><font color='#ff9999'>" + prompt + "</font><font color='white'>");

            htmlContent.append("<font color='white'>").append(highlightedWord).append("</font><br>");
        }

        htmlContent.append("</body></html>");

        menu.savesPane.setContentType("text/html");
        menu.savesPane.setText(htmlContent.toString());
    }
}
