
public class Main {

    public static void main(String[] args) {


        String sample = "/Users/fabian/Projects/Sample/commons-collections/";
        String target = "/Users/fabian/Projects/Sample/target_project/";


        DPUpdaterBase impl = new ImplNaive(sample);

        impl.updateDependencies();

        System.out.println(impl.getWorkingConfigurations());

    }



}
