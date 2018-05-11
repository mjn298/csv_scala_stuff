import java.io.File

import com.github.tototoshi.csv._

val reader = CSVReader.open(new File("/home/mjn/development/dv01/LoanStats3c.csv"))

implicit val head = reader.readNext().get.zipWithIndex.toMap

def !!(v: String)(implicit a: Map[String, Int]): Option[Int] = {
  a.get(v)
}

val desiredFields = List("grade", "loan_amnt", "loan_status",
  "int_rate", "out_prncp", "total_pymnt",
  "total_rec_prncp", "total_rec_int")

val desiredIdx = desiredFields.map(!!(_).get)

def getVals(ls: Seq[String], xs: List[Int]) = {
  xs.map(_ => ls(_))
}

val f = new File("intermediate_out.csv")

val writer = CSVWriter.open(f)

reader.foreach(fields => writer.writeRow(getVals(fields, desiredIdx)))
