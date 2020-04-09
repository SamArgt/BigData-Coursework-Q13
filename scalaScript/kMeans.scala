import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator

// Trains a k-means model.
val kmeans = new KMeans().setK(5).setSeed(1L)
val model = kmeans.fit(allTracksDfPrep)

// Make predictions
val predictions = model.transform(allTracksDfPrep)

// Evaluate clustering by computing Silhouette score
val evaluator = new ClusteringEvaluator()

val silhouette = evaluator.evaluate(predictions)
println(s"Silhouette with squared euclidean distance = $silhouette")

// Shows the result.
println("Cluster Centers: ")
model.clusterCenters.foreach(println)