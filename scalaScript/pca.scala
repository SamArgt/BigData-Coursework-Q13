import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.feature.PCA
import org.apache.spark.ml.linalg.Vectors

val scaler = new StandardScaler().
  setInputCol("features").
  setOutputCol("scaledFeatures").
  setWithStd(true).
  setWithMean(true)

val scalerModel = scaler.fit(allTracksDfPrep)

val scaledData = scalerModel.transform(allTracksDfPrep)

val pca = new PCA().
  setInputCol("scaledFeatures").
  setOutputCol("pcaFeatures").
  setK(2).
  fit(scaledData)

val result = pca.transform(scaledData).select("pcaFeatures", "label")
result.show(3)

result.write.saveAsTable("file:///home/user368/BigData-Coursework-Q13/data/pcaFeatures")