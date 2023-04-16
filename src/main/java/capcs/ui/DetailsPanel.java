package capcs.ui;

import javax.swing.JPanel;

import capcs.models.TreeListener;

public class DetailsPanel extends JPanel {

    public DetailsPanel() {
        
    }

    public void setSelection(TreeListener listener) {
        System.out.println(listener);
    }
    
}
