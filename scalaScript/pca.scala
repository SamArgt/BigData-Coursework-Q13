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

// Compute cumulative Explained Variance
pca.explainedVariance.toArray.sum


// Export results
import org.apache.spark.sql.functions._
import org.apache.spark.ml._
// A UDF to convert VectorUDT to ArrayType
val vecToArray = udf( (xs: linalg.Vector) => xs.toArray )

// Add a ArrayType Column   
val dfArr = result.withColumn("featuresArr" , vecToArray($"pcaFeatures") )

// Array of element names that need to be fetched
// ArrayIndexOutOfBounds is not checked.
// sizeof `elements` should be equal to the number of entries in column `features`
val elements = Array("PC1","PC2","PC3")

// Create a SQL-like expression using the array 
val sqlExpr = elements.zipWithIndex.map{ case (alias, idx) => col("featuresArr").getItem(idx).as(alias) }

// Extract Elements from dfArr    
val output = dfArr.select( (col("genre") +: sqlExpr) :_*)


output.write.format("csv").
save("file:///home/user368/BigData-Coursework-Q13/data/pcaFeatures")
