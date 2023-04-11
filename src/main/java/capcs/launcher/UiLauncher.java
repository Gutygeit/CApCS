package capcs.launcher;

import capcs.models.TreeListener;

import javax.swing.*;
import java.awt.*;

public class UiLauncher extends JFrame {
    public UiLauncher() {

        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));

        JList<TreeListener> list = new JList<>();
        JScrollPane scrollPane = new JScrollPane(list);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, panel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(100);


        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Clone Alpha pas Clone Soja");
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(splitPane);


        // A button to add a listener with the addListener method from the Launcher class
        /*JButton addListenerButton = new JButton("Add Listener");
        addListenerButton.addActionListener(e -> {
            String path = JOptionPane.showInputDialog("Enter the path of the directory to listen to");
            addListener(new DirectoryManager(path));
        });
        panel.add(addListenerButton);
        addListenerButton.setSize(10, 5);
*/

    }

}

