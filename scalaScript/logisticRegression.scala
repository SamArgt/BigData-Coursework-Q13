// Vector Assembler
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
val assembler = new VectorAssembler().
  setInputCols(Array("danceability", "energy", "key", "loudness", "mode",
  				"speechiness","acousticness","instrumentalness","liveness",
  				"valence", "tempo", "duration_ms", "time_signature")).
  setOutputCol("features")

val allTracksDfPrep = assembler.transform(allTracksDfClean).select("features","label")

// Logistic Regression
import org.apache.spark.ml.classification.LogisticRegression

val lr = new LogisticRegression().
  setMaxIter(10).
  setRegParam(0.3).
  setElasticNetParam(0.8)

// Fit the model
val lrModel = lr.fit(allTracksDfPrep)

// Print the coefficients and intercept for multinomial logistic regression
println(s"Coefficients: \n${lrModel.coefficientMatrix}")
println(s"Intercepts: \n${lrModel.interceptVector}")

val trainingSummary = lrModel.summary
// Obtain the objective per iteration
val objectiveHistory = trainingSummary.objectiveHistory
println("objectiveHistory:")
objectiveHistory.foreach(println)
// for multiclass, we can inspect metrics on a per-label basis
println("False positive rate by label:")
trainingSummary.falsePositiveRateByLabel.zipWithIndex.foreach { case (rate, label) =>
  println(s"label $label: $rate")
}
println("True positive rate by label:")
trainingSummary.truePositiveRateByLabel.zipWithIndex.foreach { case (rate, label) =>
  println(s"label $label: $rate")
}
println("Precision by label:")
trainingSummary.precisionByLabel.zipWithIndex.foreach { case (prec, label) =>
  println(s"label $label: $prec")
}
println("Recall by label:")
trainingSummary.recallByLabel.zipWithIndex.foreach { case (rec, label) =>
  println(s"label $label: $rec")
}
println("F-measure by label:")
trainingSummary.fMeasureByLabel.zipWithIndex.foreach { case (f, label) =>
  println(s"label $label: $f")
}
val accuracy = trainingSummary.accuracy
val falsePositiveRate = trainingSummary.weightedFalsePositiveRate
val truePositiveRate = trainingSummary.weightedTruePositiveRate
val fMeasure = trainingSummary.weightedFMeasure
val precision = trainingSummary.weightedPrecision
val recall = trainingSummary.weightedRecall
println(s"Accuracy: $accuracy\nFPR: $falsePositiveRate\nTPR: $truePositiveRate\n" +
  s"F-measure: $fMeasure\nPrecision: $precision\nRecall: $recall")