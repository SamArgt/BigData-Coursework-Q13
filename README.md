# BigData-Coursework-Q13
 Question 13 of the Big Data Module Coursework - Imperial College London - Spring Term 2020

## Question
Identify, download, and perform a statistical analysis of any suitable data that is available on the
internet, and write a one-page summary of your findings. Your analysis should use Hadoop,
Spark, or both tools. Please note that the data need not be “big” – the question is intended to
assess your approach to the analysis, and how you utilise Big Data technology in performing a
statistical analysis.

## The Data
The data is downloaded from the spotify API. The code to obtain the data is in question13_scrapping.ipynb python notebook.
There are 5 datasets in the data/ folder, one dataset for each genre.
Each dataset contains features (columns) about tracks (rows).

## scalaScript/
This folder contains scala scripts to be executed on spark.
There is an order to respect:
1. loadData.scala
2. preprocessing.scala
3. The others

pca.scala : Compute the first 3 principal components and save the output
randomForest.scala and mlpClf.scala : train a randomForest and a MLP classifier respectively and print the accuracy.

# Rscript/
This folder contains some R code to plot the graphs
