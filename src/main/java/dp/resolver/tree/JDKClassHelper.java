package dp.resolver.tree;

import java.io.File;

public class JDKClassHelper {
    /**
     * helper function which checks if a class is part of the JDK
     *
     * @param qualifiedName String name of class, must be separated by dots
     * @return true if class is part of JDK
     */
    public static boolean isPartOfJDKClassesFromQualifiedName(String qualifiedName) {
        return isPartOfJDK(qualifiedName);
    }

    /**
     * helper function which checks if a jar is part of the JDK
     *
     * @param pathToJar the full path to the jar
     * @return true if jar is part of JDK
     */
    public static boolean isPartOfJDKFromFullPath(String pathToJar) {
        String qualifiedNameWithoutPrefix = pathToJar.split("repository" + File.separator)[1];
        qualifiedNameWithoutPrefix = qualifiedNameWithoutPrefix.replace(File.separator, ".");
        return isPartOfJDK(qualifiedNameWithoutPrefix);
    }

    private static boolean isPartOfJDK(String qualifiedName) {
        return qualifiedName.startsWith("java.") || (qualifiedName.startsWith("javax.xml.parsers.")
                || (qualifiedName.startsWith("com.sun.")) || (qualifiedName.startsWith("sun."))
                || (qualifiedName.startsWith("oracle.")) || (qualifiedName.startsWith("org.xml"))
                || (qualifiedName.startsWith("com.oracle.")) || (qualifiedName.startsWith("jdk."))
                || (qualifiedName.startsWith("javax.xml.stream.")) || (qualifiedName.startsWith("javax.xml.transform."))
                || (qualifiedName.startsWith("org.w3c.dom.")));
    }

}