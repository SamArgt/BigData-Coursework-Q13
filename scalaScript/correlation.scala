import org.apache.spark.ml.linalg.{Matrix, Vectors}
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.sql.Row
val Row(coeff: Matrix) = Correlation.
		corr(allTracksDfPrep,"features", "spearman").head


import java.io._

val rowIterator = coeff.rowIter
val corrFile = "correlationMatrix.txt"
val writer = new BufferedWriter(new FileWriter(corrFile))
while (rowIterator.hasNext){
	val row = rowIterator.next().toArray.map(_.toString)
	row.foreach(ln => {
		writer.write(ln)
		writer.write("\t")
	})
	writer.write("\n")
}
writer.close()
