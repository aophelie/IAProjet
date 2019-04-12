
import org.apache.jena.query.QueryFactory
import org.apache.jena.sparql.algebra._
import org.apache.jena.sparql.algebra.walker.Walker
import visitor._
import org.apache.spark.sql.SparkSession


class JenaQuery extends App {


  //val prefix = "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>"

  def parseMyQuery() = {
  val myQuery1 =

  "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
    "PREFIX ub:<http://www.univ-mlv.fr/~ocure/lubm.owl#>" +
   " PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
   " PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
   " SELECT ?x WHERE {" +
   " ?x rdf:type ub:GraduateStudent ." +
     " ?x ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>." +
      "} "


    val myQuery = " PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
      " SELECT ?s {" +
      " ?s <http://purl.oclc.org/NET/ssnx/hasValue> ?o." +
      "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?o1." +
      "Filter (?o > 32)" +
      "} "

    //Création de la requête à partir de la variable String myQuery
    val query = QueryFactory.create(myQuery)

    //Transformation de la requête en algèbre relationnel
    val op = Algebra.compile(query)
   // println("op : " + op)

    val query1 = QueryFactory.create(myQuery1)
    val op1 = Algebra.compile(query1)

    println("op1 : " + op1)



    /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////// TEST VISITOR POUR PARCOURIR DE L'ALGEBRE /////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    // Parcours de l'algèbre obtenu en utilisant notre visitor

    Walker.walk(op1,new Visitor)


    /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////// CRETION DATAFRAME EN UTILISANT SPARKSESSION //////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    //variante création SparkSession
    //val spark = SparkSession.builder().appName("testings").master("local").config("configuration key", "configuration value").getOrCreate

    // For implicit conversions like converting RDDs to DataFrames (toDF,...)
    import spark.implicits._

    val sqlContext = spark.sqlContext

    val mydataframe = spark.read.text("D:\\M2-IFLogiciels\\IA\\LUBM encodés-20190321\\LUBMInstances\\part-00000.txt")

    /*val dataframe = spark.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("train_users_2.csv")*/


  }




}
