package dp.resolver.tree.model.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 23.03.21
 *********************************/

public class MethodConnectionSet {

    private List<MethodConnection> connections;
    private boolean changed;

    public MethodConnectionSet() {
        this.connections = new ArrayList<>();
        this.changed = false;
    }

    public void addConnection(MethodConnection con) {
        if (!this.connections.contains(con)) {
            this.connections.add(con);
            this.changed = true;
        }
    }

    public boolean isConnectionAlreadyPresent(MethodConnection con) {
        return this.connections.contains(con);
    }

    public void clearSet() {
        this.connections.clear();
    }

    public boolean isClassTransitiveReferenced(String fromClass) {
        for (MethodConnection connection : this.connections) {
            if (connection.getToClass().equals(fromClass)) return true;
        }
        return false;
    }

    public boolean hasChangedSinceLastCheck() {
        boolean hasChanged = false;
        if (this.changed) hasChanged = true;
        this.changed = false;
        return hasChanged;
    }
}
