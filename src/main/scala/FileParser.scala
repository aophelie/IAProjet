import scala.collection.mutable.HashMap
import scala.io.Source._
import java.io._

class FileParser {
  val dataPath = "resources/data.txt"
  val idsPath = "resources/ids.txt"
  val resultPath = "resources/dataDecode.txt"
  var idsMap: HashMap[String, String] = HashMap()
  var resultList = List[String]()

  // Lecture du fichier des id
  Option(scala.io.Source.fromFile(idsPath)) match {
    case None => {
      println("Error reading the file !")
      System.exit(1)
    }

    case _ => {
      val source = scala.io.Source.fromFile(idsPath)
      val lines = try source.getLines.mkString("\n").split("\n").toList finally source.close()

      for(line <- lines){
        idsMap += (line.split("\t")(1) -> line.split("\t")(0))

      }
      println("count id = " + idsMap.size)
    }
  }

  // Lecture et mapping du fichier des données
  Option(scala.io.Source.fromFile(dataPath)) match {
    case None => {
      println("Error reading the file !")
      System.exit(1)
    }

    case _ => {
      val source = scala.io.Source.fromFile(dataPath)
      val lines = try source.getLines.mkString("\n").split("\n").toList finally source.close()

      for(line <- lines){
        val s = line.split(" ")(0)
        println("s = " + s)
        val p = line.split(" ")(1)
        println("p = " + p)
        val o = line.split(" ")(2)
        println("o = " + o)
        val originalS = getKey(idsMap, s)
        val originalP = getKey(idsMap, p)
        val originalO = getKey(idsMap, o)
        println("originalS = " + originalS)
        println("originalP = " + originalP)
        println("originalO = " + originalO)

        println()
        println()
        val resultLine = originalS + " " + originalP + " " + originalO + "\n"
        resultList = resultList :+ resultLine

      }
      println("count result = " + resultList.size)
    }
  }

  def getKey(hashmap : HashMap[String, String], key : String) : String = {
    hashmap.get(key) match {
      case None => "NAN"
      case _ => hashmap.get(key).get
    }
  }

  // Exriture dans le fichier dataDecode.txt
  val writer = new BufferedWriter(new FileWriter(resultPath))
  resultList.foreach(writer.write)
  writer.close()
}
