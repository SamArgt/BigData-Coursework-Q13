import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}

val labelIndexer = new StringIndexer().
  setInputCol("genre").
  setOutputCol("indexedLabel").
  fit(allTracksDfPrep)
// Automatically identify categorical features, and index them.
// Set maxCategories so features with > 11 distinct values are treated as continuous.
val featureIndexer = new VectorIndexer().
  setInputCol("features").
  setOutputCol("indexedFeatures").
  setMaxCategories(11).
  fit(allTracksDfPrep)
// Split the data into training and test sets (30% held out for testing).
val Array(trainingData, testData) = allTracksDfPrep.randomSplit(Array(0.7, 0.3),  seed=123L)
// Train a RandomForest model.
val rf = new RandomForestClassifier().
  setLabelCol("indexedLabel").
  setFeaturesCol("indexedFeatures").
  setNumTrees(20)
// Convert indexed labels back to original labels.
val labelConverter = new IndexToString().
  setInputCol("prediction").
  setOutputCol("predictedGenre").
  setLabels(labelIndexer.labels)
// Chain indexers and forest in a Pipeline.
val pipeline = new Pipeline().
  setStages(Array(labelIndexer,featureIndexer, rf, labelConverter))
// Train model. This also runs the indexers.
val model = pipeline.fit(trainingData)
// Make predictions.
val predictions = model.transform(testData)
// Select example rows to display.
predictions.select("predictedGenre", "genre", "features").show(5)

// Select (prediction, true label) and compute test accuracy
val evaluator = new MulticlassClassificationEvaluator().
  setLabelCol("indexedLabel").
  setPredictionCol("prediction").
  setMetricName("accuracy")
val accuracy = evaluator.evaluate(predictions)
println(s"Test accuracy ${accuracy}")

// compute test accuracy for each genre
val genres = Array("rock", "rap", "jazz", "electro", "classical")
genres.foreach{ genre =>
	val predictionsGenre = predictions.filter($"genre" contains genre)
	val accuracyGenre = evaluator.evaluate(predictionsGenre)
	println(s"${genre}  Test Accuracy = ${accuracyGenre}")
}
// investigate the predictions for electro and jazz
predictions.filter($"genre" contains "electro").groupBy("predictedGenre").count.show()
predictions.filter($"genre" contains "jazz").groupBy("predictedGenre").count.show()