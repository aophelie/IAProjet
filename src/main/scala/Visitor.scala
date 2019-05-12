import java.util
import org.apache.jena.sparql.algebra._
import org.apache.jena.sparql.algebra.op._
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import scala.io.Source


class Visitor extends OpVisitor{

  /////////////////////////////// Creation de dataframe //////////////////////////////////
  System.setProperty("hadoop.home.dir", "winutils")
  System.setProperty("spark.sql.warehouse.dir", "file:///spark-warehouse")
  System.setProperty("spark.eventLog.enabled", "false")

  val sparkSession = SparkSession
    .builder()
    .master("local")
    .appName("Spark SQL basic example")
    .config("spark.some.config.option", "some-value")
    .getOrCreate

  val path = "resources/dataDecode.txt"

  //Lectures des données et mise en forme de chaque ligne

 val data = Source.fromFile(path).getLines().map(x => x.split(" ")).map{ case x => (x(0), x(1), x(2))}

    ///////////////////// Création du dataframe ////////////

  val schemaTriple =  StructType(
      StructField("x", StringType, false)::
      StructField("y", StringType, false)::
      StructField("z", StringType, false)::
      Nil)

  val datardd = data.map{ case (x, y, z) => Row(x,y,z) }
  val rowData : util.List[Row] = new util.ArrayList[Row]()
  for(x <- datardd) {
    rowData.add(x)
  }

  var dataframe = sparkSession.createDataFrame(rowData, schemaTriple)

  println("Dataframe create")

  dataframe.printSchema()
  //println("*********** Origninal **************")
 //dataframe.show()

  println("**************** TEST Expression WHERE ***************")
  println("******* Avec z = <http://www.Department6.University0.edu/FullProfessor2/Publication16> ******")
  val express = "z == \"<http://www.Department6.University0.edu/FullProfessor2/Publication16>\""
  println("**************** Résultat TEST Expression WHERE ***************")
  dataframe.where(express).show()


  ///////////////////////////// Query sous forme de string  /////////////////////////////////////////////////
  var queryString = "dataframe"
  ////////////////////////////////////////////////////////////////////////////////////////////



  override def visit(opTable: OpTable): Unit = ???

  override def visit(opNull: OpNull): Unit = ???

  override def visit(opProc: OpProcedure): Unit = ???

  override def visit(opTop: OpTopN): Unit = ???

  override def visit(opGroup: OpGroup): Unit = ???

  override def visit(opSlice: OpSlice): Unit = ???

  override def visit(opDistinct: OpDistinct): Unit = ???

  override def visit(opReduced: OpReduced): Unit = ???

  // Correspond au Select
  override def visit(opProject: OpProject): Unit = {
    println("**************** Opération SELECT ********************")
    // Construction du query
    queryString += ".select("
    // Expression
    var expression = ""
    // tableaux des colonnes
    val cols : util.List[String] = new util.ArrayList[String]()

    // Création du tableau des paramètres
    for (i <- 0 until opProject.getVars.size() ) {
      // l'objet sur lequel on itère
      val obj = opProject.getVars.get(i)
      // recupération du nom de la variable
      val x = opProject.getVars.get(i).getVarName;
     // ajout de la variable dans le tableau
     cols.add(x)

      ////////////// expression //////////////
      if(expression != "") expression += ","
      expression += "\"" + x  + "\""

    }

    // Constructon de l'expression en string et operation sur le dataframe
     if(cols.size() == 1) {
      dataframe = dataframe.select(cols.get(0))
    }
    else if(cols.size() == 2) {
      dataframe = dataframe.select(cols.get(0), cols.get(1))
     }
    else if(cols.size() == 3) {
      dataframe = dataframe.select(cols.get(0), cols.get(1), cols.get(2))

    }
    queryString += expression +  ")"
    dataframe.explain()
    println()
  }

  override def visit(opPropFunc: OpPropFunc): Unit = ???

