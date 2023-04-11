package capcs.launcher;

import capcs.io.DirectoryManager;
import capcs.models.TreeListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListDataListener;

public class UILauncher extends Launcher implements ListModel<TreeListener> {

    // MARK: - Properties

    private List<ListDataListener> listListeners = new ArrayList<>();
    private JList<TreeListener> list = new JList<>(this);
    private JPanel details = new JPanel();

    // MARK: - Constructor

    public UILauncher() {
        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane(list);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, details);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(100);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Clone Alpha pas Clone Soja");
        frame.setContentPane(splitPane);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // A button to add a listener with the addListener method from the Launcher class
        /*JButton addListenerButton = new JButton("Add Listener");
        addListenerButton.addActionListener(e -> {
            String path = JOptionPane.showInputDialog("Enter the path of the directory to listen to");
            addListener(new DirectoryManager(path));
        });
        panel.add(addListenerButton);
        addListenerButton.setSize(10, 5);
        */

        addListener(new DirectoryManager("data/src"));
    }

    // MARK: - Launcher

    @Override
    public void addListener(TreeListener listener) {
        super.addListener(listener);
        listListeners.forEach(l -> l.contentsChanged(null));
    }

    // MARK: - ListModel

    @Override
    public int getSize() {
        return listeners.size();
    }

    @Override
    public TreeListener getElementAt(int index) {
        return listeners.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listListeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listListeners.remove(l);
    }

}

