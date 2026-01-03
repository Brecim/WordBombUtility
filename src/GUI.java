import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

public class GUI extends JFrame {
    private JPanel mainPn;
    public JTextField promptField;
    private JButton findBtn;
    private JTextPane resultPane;
    private JComboBox wordLengthBox;
    private JButton bookmarkBtn;
    public JComboBox promptBox;
    public JButton searchSavedWordsBtn;
    public JTextPane savesPane;
    public JButton clearBookmarkBtn;
    public JTextField bookmarkPromptField;
    public JTextField bookmarkWordField;
    private JScrollPane scrollPaneR;
    private JScrollPane scrollPaneS;

    public ArrayList<String> dict = new ArrayList<>();
    private int length = 1000;

    // Define a variable to store the working directory path
    public String appDataDirectory;
    // Reads the name of the operating system
    public static final String osName = (System.getProperty("os.name")).toUpperCase();

    public File promptFile;
    public File wordFile;

    public final BookmarkLogic bl = new BookmarkLogic(this);
    public final SavedWordsLogic swl = new SavedWordsLogic(this);

    public GUI() {
        setContentPane(mainPn);
        setTitle("Word Bomb Utility");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack();

        setLocationRelativeTo(null);

        loadDict();
        init();
    }

    private void init() {
        checkOS();

        // Ensure the directory actually exists on the hard drive
        File directory = new File(appDataDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // This creates the folder "Word Bomb Utility"
        }

        // NOW initialize the File objects using the valid path
        promptFile = new File(appDataDirectory + "savedPrompts.txt");
        wordFile = new File(appDataDirectory + "savedWords.txt");

        try {
            if (!promptFile.exists()) {
                promptFile.createNewFile();
            }
            if (!wordFile.exists()) {
                wordFile.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "An error has occured: " + e);
            System.exit(69);
        }

        swl.loadList();

        wordLengthBox.addItem(10);
        wordLengthBox.addItem(15);
        wordLengthBox.addItem(20);
        wordLengthBox.addItem("Max");

        findBtn.addActionListener(_ -> find(promptField.getText()));
        // Jiz snima klavesy vcetne Enteru
        promptField.addActionListener(_ -> find(promptField.getText()));
        wordLengthBox.addActionListener(_ -> find(promptField.getText()));
        // Automaticky bere prednost pri spusteni aplikace
        promptField.requestFocus();
        bookmarkBtn.addActionListener(_ -> bl.saveWord());
    }

    public void checkOS() {
        // Determine the working directory based on the OS
        if (osName.contains("WIN")) {
            appDataDirectory = System.getenv("AppData");
            appDataDirectory += "\\" + "Word Bomb Utility" + "\\";
            System.out.println(appDataDirectory);
        } else {
            appDataDirectory = System.getProperty("user.home");
            if (osName.contains("MAC")) {
                appDataDirectory += "/Library/Application Support/Word Bomb Utility/";
            } else {
                appDataDirectory += "/.config/Word Bomb Utility/";
            }
        }
    }

    private void checkLength() {
        String selectedLength = Objects.requireNonNull(wordLengthBox.getSelectedItem()).toString();
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
        // DEBUG LOAD DICT, FROM TEXT FILE
        //try {
            //Scanner s = new Scanner(new FileReader("dict.txt"));
            //while (s.hasNextLine()) {
                //dict.add(s.nextLine());
            //}
        //} catch (FileNotFoundException e) {
        //    throw new RuntimeException(e);
        //}

        InputStream inputStream = getClass().getResourceAsStream("dict.txt");
        assert inputStream != null;
        Scanner s = new Scanner(inputStream);
        while (s.hasNextLine()) {
            dict.add(s.nextLine());
        }
        s.close();
        dict.sort(Comparator.comparingInt(String::length).reversed().thenComparing(Comparator.naturalOrder()));
    }

        private void find(String prompt) {
        int found = 0;
        ArrayList<String> list = new ArrayList<>();
        checkLength();
        resultPane.setText("");
        // auto nastaveni promptu
        bookmarkPromptField.setText(prompt);

        for (int i = 0; i <= dict.size() - 1; i++) {
            if (dict.get(i).contains(prompt) && dict.get(i).length() <= length) {
                list.add(dict.get(i));
                found++;
            }
            if (found == 300) {
                break;
            }
        }
        // AI formatovani, jupiiiii
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body style='font-family: monospace; color: white;'>");

        for (String word : list) {
            String highlightedWord = word.replace(prompt, "</font><font color='#ff9999'>" + prompt + "</font>");

            htmlContent.append("<font color='white'>").append(highlightedWord).append("</font><br>");
        }

        htmlContent.append("</body></html>");

        resultPane.setContentType("text/html");
        resultPane.setText(htmlContent.toString());
    }

    void main() {
        setVisible(true);
    }
}
