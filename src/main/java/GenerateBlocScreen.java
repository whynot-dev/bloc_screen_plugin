import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import helper.BlocConfig;
import helper.BlocTaoData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;


public class GenerateBlocScreen extends AnAction {
    private Project project;
    private String psiPath;
    private BlocTaoData data;

    /**
     * Overall popup entity
     */
    private JDialog jDialog;
    private JTextField nameTextField;
    /**
     * Checkbox
     * Use folder：default true
     * Use prefix：default false
     */
    private JCheckBox isScreenBox;

    private JRadioButton actualVersionBtn, previousVersionBtn;

    private final String dotDart = ".dart";

    @Override
    public void actionPerformed(AnActionEvent event) {
        project = event.getProject();
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT).toString();
        psiPath = psiPath.substring(psiPath.indexOf(":") + 1);
        initData();
        initView();
    }

    private void initData() {
        data = BlocTaoData.getInstance();
        jDialog = new JDialog(new JFrame(), "Bloc Template Code Produce");
    }

    private void initView() {
        //Set function button
        Container container = jDialog.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        //Set the main mode style
        //deal default value
        addSelectorVersion(container);

        //Setting options: whether to use prefix
        //deal default value
        setCodeFile(container);

        //Generate module name and OK cancel button
        setModuleAndConfirm(container);

        //Choose a pop-up style
        setJDialog();
    }

    /**
     * generate  file
     */
    private void save() {
        if (nameTextField.getText() == null || "".equals(nameTextField.getText().trim())) {
            Messages.showInfoMessage(project, "Please input the module name", "Info");
            return;
        }
        dispose();
        //Create a file
        createFile();
        //Refresh project
        project.getProjectFile().refresh(false, true);
    }

    /**
     * Set the overall pop-up style
     */
    private void setJDialog() {
        //The focus is on the current pop-up window,
        // and the focus will not shift even if you click on other areas
        jDialog.setModal(true);
        //Set padding
        ((JPanel) jDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jDialog.setSize(430, 350);
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    /**
     * Main module
     */
    private void addSelectorVersion(Container container) {
        //Two rows and two columns
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 2));
        //Set the main module style：mode, function
        jPanel.setBorder(BorderFactory.createTitledBorder("Select version"));
        //default: high setting
        actualVersionBtn = new JRadioButton(BlocConfig.actualVersion, true);
        previousVersionBtn = new JRadioButton(BlocConfig.previousVersion, false);
        setPadding(actualVersionBtn);

        actualVersionBtn.setActionCommand(BlocConfig.actualVersion);
        previousVersionBtn.setActionCommand(BlocConfig.previousVersion);

        ButtonGroup group = new ButtonGroup();
        group.add(actualVersionBtn);
        group.add(previousVersionBtn);

        jPanel.add(actualVersionBtn);
        jPanel.add(previousVersionBtn);

        container.add(jPanel);
        setDivision(container);
    }

    /**
     * Generate file
     */
    private void setCodeFile(Container container) {
        //Select build file
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Select Function"));

        //use prefix
        isScreenBox = new JCheckBox("is Screen", data.isScreen);
        setMargin(isScreenBox);
        panel.add(isScreenBox);

        container.add(panel);
        setDivision(container);
    }


    /**
     * Generate file name and button
     */
    private void setModuleAndConfirm(Container container) {
        JPanel nameField = new JPanel();
        nameField.setLayout(new FlowLayout());
        nameField.setBorder(BorderFactory.createTitledBorder("Directory Name"));
        nameTextField = new JTextField(28);
        nameTextField.addKeyListener(keyListener);
        nameField.add(nameTextField);
        container.add(nameField);

        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout());

        //Set bottom spacing
        setDivision(container);

        //OK cancel button
        JButton cancel = new JButton("Cancel");
        cancel.setForeground(JBColor.RED);
        cancel.addActionListener(actionListener);

        JButton ok = new JButton("OK");
        ok.setForeground(JBColor.GREEN);
        ok.addActionListener(actionListener);
        menu.add(cancel);
        menu.add(ok);
        container.add(menu);
    }

    private void createFile() {

        data.isScreen = isScreenBox.isSelected();

        String name = upperCase(nameTextField.getText());
        String prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        String folder = "/" + prefix;
        String prefixName = prefix + "_";

        if (actualVersionBtn.isSelected()) {
            generateActualVersionBloc(folder, prefixName);
        } else if (previousVersionBtn.isSelected()) {
            generatePreviousVersionBloc(folder, prefixName);
        }

    }

    private void generateActualVersionBloc(String folder, String prefixName) {
        String path = psiPath + folder;

        if (isScreenBox.isSelected()) {
            generateFile("actual_version/screen.dart", path, prefixName + data.viewFileName.toLowerCase() + dotDart);
        } else {
            generateFile("actual_version/widget.dart", path, prefixName.replaceFirst("_", "") + dotDart);
        }

        generateFile("actual_version/bloc.dart", path + "/bloc/", prefixName + data.blocName.toLowerCase() + dotDart);
        generateFile("actual_version/event.dart", path + "/bloc/", prefixName + data.eventName.toLowerCase() + dotDart);
        generateFile("actual_version/state.dart", path + "/bloc/", prefixName + "state" + ".dart");
    }

    private void generatePreviousVersionBloc(String folder, String prefixName) {
        String path = psiPath + folder;

        if (isScreenBox.isSelected()) {
            generateFile("previous_version/screen.dart", path, prefixName + data.viewFileName.toLowerCase() + dotDart);
        } else {
            generateFile("previous_version/widget.dart", path, prefixName.replaceFirst("_", "") + dotDart);
        }

        generateFile("previous_version/bloc.dart", path + "/bloc/", prefixName + data.blocName.toLowerCase() + dotDart);
        generateFile("previous_version/event.dart", path + "/bloc/", prefixName + data.eventName.toLowerCase() + dotDart);
        generateFile("previous_version/state.dart", path + "/bloc/", prefixName + "state" + dotDart);
    }


    private void generateFile(String inputFileName, String filePath, String outFileName) {
        //content deal
        String content = dealContent(inputFileName, outFileName);

        //Write file
        try {
            File folder = new File(filePath);
            // if file doesnt exists, then create it
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(filePath + "/" + outFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //content need deal
    private String dealContent(String inputFileName, String outFileName) {
        //name baseFolder
        String baseFolder = "/templates/";
        final String packageName = project.getName();

        //read file
        String content = "";
        try {
            InputStream in = this.getClass().getResourceAsStream(baseFolder + inputFileName);
            assert in != null;
            content = new String(readStream(in));
        } catch (Exception e) {
        }

        //replace Bloc
        if (outFileName.contains(data.blocName.toLowerCase())) {
            content = content.replaceAll("\\$nameBloc", "\\$name" + data.blocName);
            content = content.replaceAll("\\$nameEvent", "\\$name" + data.eventName);
        }

        String[] tempArray = nameTextField.getText().split("_");

        for (int i = 0; i < tempArray.length; i++) {
            tempArray[i] = upperCase(tempArray[i]);
        }

        String stringForName = String.join("", tempArray);


        content = content.replaceAll("\\$name", upperCase(stringForName));
        content = content.replaceAll("\\$file_name", nameTextField.getText());
        content = content.replaceAll("\\$main_package", packageName);

        return content;
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
                System.out.println(new String(buffer));
            }

        } catch (IOException e) {
        } finally {
            outSteam.close();
            inStream.close();
        }
        return outSteam.toByteArray();
    }


    private final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) save();
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) dispose();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };

    private final ActionListener actionListener = e -> {
        if (e.getActionCommand().equals("Cancel")) {
            dispose();
        } else {
            save();
        }
    };

    private void setPadding(JRadioButton btn) {
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 0));
    }

    private void setMargin(JCheckBox btn) {
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 0));
    }

    private void setDivision(Container container) {
        //Separate the spacing between modules
        JPanel margin = new JPanel();
        container.add(margin);
    }

    private void dispose() {
        jDialog.dispose();
    }

    private String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}