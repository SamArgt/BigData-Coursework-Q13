// drop columns
val rockTracksDfClean = rockTracksDf.drop("id","uri","track_href",
								"analysis_url", "type")
val rapTracksDfClean = rapTracksDf.drop("id","uri","track_href",
								"analysis_url", "type")
val jazzTracksDfClean = jazzTracksDf.drop("id","uri","track_href",
								"analysis_url", "type")
val classicalTracksDfClean = classicalTracksDf.drop("id","uri","track_href",
								"analysis_url", "type")
val electroTracksDfClean = electroTracksDf.drop("id","uri","track_href",
								"analysis_url", "type")


// concatenate tracks
val allTracksDf = rockTracksDfClean.unionByName(rapTracksDfClean).
							unionByName(classicalTracksDfClean).
							unionByName(jazzTracksDfClean).
							unionByName(electroTracksDfClean)

// encode label
import org.apache.spark.ml.feature.StringIndexer

val indexer = new StringIndexer().
  setInputCol("genre").
  setOutputCol("label")

val allTracksDfEnc = indexer.fit(allTracksDf).transform(allTracksDf)
// show correspondance
allTracksDfEnc.select("genre","label").groupBy("genre").mean().show()

val allTracksDfClean = allTracksDfEnc.drop("genre")

// Vector Assembler
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
val assembler = new VectorAssembler().
  setInputCols(Array("danceability", "energy", "key", "loudness", "mode",
  				"speechiness","acousticness","instrumentalness","liveness",
  				"valence", "tempo", "duration_ms", "time_signature")).
  setOutputCol("features")

val allTracksDfPrep = assembler.transform(allTracksDfClean).select("features","label")