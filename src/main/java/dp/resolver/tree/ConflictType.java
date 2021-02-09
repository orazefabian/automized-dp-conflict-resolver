package dp.resolver.tree;

public enum ConflictType {
    /**
     * Different conflict(issue) types, that can occur in the finished {@link CallTree}
     * <p>
     * TYPE_1: a method is called at least twice with different signatures from two distinct versions af a dependency
     */
    TYPE_1,

    /**
     * TYPE_2: an object is directly in use by two different versions of the same dependency
     */
    TYPE_2,

    /**
     * TYPE_3: all leaf nodes are considered critical and must be solved with clingo
     */
    TYPE_3
}