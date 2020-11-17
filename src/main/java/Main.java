
public class Main {

    public static void main(String[] args) {


     /*
        String target = "/Users/fabian/Projects/Sample/commons-collections/";
        String sample = "/Users/fabian/Projects/Sample/sample_project/";


        DPUpdaterBase impl = new ImplNaive(sample, 2);


        impl.updateDependencies();
        System.out.println(impl.getWorkingConfigurations());
    */


        String target = "/Users/fabian/Projects/Sample/conflict_sample/";

        ConflictSeeker cf = new ConflictSeeker(target);

        cf.getDPJson(null);
        cf.createGraphPNG();

    }


}
