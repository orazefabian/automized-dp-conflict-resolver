package dp.conflict.resolver.parse;

import dp.conflict.resolver.tree.CallNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 23.12.20
 *********************************/

public class FactParser {

    private List<CallNode> conflictNodes;
    private StringBuilder factsBuilder;
    private FileWriter writer;
    private final String ROOT_DIR = System.getProperty("user.dir");
    private final File factsFile;

    public FactParser(List<CallNode> conflictNodes) throws IOException {
        this.conflictNodes = conflictNodes;
        this.factsBuilder = new StringBuilder();
        this.factsFile = new File(ROOT_DIR + File.separator + "target" + File.separator + "facts.lp");
        this.writer = new FileWriter(this.factsFile);
        generateFacts();
    }

    private void generateFacts() {
        //TODO: parse conflicts to facts
        for (CallNode node : this.conflictNodes) {

        }
    }


}
