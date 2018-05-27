package tables_extractor.arguments;

import java.io.File;
import java.util.Objects;

import net.sourceforge.argparse4j.annotation.Arg;

public class Arguments {

    public static final String INPUT_DIR = "inputDirectory";
    @Arg(dest = INPUT_DIR)
    public File inputDirectory;

    public static final String INPUT_FILE = "inputFile";
    @Arg(dest = INPUT_FILE)
    public File inputFile;

    public static final String OUTPUT_DIR = "outputDirectory";
    @Arg(dest = OUTPUT_DIR)
    public File outputDirectory;

    public static final String OUTPUT_LAYOUT = "outputLayout";
    @Arg(dest = OUTPUT_LAYOUT)
    public OutputLayout outputLayout;

    public static final String RUN_HEURISTIC = "runTableHeuristic";
    @Arg(dest = RUN_HEURISTIC)
    public boolean runTableHeuristic;

    @Override
    public int hashCode() {
        return Objects.hash(inputDirectory, inputFile, outputDirectory, outputLayout, runTableHeuristic);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Arguments){
            Arguments obj_ = (Arguments) obj;
            if (inputDirectory.equals(obj_.inputDirectory) &&
                inputFile.equals(obj_.inputFile) &&
                outputDirectory.equals(obj_.outputDirectory) &&
                outputLayout.equals(obj_.outputLayout) &&
                runTableHeuristic == obj_.runTableHeuristic){
                return true;
            }
        }
        return false;
    }
}