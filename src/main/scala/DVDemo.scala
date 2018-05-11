import java.io.File
import com.github.tototoshi.csv._

object DVDemo extends App {

  val file_path = "YOUR/PATH/HERE"
  val intermediate_file_path = "YOUR/PATH/HERE"

  val reader = CSVReader.open(new File(file_path))

  implicit val head = reader.readNext().get.zipWithIndex.toMap

  def !!(v: String)(implicit a: Map[String, Int]): Option[Int] = {
    a.get(v)
  }

  val desiredFields = List("grade", "loan_amnt", "loan_status",
    "int_rate", "out_prncp", "total_pymnt",
    "total_rec_prncp", "total_rec_int")

  val desiredIdx = desiredFields.map(!!(_).get)

  def getVals(ls: Seq[String], xs: List[Int]): List[String] = {
    try xs.map(ls(_))
    catch {
      case e: Exception => List()
    }
  }

  val f = new File("intermediate_out.csv")

  val writer = CSVWriter.open(f)

//  writer.writeRow(desiredFields)
  reader.foreach(fields => writer.writeRow(getVals(fields, desiredIdx)))

  reader.close()
  writer.close()

  val r2 = CSVReader.open(new File(intermediate_file_path))
  val resultsMap = Map[String, List[String]]
  r2.toStream.groupBy(_(0)).aggregate()

  // These two methods don't really work

  def avg(xs: List[Int]): Double = {
    xs.reduce(_ + _) / xs.size
  }

  def sum(xs: List[Int]): BigInt = {
    xs.reduce(_ + _)
  }
}
