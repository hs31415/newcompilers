import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Compiler System");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel codePathLabel = new JLabel("Code Path:");
        JTextField codePathTextField = new JTextField(20);
        JButton browseButton = new JButton("Browse");
        JButton compileButton = new JButton("Compile");


        JLabel outLabel = new JLabel("Output:");
        JTextArea outTextArea = new JTextArea(20, 22);

        JLabel symLabel = new JLabel("Symbols:");
        JTextArea symTextArea = new JTextArea(20, 22);

        JLabel errLabel = new JLabel("Errors:");
        JTextArea errTextArea = new JTextArea(20, 22);

        JLabel inLabel = new JLabel("Input:");
        JTextArea inTextArea = new JTextArea(20, 50);


        JScrollPane outScrollPane = new JScrollPane(outTextArea);
        JScrollPane symScrollPane = new JScrollPane(symTextArea);
        JScrollPane errScrollPane = new JScrollPane(errTextArea);
        JScrollPane inScrollPane = new JScrollPane(inTextArea);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                // 设置对话框只能选择文件
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    codePathTextField.setText(path);

                    File parentDirectory = selectedFile.getParentFile();
                    String fileName = selectedFile.getName();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

                    String inFilePath = parentDirectory.getAbsolutePath() + File.separator + fileName;
                    String outFilePath = parentDirectory.getAbsolutePath() + File.separator + fileName.replace(fileExtension, "out");
                    String symFilePath = parentDirectory.getAbsolutePath() + File.separator + fileName.replace(fileExtension, "sym");
                    String errFilePath = parentDirectory.getAbsolutePath() + File.separator + fileName.replace(fileExtension, "err");

                    displayFileContent(outFilePath, outTextArea);
                    displayFileContent(symFilePath, symTextArea);
                    displayFileContent(errFilePath, errTextArea);
                    displayFileContent(inFilePath, inTextArea);
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

        JPanel topPanel = new JPanel();
        topPanel.add(codePathLabel);
        topPanel.add(codePathTextField);
        topPanel.add(browseButton);
        topPanel.add(compileButton);

        JPanel centerPanel = new JPanel();
        centerPanel.add(outLabel );
        centerPanel.add(outScrollPane);
        centerPanel.add(symLabel );
        centerPanel.add(symScrollPane);
        centerPanel.add(errLabel );
        centerPanel.add(errScrollPane);
        centerPanel.add(inLabel );
        centerPanel.add(inScrollPane);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public static void displayFileContent(String filePath, JTextArea textArea) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            textArea.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