  override def visit(opFilter: OpFilter): Unit = {
    println("**************** Opération FILTER ********************")
    println("visitor filter")
    for (i <- 0 until opFilter.getExprs.getList.size()) {
      // l'objet surlequel on itère
      val obj =  opFilter.getExprs.getList.get(i)
      // construction du query
      queryString += ".filter("
      //L'operation du filtre
      val op = obj.getFunction.getOpName
      // expression a gauche
      val leftOp = obj.getFunction.getArg(1).getVarName
      // expression à droite
      val rigthtOp = obj.getFunction.getArg(2)
      // expression finale
      val expression =  leftOp  + " " + op +  " \"<" + rigthtOp + " \">"
      // suite construction du query
      queryString += expression
      queryString +=  ")"
      // operation sur le dataframe
      dataframe = dataframe.filter(expression) //la syntaxe est bonne mais il ne retourne rien
      dataframe.filter(expression).explain()
    }

    println()
  }

  override def visit(opGraph: OpGraph): Unit = ???

  override def visit(opService: OpService): Unit = ???

  override def visit(opPath: OpPath): Unit = ???

  override def visit(opQuad: OpQuad): Unit = ???

  override def visit(opTriple: OpTriple): Unit = {
  }

  override def visit(quadBlock: OpQuadBlock): Unit = ???

  override def visit(quadPattern: OpQuadPattern): Unit = ???

  override def visit(opBGP: OpBGP): Unit = {
    println("**************** BGP --- Opération WHERE ********************")
    println("visitor bgp")
    println("var = " + opBGP.getPattern.getList)
    for (i <- 0 until opBGP.getPattern.getList.size()) {
      // l'objet sur lequel on itère
      val obj = opBGP.getPattern.get(i)
      // expression finale
      var expression = ""
      // construction du query
      queryString += ".where("

      // subject
      if(obj.getSubject.isConcrete) {
        val x = obj.getSubject
        //println("************Expression XXXXXXXXXX **************"+x)
        if(expression != "") expression += " AND "
         expression += "x ="+"\"<"+ x+">\""
        //expression += "x == \" + \"\\\"\"+ x + \"\\\"\"
      }
      // predicat
      if(obj.getPredicate.isConcrete) {
        val y = obj.getPredicate
        //println("************Expression XXXXXXXXXX **************"+y)
        if(expression != "") expression += " AND "
        expression += "y = "+"\"<"+y+">\""
        //expression += "y == "+ y
      }
      // object
      if(obj.getObject.isConcrete) {
        val z =obj.getObject

        if(expression != "") expression += " AND "
        expression += "z ="+"\"<"+z+">\""
        //expression += "z == "+ z
      }

      println("************Expression**************"+expression)


      // suite construction du query
      queryString += expression
      queryString +=  ")"
      println("Expression "+expression)
      dataframe = dataframe.where(expression)
      //dataframe2.show()

    }
    //dataframe.explain()

    dataframe.show()
    println()
  }

  override def visit(dsNames: OpDatasetNames): Unit = ???

  override def visit(opLabel: OpLabel): Unit = ???

  override def visit(opAssign: OpAssign): Unit = ???

  override def visit(opExtend: OpExtend): Unit = ???

  override def visit(opJoin: OpJoin): Unit = {
    println("visitor join")
  }

  override def visit(opLeftJoin: OpLeftJoin): Unit = {
    println("visitor LeftJoin")
  }

  override def visit(opUnion: OpUnion): Unit = {
    {
      println("visitor join")
    }
  }

  override def visit(opDiff: OpDiff): Unit = ???

  override def visit(opMinus: OpMinus): Unit = ???

  override def visit(opCondition: OpConditional): Unit = ???

  override def visit(opSequence: OpSequence): Unit = ???

  override def visit(opDisjunction: OpDisjunction): Unit = ???

  override def visit(opList: OpList): Unit = ???

  override def visit(opOrder: OpOrder): Unit = ???
}
