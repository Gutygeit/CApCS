package capcs.launcher;

import capcs.models.TreeListener;
import capcs.ui.DetailsPanel;
import capcs.ui.ListPanel;

import javax.swing.*;

public class UILauncher extends Launcher {

    // MARK: - Properties

    private DetailsPanel detailsPanel = new DetailsPanel();
    private ListPanel listPanel = new ListPanel(this, detailsPanel);

    // MARK: - Constructor

    public UILauncher() {
        JFrame frame = new JFrame();

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            listPanel,
            detailsPanel
        );
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Clone Alpha pas Clone Soja");
        frame.setContentPane(splitPane);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

}
