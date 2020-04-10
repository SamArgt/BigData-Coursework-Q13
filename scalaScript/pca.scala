import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.feature.PCA
import org.apache.spark.ml.linalg.Vectors

// scaled the data
val scaler = new StandardScaler().
  setInputCol("features").
  setOutputCol("scaledFeatures").
  setWithStd(true).
  setWithMean(true)
val scalerModel = scaler.fit(allTracksDfPrep)
val scaledData = scalerModel.transform(allTracksDfPrep)
// Compute PCs
val pca = new PCA().
  setInputCol("scaledFeatures").
  setOutputCol("pcaFeatures").
  setK(3).
  fit(scaledData)

val result = pca.transform(scaledData).select("pcaFeatures", "genre")
result.show(3)
// Compute Explained Variance
pca.explainedVariance.toArray.foreach(v => println(v))

// Export results
import org.apache.spark.sql.functions._
import org.apache.spark.ml._
val vecToArray = udf( (xs: linalg.Vector) => xs.toArray )
val dfArr = result.withColumn("featuresArr" , vecToArray($"pcaFeatures") )
val elements = Array("PC1","PC2","PC3")
val sqlExpr = elements.zipWithIndex.map{ case (alias, idx) => col("featuresArr").getItem(idx).as(alias) }
val output = dfArr.select( (col("genre") +: sqlExpr) :_*)
//output.write.format("csv").
//save("file:///home/user368/BigData-Coursework-Q13/data/pcaFeatures")
