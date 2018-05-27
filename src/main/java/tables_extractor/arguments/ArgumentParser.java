package tables_extractor.arguments;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;

public class ArgumentParser {

    public static tables_extractor.arguments.Arguments parse(String[] args){

        net.sourceforge.argparse4j.inf.ArgumentParser parser = ArgumentParsers.newFor("PDF Tables Extractor").build();

        MutuallyExclusiveGroup inputGroup = parser.addMutuallyExclusiveGroup();
        inputGroup.required(true);

        Argument inputDirArg = inputGroup.addArgument("--input-dir", "-i");
        inputDirArg.type(Arguments.fileType().verifyIsDirectory().verifyCanRead());
        inputDirArg.dest(tables_extractor.arguments.Arguments.INPUT_DIR);

        Argument inputFileArg = inputGroup.addArgument("--input-file", "-f");
        inputFileArg.type(Arguments.fileType().verifyIsFile().verifyCanRead());
        inputFileArg.dest(tables_extractor.arguments.Arguments.INPUT_FILE);

        Argument outputDirArg = parser.addArgument("--output-dir", "-o");
        outputDirArg.required(true);
        outputDirArg.type(Arguments.fileType().verifyNotExists().verifyCanCreate().or().verifyIsDirectory().verifyCanWrite());
        outputDirArg.dest(tables_extractor.arguments.Arguments.OUTPUT_DIR);

        Argument outputLayoutArg = parser.addArgument("--output-mode", "-m");
        outputLayoutArg.type(OutputLayout.class);
        outputLayoutArg.setDefault(OutputLayout.Flat);
        outputLayoutArg.dest(tables_extractor.arguments.Arguments.OUTPUT_LAYOUT);

        Argument runTableHeuristicArg = parser.addArgument("--no-heuristic", "-n");
        runTableHeuristicArg.setDefault(true);
        runTableHeuristicArg.action(Arguments.storeFalse());
        runTableHeuristicArg.dest(tables_extractor.arguments.Arguments.RUN_HEURISTIC);

        try {
            tables_extractor.arguments.Arguments arguments = new tables_extractor.arguments.Arguments();
			parser.parseArgs(args, arguments);
            return arguments;
		} catch (ArgumentParserException e) {
            parser.handleError(e);
            return null;
		}
    }
}