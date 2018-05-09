# PDF Tables Extractor

Simple wrapper around [tabula-java](https://github.com/tabulapdf/tabula-java) extracting tables, the page number on which the tables are extracted and the image of the pages containing tables.

Sample of output:
```
├───Doc 1
│   │   document.json
│   │   
│   ├───pages
│   │       page.002.json
│   │       page.002.png
│   │       ...
│   │       
│   └───tables
│           page.002.table.00.csv
│           page.002.table.00.json
│           ...
│           
├───Doc N
│   ...
```

## Requirements

Java 8

## Download

<a href="/ewoij/pdf-tables-extractor/releases/download/v1.0.0/tables-extractor-1.0.jar" rel="nofollow">
<small class="text-gray float-right">11.5 MB</small>
<svg class="octicon octicon-package text-gray d-inline-block" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M1 4.27v7.47c0 .45.3.84.75.97l6.5 1.73c.16.05.34.05.5 0l6.5-1.73c.45-.13.75-.52.75-.97V4.27c0-.45-.3-.84-.75-.97l-6.5-1.74a1.4 1.4 0 0 0-.5 0L1.75 3.3c-.45.13-.75.52-.75.97zm7 9.09l-6-1.59V5l6 1.61v6.75zM2 4l2.5-.67L11 5.06l-2.5.67L2 4zm13 7.77l-6 1.59V6.61l2-.55V8.5l2-.53V5.53L15 5v6.77zm-2-7.24L6.5 2.8l2-.53L15 4l-2 .53z"></path></svg>
<strong class="pl-1">tables-extractor-1.0.jar</strong>
</a>

## Run

```bash
java -jar tables-extractor-1.0.jar <input dir> <output dir>
```

Parameters:
 * Input dir: A directory containing PDF files. Only PDF at the root are processed.
 * Output dir: The directory where the output will be created.