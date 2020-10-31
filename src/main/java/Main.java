import com.ctc.wstx.exc.WstxOutputException;
import org.paukov.combinatorics3.Generator;

import javax.crypto.spec.PSource;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class Main {

    public static void main(String[] args) throws Exception {

        String sample = "/Users/fabian/Projects/Sample/sample_project/";
        String target = "/Users/fabian/Projects/Sample/target_project/";


        DPUpdaterBase impl = new ImplNaive(target, 5);
        System.out.println(impl.getPomModel());
        impl.getBuildSuccess(false);

        impl.computeVersionConfiguration();


    }


}
