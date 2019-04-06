
import org.apache.jena.query.{Query, QueryExecutionFactory, QueryFactory}
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.sparql.algebra.OpAsQuery.Converter
import org.apache.jena.sparql.algebra.op._
import org.apache.jena.sparql.algebra.walker.WalkerVisitor
import org.apache.jena.sparql.algebra._
import org.apache.jena.sparql.lang.SPARQLParser
import org.apache.spark
import org.apache.jena.sparql.algebra.walker.Walker
import visitor._
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

    //val model = ModelFactory.createDefaultModel()

    //val qr = QueryFactory.create(myQuery)
    //val query = QueryFactory.parse(new Query(), myQuery, prefix, )

    val query = QueryFactory.create(myQuery)
    // Transformation en algebra
    val op = Algebra.compile(query)
   // println("op : " + op)

    val query1 = QueryFactory.create(myQuery1)
    val op1 = Algebra.compile(query1)

    println("op1 : " + op1)

    //walkerVisitor

    // Parcours de ce dernier
    //op.visit(new Visitor)

    Walker.walk(op1,new Visitor)


    //println("op1 : " + op1)

    /*val rs = query.execSelect();

    while (rs.hasNext()) {
      print(rs.next())
    }*/
  }




}
