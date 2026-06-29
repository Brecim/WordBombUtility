import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class DeleteConfirmPrompt extends JFrame {

    private JPanel mainPn;
    private JButton cancelBtn;
    private JButton confirmBtn;
    private JLabel promptLabel;

    private final GUI menu;

    public DeleteConfirmPrompt(GUI menu) {
        this.menu = menu;
        setContentPane(mainPn);
        setTitle("Warning");
        setPreferredSize(new Dimension(500, 150));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();

        cancelBtn.addActionListener(_ -> closePrompt());
        confirmBtn.addActionListener(_ -> deleteBookmarks());
    }

    public void closePrompt() {
        menu.bl.DCPopens = 0;
        dispose();
    }

    private void deleteBookmarks() {
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
