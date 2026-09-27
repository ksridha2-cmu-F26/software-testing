# A1: Investigating Bias in Code Smell Detection Models

**Student:** [Your Name]  
**Course:** Software Testing

## Section 1: Study and Experimental Approach

This study compares Data Class, a class-level smell, with Feature Envy, a method-level smell. Both CSVs were downloaded from the official [ESSeRE Lab code-smell dataset page](https://essere.disco.unimib.it/machine-learning-for-code-smell-detection/) through its `binary_class.zip` link. The page's separate `evaluation_dataset.zip` URL currently returns HTTP 404; the analysis therefore uses the related official binary-class release in `data/binary_class/`. Each selected file contains 420 labeled examples, with 140 smelly and 280 non-smelly records across 74 project identifiers. The observed 1:2 ratio describes these samples, not real-world prevalence.

The analysis reports missing values, duplicate metric/label rows, constant and near-constant features, class balance, class-wise metric summaries and plots, and Spearman correlations. Identifiers and names are excluded from predictors; project identifiers are retained only for grouping. Exact duplicate metric/label records are excluded from model evaluation. Decision Tree and class-weighted Random Forest models use median imputation inside a pipeline. A stratified five-fold group split holds out entire projects for final evaluation; grouped cross-validation is restricted to training projects. Smelly is the positive class. Precision, recall, F1, balanced accuracy, and confusion matrices are reported. A second grouped-CV experiment removes selected size-related metrics.

## Section 2: Bias in Software Metric Distributions

Both datasets are moderately imbalanced in this sample: 33.3% smelly and 66.7% non-smelly. Data Class has 75 missing feature cells and 2 duplicate metric/label rows; Feature Envy has 92 missing cells, 13 duplicate rows, and one near-constant feature. Missing numeric values are imputed within each training fold.

The class-wise plots show marked metric shifts. For Data Class, the non-smelly median WOC is 0.733 versus 0.111 for smelly instances, with non-overlapping IQRs; WOC is the proportion of functional public methods among public members. TCC medians are 0.414 and 0.143, respectively, although their IQRs overlap. Feature Envy has non-overlapping IQRs for all six plotted metrics: FDP medians are 0 versus 4, ATFD medians are 0 versus 12, CINT medians are 1 versus 17, and NOAV medians are 3 versus 35.5. These metrics describe foreign-data providers/access, distinct operations called, and accessed variables. The full observed ranges overlap, and count metrics are right-skewed with outliers, so the plots do not establish universal cutoffs.

On the held-out project fold, the Decision Tree achieved F1/balanced accuracy of 0.982/0.992 for Data Class and 0.929/0.939 for Feature Envy. The Random Forest achieved 1.000 on both metrics for both datasets. Such high scores are consistent with strong metric-label associations in this archive, but do not show whether those associations reflect general smell characteristics or the dataset's label construction. The selected samples and class ratio are not representative evidence of broader smell prevalence.

## Section 3: Correlation and Bias Amplification

The Data Class matrix shows strong redundancy between `NOM_project` and `NOMNAMM_project` (Spearman rho 0.992), and between `FANOUT_type` and `CFNAMM_type` (0.985). In Feature Envy, `MaMCL_method` and `MeMCL_method` correlate at 0.9997, while `LOC_type` and `LOCNAMM_type` correlate at 0.9967. Multiple size, complexity, or coupling proxies can therefore repeat similar information and make a dataset-specific metric-label pattern easier for a classifier to exploit.

The size-metric ablation does not show a large performance loss. For example, Random Forest grouped-CV F1 changes from 0.962 to 0.976 for Data Class and from 0.939 to 0.933 for Feature Envy; held-out F1 remains 0.982 and 1.000, respectively, after removal. This sensitivity check does not support size metrics as the sole explanation, though remaining correlated features may substitute for them.

The evidence supports strong class-specific metric patterns and high within-archive predictive performance, including for projects held out from training. It cannot establish causal bias, real-world prevalence, or performance on different projects, metric distributions, or labeling protocols. The perfect Random Forest results come from one held-out fold in the same archive and should be treated cautiously, not as external validation.
