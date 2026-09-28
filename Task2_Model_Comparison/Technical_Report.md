# A1: Investigating Bias in Code Smell Detection Models

**Student:** Karthik Sridhar  
**Course:** Data Science for Software Engineering

## Section 1: Study and Experimental Approach

I selected Data Class and Feature Envy because they represent different code levels: Data Class describes a class, while Feature Envy describes a method. The two CSVs are from the official ESSeRE Lab `binary_class.zip` release. Each contains 420 labeled examples from 74 projects: 140 smelly and 280 non-smelly. This 1:2 balance is the makeup of the provided samples, not an estimate of smell prevalence in software generally.

I checked class counts, missing values, repeated metric/label rows, and constant features. I compared metric distributions by label and calculated Spearman correlations. I then compared a Decision Tree and a class-weighted Random Forest. Missing values are filled within each model pipeline, and project names are used to keep test projects separate from training and cross-validation projects. Smelly examples are the positive class. I report precision, recall, F1, balanced accuracy, and confusion matrices, and also compare results after removing selected size-related metrics.

## Section 2: Bias in Software Metric Distributions

Both samples have the same class balance: one third smelly and two thirds non-smelly. Data Class has 75 missing metric values and 2 repeated metric/label rows. Feature Envy has 92 missing values, 13 repeated rows, and one near-constant metric. These issues are reported in the notebook; missing values are handled during training.

Several metrics differ noticeably by label. For Data Class, median WOC is 0.733 for non-smelly examples and 0.111 for smelly examples. The middle half of the values does not overlap, although the full ranges do. TCC also has different medians (0.414 and 0.143), but the middle ranges overlap. In Feature Envy, the middle half of each of the six plotted metrics is separated by label. For example, the FDP medians are 0 and 4, and the ATFD medians are 0 and 12. The count metrics are skewed, with some large outliers. These patterns suggest that the measured metrics closely track the labels ein these samples, but they do not provide universal thresholds for detecting smells.

The held-out-project Random Forest reached an F1 score of 1.000 for both datasets. The Decision Tree reached 0.982 for Data Class and 0.929 for Feature Envy. These high scores show that the models can distinguish the labels in this split. They do not establish that the sample represents other projects or that the same performance would hold with a different labeling process.

## Section 3: Correlation and Bias Amplification

Several predictors measure similar things. For Data Class, `NOM_project` and `NOMNAMM_project` have a Spearman correlation of 0.992; `FANOUT_type` and `CFNAMM_type` correlate at 0.985. For Feature Envy, `MaMCL_method` and `MeMCL_method` correlate at 0.9997, while `LOC_type` and `LOCNAMM_type` correlate at 0.9967. When related metrics also differ by label, a model may receive the same signal in several forms.

Removing the selected size-related metrics caused only small changes in the Random Forest results. Grouped cross-validation F1 changed from 0.962 to 0.976 for Data Class and from 0.939 to 0.933 for Feature Envy. The held-out F1 scores changed from 1.000 to 0.982 and stayed at 1.000, respectively. This experiment does not suggest that size metrics alone explain the predictions; other related metrics may carry similar information.

Overall, these results show strong metric-label patterns in the two selected files. They cannot tell us how common code smells are in real software, prove that a metric causes a smell, or guarantee performance on different projects or labeling methods. The held-out projects come from the same archive, so the high scores should be treated as evidence about this sample rather than proof of broad generalization.