package dp.resolver.asp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public enum Rules {

    /**
     * compute all includeJar facts based on all jar facts, don't include 0 because this already indicates the root
     */
    INCLUDE_JAR {
        @Override
        public String txt() {
            return "{includeJar(X)} :- jar(X,_,_,_), X != 0.\n";
        }
    },
    /**
     * a method that must be included is one that is called by the root project (ID: 0), from a invocation
     */
    MUST_INCLUDE_METHOD {
        @Override
        public String txt() {
            return "mustIncludeMethod(C,N,S) :- invocation(0,C,N,S).\n";
        }
    },
    /**
     * facts for each method that comes with the included jar
     */
    INCLUDE_METHOD {
        @Override
        public String txt() {
            return "includeMethod(C,N,S):-includeJar(X),method(X,C,N,S),jar(X,_,_,_).\n";
        }
    },

    /**
     * compute missing methods that are not covered by the included jars but must be included
     */
    MISSING_METHOD {
        @Override
        public String txt() {
            return "missingMethod(C,N,S):-mustIncludeMethod(C,N,S),not includeMethod(C,N,S).\n";
        }
    },

    TRANSITIVE_JAR {
        @Override
        public String txt() {
            return "transitiveIncludedJar(X2) :- includeJar(X1), connection(X1,X2), jar(X2,_,_,_).\n";
        }
    },

    TRANSITIVE_2_JAR {
        @Override
        public String txt() {
            return "transitiveIncludedJar(X2) :- transitiveIncludedJar(X1), connection(X1,X2), jar(X2,_,_,_).\n";
        }
    },

    /**
     * generate clash facts based on the usage of methods with same names and different signatures
     */
    CLASH_SIGNATURE {
        @Override
        public String txt() {
            return "clash(X,X2,C,N):-includeJar(X),includeJar(X2),jar(X,Y,Z,V),jar(X2,Y,Z,V2),method(X,C,N,S),method(X2,C,N,S2),X!=X2,S!=S2.\n";
        }
    },

    /**
     * generate clash facts if two jars are included whose connections(dependencies)each contain methods with potential clash
     */
    CLASH_CONNECTION {
        @Override
        public String txt() {
            return "clash(X,X2,C,N) :- includeJar(X), transitiveIncludedJar(X2), jar(X,Y,Z,V), jar(X2,Y,Z,V2), method(X,C,N,S), method(X2,C,N,S2), X != X2, S != S2.\n";
        }
    },

    CLASH_TRANSITIVE {
        @Override
        public String txt() {
            return "clash(X,X2,C,N) :- transitiveIncludedJar(X), transitiveIncludedJar(X2), jar(X,Y,Z,V), jar(X2,Y,Z,V2), method(X,C,N,S), method(X2,C,N,S2), X != X2, S != S2.\n";
        }
    },

    /**
     * if a minimum amount of included jars are satisfying the problem,do not add other jars
     */
    WEAK_CONSTRAINT_INCLUDE_JAR {
        @Override
        public String txt() {
            return ":~includeJar(X).[2,X]\n";
        }
    },

    /**
     * another weak constraint to filter
     */
    /*WEAK_CONSTRAINT_METHODS {
        @Override
        public String txt() {
            return ":~includeMethod(C,N,S).[1,C,N,S]\n";
        }
    },*/

    /**
     * the amount of clashes must be 0
     */
    COUNT_CLASH_CONSTRAINT {
        @Override
        public String txt() {
            return ":- #count{X,X2,C,N:clash(X,X2,C,N)}!=0.\n";
        }
    },

    /**
     * the amount of missing methods must be 0
     */
    COUNT_MISSING_METHOD_CONSTRAINT {
        @Override
        public String txt() {
            return ":- #count{C,N,S:missingMethod(C,N,S)}!=0.\n";
        }
    },

    /**
     * show directive for result
     */
    SHOW_INCLUDE_JARS {
        @Override
        public String txt() {
            return "#show includeJar/1.\n";
        }
    };

    public abstract String txt();

    public static Stream<Rules> stream() {
        return Stream.of(Rules.values());
    }

    public static File createAndGetRulesFile() {
        File rulesFile = new File("rules.lp");
        StringBuilder builder = new StringBuilder();
        stream().forEach(rules -> builder.append(rules.txt()));
        try {
            FileWriter writer = new FileWriter(rulesFile);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rulesFile;
    }
}



/*
%GENERATION OF ADDITIONAL FACTS

        %#show mustIncludeMethod/3.
        #show missingMethod/3.
        #show clash/4.

        %COMMAND
        %clingo rules.lp facts.lp--opt-mode=optN--quiet=1,2*/
