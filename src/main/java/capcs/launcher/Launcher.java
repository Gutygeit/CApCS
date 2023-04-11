package capcs.launcher;

import java.util.ArrayList;
import java.util.List;

import capcs.models.TreeListener;

public class Launcher {

    // MARK: - Properties

    /**
     * Listeners list
     */
    protected List<TreeListener> listeners = new ArrayList<TreeListener>();

    // MARK: - Methods

    /**
     * Add a listener to the list
     * @param listener
     */
    public void addListener(TreeListener listener) {
        listeners.forEach(l -> {
            l.registerListener(listener);
            listener.registerListener(l);
        });
        listeners.add(listener);
    }
    
}
