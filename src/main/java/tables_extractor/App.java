package tables_extractor;

import com.google.inject.Guice;

import tables_extractor.arguments.ArgumentParser;
import tables_extractor.arguments.Arguments;
import tables_extractor.inject.AppModule;

public class App
{
    public static void main(String[] args) throws Exception
    {
        Arguments args_ = ArgumentParser.parse(args);
        if (args_ == null) return;
        AppModule appModule = new AppModule(args_);
        Guice.createInjector(appModule).getInstance(Main.class).execute();
    }
}
