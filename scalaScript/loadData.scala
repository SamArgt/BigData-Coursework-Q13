val rockTracksDf = spark.read.format("csv").
  option("sep", ",").option("inferSchema", "true").option("header", "true").
  load("file:///home/user368/BigData-Coursework-Q13/data/rock_tracks.csv")

val rapTracksDf = spark.read.format("csv").
  option("sep", ",").option("inferSchema", "true").option("header", "true").
  load("file:///home/user368/BigData-Coursework-Q13/data/rap_tracks.csv")

val classicalTracksDf = spark.read.format("csv").
  option("sep", ",").option("inferSchema", "true").option("header", "true").
  load("file:///home/user368/BigData-Coursework-Q13/data/classical_tracks.csv")

val jazzTracksDf = spark.read.format("csv").
  option("sep", ",").option("inferSchema", "true").option("header", "true").
  load("file:///home/user368/BigData-Coursework-Q13/data/jazz_tracks.csv")

val electroTracksDf = spark.read.format("csv").
  option("sep", ",").option("inferSchema", "true").option("header", "true").
  load("file:///home/user368/BigData-Coursework-Q13/data/electro_tracks.csv")

// print columns
rockTracksDf.printSchema()

// show three first lines
rockTracksDf.show(3)





// get correlation
val corr1 = rockTracksDfClean.stat.corr("energy", "loudness")


// convert to Dataset type
case class AudioFeatures(danceability:Double, energy:Double, key:Int, 
			loudness:Double, mode:Int, speechiness:Double, acousticness:Double, 
			instrumentalness:Double, liveness:Double, valence:Double,
  		tempo:Double, duration_ms:Int, time_signature:Int, genre:String)

val rockTracksDs = rockTracksDf.as[AudioFeatures]




