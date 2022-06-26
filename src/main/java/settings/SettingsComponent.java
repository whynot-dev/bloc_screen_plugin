package settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class SettingsComponent {
    public JPanel mainPanel;
    public JBTextField blocName;
    public JBTextField eventName;
    public JBTextField viewName;
    public JBTextField viewFileName;

    public SettingsComponent() {
        blocName = new JBTextField();
        eventName = new JBTextField();
        viewName = new JBTextField();
        viewFileName = new JBTextField();

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Bloc name: "), blocName)
                .addLabeledComponent(new JBLabel("Event name: "), eventName)
                .addLabeledComponent(new JBLabel("View name: "), viewName)
                .addLabeledComponent(new JBLabel("View file name: "), viewFileName)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }
}
