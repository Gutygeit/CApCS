package capcs.ui;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import capcs.io.DirectoryManager;
import capcs.io.NetworkManager;
import capcs.models.TreeListener;

public class ListenersCellRenderer implements ListCellRenderer<TreeListener> {

    @Override
    public Component getListCellRendererComponent(
        JList<? extends TreeListener> list,
        TreeListener value,
        int index,
        boolean isSelected,
        boolean cellHasFocus
    ) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        if (isSelected) {
            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
        } else {
            panel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());
        }

        if (value instanceof DirectoryManager) {
            DirectoryManager directoryManager = (DirectoryManager) value;
            panel.add(new JLabel("Dossier"));
            panel.add(new JLabel(directoryManager.getPath()));
        } else if (value instanceof NetworkManager) {
            NetworkManager networkManager = (NetworkManager) value;
            if (networkManager.getAddress() != null) {
                panel.add(new JLabel("Client"));
                panel.add(new JLabel(networkManager.getAddress()));
                panel.add(new JLabel("Port : " + networkManager.getPort()));
            } else {
                panel.add(new JLabel("Serveur"));
                panel.add(new JLabel("Port : " + networkManager.getPort()));
            }
        }
        
        return panel;
    }
    
}
