package tables_extractor.inject;

import java.io.File;

import com.google.inject.AbstractModule;
import com.google.inject.util.Providers;

import tables_extractor.arguments.Arguments;
import tables_extractor.file.DeepPdfFileManager;
import tables_extractor.file.FlatPdfFileManager;
import tables_extractor.file.PdfFileManager;
import tables_extractor.inject.annotations.InputDir;
import tables_extractor.inject.annotations.InputFile;
import tables_extractor.inject.annotations.OutputDir;
import tables_extractor.inject.annotations.RunTableHeuristic;

public class AppModule extends AbstractModule {

    private final Arguments arguments;

    public AppModule(Arguments arguments){
        this.arguments = arguments;
    }

    @Override
    public void configure(){
        bind(File.class).annotatedWith(InputDir.class).toProvider(Providers.of(arguments.inputDirectory));
        bind(File.class).annotatedWith(InputFile.class).toProvider(Providers.of(arguments.inputFile));
        bind(File.class).annotatedWith(OutputDir.class).toInstance(arguments.outputDirectory);
        bind(boolean.class).annotatedWith(RunTableHeuristic.class).toInstance(arguments.runTableHeuristic);
        bind(PdfFileManager.class).to(getFileManagerClass());
    }

	private Class<? extends PdfFileManager> getFileManagerClass() {
		switch (arguments.outputLayout) {
            case Flat:
                return FlatPdfFileManager.class;
            case Deep:
                return DeepPdfFileManager.class;
            default:
                throw new UnsupportedOperationException(String.format("OutputLayout '%s' is not mapped.", arguments.outputLayout));
        }
	}
}