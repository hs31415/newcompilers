import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Compiler System");
        JPanel panel = new JPanel();

        JLabel codePathLabel = new JLabel("Code Path:");
        JTextField codePathTextField = new JTextField(20);
        JButton browseButton = new JButton("Browse");
        JButton compileButton = new JButton("Compile");

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                // 设置对话框只能选择文件
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    codePathTextField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        compileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inPath = codePathTextField.getText();
                int index = inPath.lastIndexOf(".");
                String prefix = inPath.substring(0, index);

                Lexer lexer = new Lexer();
                try {
                    lexer.GetInput(inPath, prefix + ".out", prefix + ".sym", prefix + ".err");

                    codePathTextField.setText("");
                    System.out.println("Compilation process completed!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.add(codePathLabel);
        panel.add(codePathTextField);
        panel.add(browseButton);
        panel.add(compileButton);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }
}
