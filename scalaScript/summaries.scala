allTracksDf.groupBy("genre").agg(avg("energy"), avg("loudness"), avg("danceability"),
		avg("speechiness"), avg("instrumentalness"), avg("duration_ms")).show()

allTracksDf.groupBy("genre").agg(stddev("energy"), stddev("loudness"), stddev("danceability"),
		stddev("speechiness"), stddev("instrumentalness"), stddve("duration_ms")).show()

allTracksDf.groupBy("genre", "mode").agg(count("mode")).orderBy("genre").show()

import org.apache.spark.ml.linalg.{Matrix, Vectors}
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.sql.Row
val coeff = Correlation.corr(allTracksDfPrep,"features", "spearman")

val coeffRow = coeff.collect()(0)

import java.io._
val corrFile = "correlationMatrix.txt"
val writer = new BufferedWriter(new FileWriter(corrFile))
writer.write(coeffRow.toString())
writer.close()