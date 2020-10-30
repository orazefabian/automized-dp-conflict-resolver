import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import org.w3c.dom.Document;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************
 *
 * Implementation of the DPUpdater interface
 * Naive approach which just gets the latest versions of a provided List with the groupIDs of a number of dependencies
 *
 */
public class ImplNaive extends DPUpdaterBase {


    protected List<List<String>> fullDPsWithVersions;

    public ImplNaive(String pathToRepo) throws IOException {
        super(pathToRepo);
        this.fullDPsWithVersions = new ArrayList<>();
    }

    @Override
    public DPConfiguration computeVersionConfiguration() {
        DPConfiguration configuration;
        // TODO: calculate different versions of dps
        String xml = null;
        File file = new File(path + "pom.xml");
        FileWriter fileWriter;
        getPomModel().getDependencies().getDependency().get(0).setVersion("4.13.1");
        getPomModel().getDependencies().getDependency().get(1).setVersion("0.8.6");
        getPomModel().getDependencies().getDependency().get(2).setVersion("3.6.0");

       /* mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            xml = mapper.writeValueAsString(pomModel);
            fileWriter = new FileWriter(file);
            fileWriter.write(xml);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public void getDependencies() throws Exception {
        Model.Dependencies dps = this.pomModel.getDependencies();
        List<Dependency> dp = dps.getDependency();
        Document doc;
        String url;
        this.fullDPsWithVersions = null;
        for (Dependency d : dp) {
            url = processDependencies(d.getGroupId(), d.getArtifactId());
            doc = loadDocument(url);
            this.versions = getVersions(doc);
            this.fullDPsWithVersions.add(versions);
        }
    }
}
