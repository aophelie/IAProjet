import java.util

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.io.Source


object Main extends App {
  // création du fichier de données (on décode le fichier et dataDecode.txt est créé)
  //Nécessaire juste pour update les données (faire la jointure des fichiers de données)
  val fileParser = new FileParser()

  // exécution des requête
  val jenaQuery = new JenaQuery()

}



