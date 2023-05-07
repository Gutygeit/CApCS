package capcs.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

import capcs.io.DirectoryManager;
import capcs.io.NetworkManager;
import capcs.models.TreeListener;

public class DetailsPanel extends JPanel {

    private JPanel formPanel;

    public DetailsPanel() {
        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
    }

    public void setSelection(TreeListener listener) {
        formPanel.removeAll();

        if (listener instanceof DirectoryManager) {
            DirectoryManager directoryManager = (DirectoryManager) listener;
            formPanel.add(new JLabel("Path:"));
            formPanel.add(new JTextField(directoryManager.getPath()));
        } else if (listener instanceof NetworkManager) {
            NetworkManager networkManager = (NetworkManager) listener;
            if (networkManager.getAddress() != null) {
                formPanel.add(new JLabel("Address:"));
                formPanel.add(new JTextField(networkManager.getAddress()));
                formPanel.add(new JLabel("Port:"));
                formPanel.add(new JTextField(networkManager.getPort() + ""));
            } else {
                formPanel.add(new JLabel("Port:"));
                formPanel.add(new JTextField(networkManager.getPort() + ""));
            }
        }

        formPanel.revalidate();
        formPanel.repaint();

        revalidate();
        repaint();
    }
    
}
