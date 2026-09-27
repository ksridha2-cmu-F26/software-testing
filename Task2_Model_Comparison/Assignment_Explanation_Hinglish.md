# A1 Assignment: Hinglish Explanation and Final-Run Checklist

## 1. Assignment Mein Kya Maanga Gaya Hai?

Assignment ka goal sirf high model score lena nahi hai. Humein dekhna hai ki code-smell labels aur software metrics ke beech kya patterns hain, aur kya woh patterns doosre projects par bhi lagu ho sakte hain.

Task 1 ke liye kam-se-kam do datasets analyze karne hain, jo alag smell types ko represent karein. Har dataset ke liye class counts, data quality, metric distributions, correlations, aur models ka evaluation dikhana hai. Graphs ke saath unka matlab bhi explain karna hai.

Task 2 ke liye technical report chahiye, jisme exactly ye teen required headings hon:

1. `Section 1: Study and Experimental Approach`
2. `Section 2: Bias in Software Metric Distributions`
3. `Section 3: Correlation and Bias Amplification`

Report main text teen pages se zyada nahi hona chahiye. Notebook aur report dono mein student ka naam hona chahiye. Final submission ZIP mein executed notebook, executed notebook ka PDF, aur report ka PDF hona chahiye.

## 2. Kaunse Datasets Use Ho Rahe Hain?

Notebook do smell types use karta hai:

- `data-class.csv`: Data Class, jo class-level smell hai.
- `feature-envy.csv`: Feature Envy, jo method-level smell hai.

Dono files official ESSeRE Lab page par listed `binary_class.zip` release ke andar hain. Assignment page ka alag `evaluation_dataset.zip` link download ke waqt HTTP 404 de raha tha, isliye analysis available official binary-class archive ke in do datasets par based hai. Is baat ko report mein sahi tarah batana chahiye.

Notebook mein har file ke 420 examples hain: 140 smelly aur 280 non-smelly. Ye sample ka balance hai, real projects mein smell prevalence ka estimate nahi. Colab mein upload prompt aaye toh sirf ye dono CSV files upload karni hain. Baaki chaar CSVs current notebook analysis mein use nahi hote.

## 3. Notebook Mein Kya Kiya Gaya Hai?

### Dataset Overview

Notebook rows, numeric feature counts, smelly/non-smelly counts, missing metric values, duplicate metric/label rows, aur constant/near-constant features report karta hai. Current saved run mein Data Class ke 75 missing values aur 2 duplicate metric/label rows hain. Feature Envy mein 92 missing values, 13 duplicate rows, aur ek near-constant feature hai.

Model features se IDs aur artifact names hata diye gaye hain. `project` column model feature nahi hai; uska use sirf projects ko training aur test groups mein alag rakhne ke liye hota hai. Exact duplicate metric/label rows model evaluation mein include nahi kiye jaate. Missing numeric values model pipeline ke andar fill hoti hain.

### Class Distribution

Tables aur plots dikhate hain ki dono CSVs mein 33.3% examples smelly aur 66.7% non-smelly hain. Model mein smelly class ko positive class (`1`) maana gaya hai. Is imbalance ko samajhne ke liye notebook accuracy ke saath precision, recall, F1 aur balanced accuracy bhi report karta hai.

### Metric Distributions

Notebook har dataset ke un chhe metrics ko plot karta hai jinke smelly/non-smelly groups ke standardized mean differences sabse bade hain. Histograms aur boxplots ke saath median, skew, IQR overlap, aur observed range bhi print hote hain.

Saved run ke examples:

- Data Class mein WOC median non-smelly ke liye `0.733` aur smelly ke liye `0.111` tha. Dono groups ke middle 50% values alag the, lekin complete observed ranges overlap karte the.
- Feature Envy mein FDP median `0` vs `4`, aur ATFD median `0` vs `12` tha. Selected chhe metrics ke IQRs alag the, par isse universal smell threshold prove nahi hota.

Ye values current saved run ke hain. Final Colab run ke baad unhe dobara check karna hai.

### Correlation Analysis

Spearman correlation heatmaps aur threshold se upar ke metric-pair tables dikhaye jaate hain. Saved run mein Data Class ke `NOM_project` aur `NOMNAMM_project` ka rho `0.992` tha. Feature Envy mein `MaMCL_method` aur `MeMCL_method` ka rho `0.9997` tha.

