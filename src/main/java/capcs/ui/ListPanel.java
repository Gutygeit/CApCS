package capcs.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;

import capcs.io.DirectoryManager;
import capcs.io.NetworkManager;
import capcs.launcher.UILauncher;
import capcs.models.TreeListener;

public class ListPanel extends JPanel {
    
    private List<ListDataListener> listListeners = new ArrayList<>();
    private JList<TreeListener> list;

    public ListPanel(UILauncher launcher, DetailsPanel detailsPanel) {
        list = new JList<>(new AbstractListModel<TreeListener>() {
            @Override
            public int getSize() {
                return launcher.getSize();
            }

            @Override
            public TreeListener getElementAt(int index) {
                return launcher.getElementAt(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {
                listListeners.add(l);
            }

            @Override
            public void removeListDataListener(ListDataListener l) {
                listListeners.remove(l);
            }
        });
        list.setCellRenderer(new ListenersCellRenderer());
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                detailsPanel.setSelection(list.getSelectedValue());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(list);

        JButton addFolderButton = new JButton("New folder");
        addFolderButton.addActionListener(e -> {
            String path = JOptionPane.showInputDialog("Path");
            if (path == null) return;
            launcher.addListener(new DirectoryManager(path));
            listListeners.forEach(l -> l.contentsChanged(null));
        });

        JButton addClientButton = new JButton("New client");
        addClientButton.addActionListener(e -> {
            String address = JOptionPane.showInputDialog("Server address");
            if (address == null) return;
            int port = -1;
            do {
                try {
                    String portStr = JOptionPane.showInputDialog("Server port");
                    if (portStr == null) return;
                    port = Integer.parseInt(portStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Port must be a number");
                }
            } while (port < 0);
            launcher.addListener(new NetworkManager(address, port));
            listListeners.forEach(l -> l.contentsChanged(null));
        });

        JButton addServerButton = new JButton("New server");
        addServerButton.addActionListener(e -> {
            int port = -1;
            do {
                try {String portStr = JOptionPane.showInputDialog("Server port");
                if (portStr == null) return;
                port = Integer.parseInt(portStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Port must be a number");
                }
            } while (port < 0);
            launcher.addListener(new NetworkManager(null, port));
            listListeners.forEach(l -> l.contentsChanged(null));
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(scrollPane);
        add(addFolderButton);
        add(addClientButton);
        add(addServerButton);
    }
    
}
