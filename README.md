# PDF Tables Extractor

Simple wrapper around [tabula-java](https://github.com/tabulapdf/tabula-java) extracting tables as CSV and saving pages as PNG.

A simple heuristic is used to on top of tabula to filter relevant tables.

Sample of output:
```
doc1.document.json
doc1.page.005.json
doc1.page.005.png
doc1.page.005.table.00.csv
doc1.page.005.table.00.json
doc2.document.json
doc3.document.json
doc3.page.004.json
doc3.page.004.png
doc3.page.004.table.00.csv
doc3.page.004.table.00.json
log-20180527-170650.log
```

## Requirements

Java 8

## Download

See [Releases](https://github.com/ewoij/pdf-tables-extractor/releases).

## Run

```
>java -jar tables-extractor-2.0.0-jar-with-dependencies.jar --help
usage: PDF Tables Extractor [-h] --output-dir OUTPUTDIRECTORY
                            [--output-mode {Flat,Deep}] [--no-heuristic]
                            (--input-dir INPUTDIRECTORY |
                            --input-file INPUTFILE)

named arguments:
  -h, --help             show this help message and exit
  --input-dir INPUTDIRECTORY, -i INPUTDIRECTORY
  --input-file INPUTFILE, -f INPUTFILE
  --output-dir OUTPUTDIRECTORY, -o OUTPUTDIRECTORY
  --output-mode {Flat,Deep}, -m {Flat,Deep}
  --no-heuristic, -n
```

Example:
```bash
java -jar tables-extractor-2.0.0-jar-with-dependencies.jar -i directory-containing-pdfs -o output-dir
```