Iska matlab related metrics similar information encode kar sakte hain. Correlation apne aap bias ya causation prove nahi karta; isliye report mein ise class-wise metric differences aur model results ke saath jodna hai.

### Model Evaluation

Do models compare hote hain: Decision Tree aur class-weighted Random Forest. Notebook project groups ko alag rakhkar training aur held-out test banata hai, aur training projects par grouped cross-validation karta hai. Median imputation model pipeline ke andar hoti hai. Test set ko model selection ke liye use nahi karna chahiye.

Saved run mein Random Forest held-out F1 dono datasets par `1.000` tha. Decision Tree held-out F1 Data Class ke liye `0.982` aur Feature Envy ke liye `0.929` tha. Confusion matrices aur baaki required metrics notebook mein aate hain. Ek held-out fold ka perfect score broad real-world performance ki guarantee nahi hai.

### Size-Metric Comparison

Notebook selected LOC/count/size-related features hata kar models phir se evaluate karta hai. Isse check hota hai ki results sirf size metrics par depend karte hain ya nahi. Saved run mein Random Forest grouped-CV F1 Data Class ke liye `0.962` se `0.976`, aur Feature Envy ke liye `0.939` se `0.933` hua. Ye size ko akela explanation nahi banata; bache hue related metrics similar signal de sakte hain.

## 4. Final Colab Run Kaise Karna Hai?

1. `A1_Bias_Identification.ipynb` ko Google Colab mein kholo.
2. Fresh runtime use karo, khaaskar agar pehle notebook partially run hua ho.
3. Notebook ke cells top se bottom tak order mein chalao.
4. Upload prompt aane par `data-class.csv` aur `feature-envy.csv` select karo. Filenames change mat karo.
5. Loader output check karo: har dataset mein 420 rows aur 74 project groups report hone chahiye.
6. Aage ke saare cells complete chalao. Koi error aaye toh usse samjhe bina aage ke cells mat chalao.
7. Final output tables, plots, confusion matrices aur ablation table visible hain, ye verify karo.

## 5. Final Run Ke Baad Report Mein Kya Manually Update Karna Hai?

Notebook ke calculated tables aur plots manually edit nahi karne. Colab mein cells rerun honge toh woh outputs khud regenerate honge. Manually sirf report ke text mein diye gaye numbers/claims ko final outputs se verify karke update karna hai.

### Report Section 1

- Dataset names aur smell levels sahi hain, ye confirm karo.
- Agar dataset rows/class counts final run mein different hon, report mein counts update karo.
- Evaluation method ya models change kiye hon, toh unka description update karo.

### Report Section 2

Notebook overview table se verify/update karo:

- Smelly/non-smelly counts aur percentages.
- Missing value counts.
- Duplicate metric/label rows.
- Near-constant feature count.

Metric distribution outputs se verify/update karo:

- WOC, TCC, FDP, ATFD jaise metrics ke cited medians.
- IQR overlap/separation aur skew/outliers ke observations.
- Model results table se Decision Tree aur Random Forest ke held-out F1, precision, recall aur balanced accuracy ke cited values.

### Report Section 3

Correlation table se verify/update karo:

- Report mein use kiye gaye high-correlation metric pairs aur rho values.
- Size-metric comparison table se grouped-CV aur held-out F1 before/after values.
- Interpretation: kya size metrics hatane par performance noticeably badli, ya lagbhag same rahi?

Agar final run ke numbers saved run jaise hi hon, unhe change karne ki zaroorat nahi. Agar alag hon, purane values replace karo. Naye numbers ko outputs se copy karo; guess mat karo. Conclusions ko inhi datasets aur isi evaluation run tak limit rakho.

## 6. Submission Se Pehle Checklist

- [ ] Student ka naam notebook aur report mein sahi hai.
- [ ] Colab mein notebook top-to-bottom bina error ke run hua.
- [ ] Notebook ke final outputs aur figures visible hain.
- [ ] Report ke numbers final Colab outputs se match karte hain.
- [ ] Report mein exactly teen required section headings hain.
- [ ] Report ka main text teen pages se zyada nahi hai.
- [ ] Executed notebook `.ipynb` ke roop mein download kiya.
- [ ] Executed notebook ka PDF banaya aur plots/tables check kiye.
- [ ] Report ka PDF banaya aur formatting/name check kiya.
- [ ] Teeno required files submission ZIP mein rakhe: executed `.ipynb`, notebook PDF, aur report PDF.
