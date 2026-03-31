import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

public class GUI extends JFrame {
    private JPanel mainPn;
    public JTextField promptField;
    private JButton findBtn = new JButton();
    private JTextPane resultPane;
    private JButton bookmarkBtn;
    public JButton searchSavedWordsBtn = new JButton();
    public JTextPane savesPane;
    public JButton clearBookmarkBtn;
    public JTextField bookmarkWordField;
    public JTextField bookmarkSearchField;

    private JScrollPane scrollPaneR;
    public JScrollPane scrollPaneS;
    private JSlider wordLengthSlider;

    public ArrayList<String> dict = new ArrayList<>();
    private int length = 1000;

    // Define a variable to store the working directory path
    public String appDataDirectory;
    // Reads the name of the operating system
    public static final String osName = (System.getProperty("os.name")).toUpperCase();

    public File wordFile;

    public final BookmarkLogic bl = new BookmarkLogic(this);
    public final SavedWordsLogic swl = new SavedWordsLogic(this);

    public GUI() {
        setContentPane(mainPn);
        setTitle("Word Bomb Utility");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        setVisible(true);
        pack();

        setLocationRelativeTo(null);

        loadDict();
        init();
    }

    private void init() {
        checkOS();

        File directory = new File(appDataDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        wordFile = new File(appDataDirectory + "savedWords.txt");

        try {
            if (!wordFile.exists()) {
                wordFile.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "An error has occured: " + e);
            System.exit(69);
        }

        swl.loadList();


        findBtn.addActionListener(_ -> find(promptField.getText()));
        promptField.addActionListener(_ -> find(promptField.getText()));
        wordLengthSlider.addChangeListener(_ -> find(promptField.getText()));
        bookmarkBtn.addActionListener(_ -> bl.saveWord());

        // Add the DocumentListener to trigger search on every keystroke
        promptField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                find(promptField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                find(promptField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                find(promptField.getText());
            }
        });
        promptField.requestFocus();
    }

    public void checkOS() {
        // Determine the working directory based on the OS
        if (osName.contains("WIN")) {
            appDataDirectory = System.getenv("AppData");
            appDataDirectory += "\\" + "Word Bomb Utility" + "\\";
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
        length = wordLengthSlider.getValue();
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
        paneUp();
        resultPane.setText("");


        for (int i = 0; i <= dict.size() - 1; i++) {
            if (dict.get(i).length() <= length && dict.get(i).contains(prompt)) {
                list.add(dict.get(i));
                found++;
            }
            if (found == 100) {
                break;
            }
        }




        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body style='font-family: monospace; color: white;'>");

        for (String word : list) {
            String highlightedWord = word.replace(prompt, "</font><font color='#ff9999'>" + prompt + "</font>");

            htmlContent.append("<font color='white'>").append(highlightedWord).append("</font><br>");
        }

        htmlContent.append("</body></html>");

        resultPane.setContentType("text/html");
        resultPane.setText(htmlContent.toString());
        scrollPaneR.getViewport().setViewPosition(new Point(0,0));
    }

    private void paneUp() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resultPane.setCaretPosition(0);
            }
        });
    }
}
