import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.ml.feature.StandardScaler

val labelIndexer = new StringIndexer().
  setInputCol("genre").
  setOutputCol("indexedLabel").
  fit(allTracksDfPrep)
 // Split the data into training and test sets (30% held out for testing).
val Array(trainingData, testData) = allTracksDfPrep.randomSplit(Array(0.7, 0.3), seed=123L)

val scalerModel = new StandardScaler().
  setInputCol("features").
  setOutputCol("scaledFeatures").
  setWithStd(true).
  setWithMean(true).
  fit(trainingData)
// Train a MLP model.
val layers = Array[Int](13, 5, 4, 5)
// create the trainer and set its parameters
val mlp = new MultilayerPerceptronClassifier().
  setLayers(layers).
  setBlockSize(128).
  setSeed(1234L).
  setMaxIter(100).
  setLabelCol("indexedLabel").
  setFeaturesCol("scaledFeatures")
// Convert indexed labels back to original labels.
val labelConverter = new IndexToString().
  setInputCol("prediction").
  setOutputCol("predictedGenre").
  setLabels(labelIndexer.labels)
// Chain indexers and GBT in a Pipeline.
val pipelineMlp = new Pipeline().
  setStages(Array(labelIndexer, scalerModel, mlp, labelConverter))
// Train model. This also runs the indexers.
val mlpModel = pipelineMlp.fit(trainingData)
// Make predictions.
val mlpPredictions = mlpModel.transform(testData)
// Select (prediction, true label) and compute test accuracy
val evaluator = new MulticlassClassificationEvaluator().
  setLabelCol("indexedLabel").
  setPredictionCol("prediction").
  setMetricName("accuracy")

val mlpAccuracy = evaluator.evaluate(mlpPredictions)
println(s"Test accuracy ${mlpAccuracy}")
// compute test accuracy for each genre
val genres = Array("rock", "rap", "jazz", "electro", "classical")
genres.foreach{ genre =>
  val mlpPredictionsGenre = mlpPredictions.filter($"genre" contains genre)
  val mlpAccuracyGenre = evaluator.evaluate(mlpPredictionsGenre)
  println(s"${genre}  Test Accuracy = ${mlpAccuracyGenre}")
}
// investigate the predictions for electro and jazz
mlpPredictions.filter($"genre" contains "electro").groupBy("predictedGenre").count.show()
mlpPredictions.filter($"genre" contains "jazz").groupBy("predictedGenre").count.show()