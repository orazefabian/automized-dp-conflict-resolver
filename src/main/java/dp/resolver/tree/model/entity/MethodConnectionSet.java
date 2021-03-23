package dp.resolver.tree.model.entity;

import java.util.HashSet;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 23.03.21
 *********************************/

public class MethodConnectionSet {

    Set<MethodConnection> connections;

    public MethodConnectionSet() {
        this.connections = new HashSet<>();
    }

    public void addConnection(MethodConnection con) {
        this.connections.add(con);
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

}
