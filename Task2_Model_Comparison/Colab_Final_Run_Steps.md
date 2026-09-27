# Final Run in Google Colab

## Before you start

1. Open `A1_Bias_Identification.ipynb` in Google Colab. Use a fresh runtime if you have already tried running it.
2. Get the two CSV files from the extracted `binary_class` folder in `binary_class.zip`:
   - `data-class.csv`
   - `feature-envy.csv`
3. You do not need to upload `binary_class.zip` or the other four CSVs. This notebook analyzes only Data Class and Feature Envy.

## Run the notebook

1. Run the cells from the top, in order. The setup cell detects Colab and asks you to upload the two CSVs if they are not already in the runtime.
2. Select both CSVs in the upload dialog. Keep the filenames unchanged.
3. Continue running all remaining cells. Check that the load cell reports Data Class and Feature Envy with 420 rows each. The analysis cells should produce the overview tables, plots, correlation tables, model results, confusion matrices, and size-metric comparison.
4. If a cell errors, stop there and read the error before running later cells. Do not copy old outputs into the final notebook.

## Update the report with the final run

The notebook calculates the authoritative values. Compare its latest outputs with the report and update any values that differ:

- Dataset counts, class percentages, missing values, duplicate rows, and near-constant features from the overview table.
- Metric medians and distribution observations from the metric summary output.
- Correlations from the table of metric pairs with absolute Spearman correlation at least 0.85.
- Cross-validation F1 and held-out F1 values from the model-results tables.
- The before/after results from the size-metric comparison.

Use the final-run results rather than the values from an earlier execution. Describe the scores as results for these datasets and this split; do not claim they prove performance on all projects or real-world smell prevalence. Keep the report to three pages or fewer of main text and retain exactly the three required section headings.

## Save submission files

1. In Colab, use **File > Download > Download .ipynb** to save the executed notebook with its outputs.
2. Export or print the executed notebook to PDF and check that the tables and plots are visible.
3. Export `Technical_Report_Draft.md` to PDF after updating it. Confirm the student name and formatting.
4. Put the executed `.ipynb`, notebook PDF, and report PDF in the submission ZIP. The CSV files and this checklist are not required in the submission ZIP unless the instructor requests them.
