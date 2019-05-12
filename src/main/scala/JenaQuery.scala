
import java.util

import org.apache.jena.query.QueryFactory
import org.apache.jena.sparql.algebra._
import org.apache.jena.sparql.algebra.walker.Walker



class JenaQuery {
    //////////////////////////////////////// Création du tableau des queries ///////////////////////////////////////////////////////////
    val queriesTab : util.List[String] = new util.ArrayList[String]()
    queriesTab.add("SELECT ?x WHERE { \n\t?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#GraduateStudent> . \n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#takesCourse> <http://www.Department0.University0.edu/GraduateCourse0> \n}")
    queriesTab.add("PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\nPREFIX ub:<http://www.univ-mlv.fr/~ocure/lubm.owl#>\nPREFIX owl:<http://www.w3.org/2002/07/owl#>\nPREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nSELECT ?x ?y ?z WHERE { \n\t?x rdf:type ub:GraduateStudent . \n\t?y rdf:type ub:University . \n\t?z rdf:type ub:Department . \n\t?x ub:memberOf ?z . \n\t?z ub:subOrganizationOf ?y . \n\t?x ub:undergraduateDegreeFrom ?y.\n}")
    //queriesTab.add("SELECT ?x ?y ?z WHERE { \n\t?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#GraduateStudent> . \n\t?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#University> . \n\t?z <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#Department> . \n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#memberOf> ?z . \n\t?z <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#subOrganizationOf> ?y . \n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#undergraduateDegreeFrom> ?y}")

  ////////////// y1 y2 y3 n'existent  pas dans le dataframe et on n'a pas de jointure dans la requête ///////////////
  ///////////// on ne sait pas comment gérer//////
   //queriesTab.add("SELECT ?x ?y1 ?y2 ?y3 WHERE {\n\t?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#Professor> . \n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#worksFor> <http://www.Department0.University0.edu> . \n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#name> ?y1 . \n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#emailAddress> ?y2 .\n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#telephone> ?y3\n}")
   //queriesTab.add("SELECT ?x WHERE { \n\t?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#Person> . \n\t?x <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#memberOf> <http://www.Department0.University0.edu> \n}")
   //queriesTab.add("SELECT ?x WHERE { \n\t?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#Student> \n}")
    /////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////// Itération sur les queries ////////////////////////////////////////
   for(i <- 0 until queriesTab.size()) {
     // Récuperation du query à la position i
      val queryString = queriesTab.get(i)
      val query = QueryFactory.create((queryString))
     // Création de l'algèbre du query
      val opTest = Algebra.compile(query)
      println("********************* Algèbre relationnel **********************")
      println("opTest : " + opTest)
      val visitor = new Visitor()
      // Parcours de l'algèbre avec notre visitor
      Walker.walk(opTest, visitor)

      println("***************** Dataframe " + i  + " *******************")
      visitor.dataframe.show(10)
      println("**************** Query String " + i +  " ********************")
      println(visitor.queryString)
      println()
    }
    ////////////////////////////////////////////////////////////////////////

}
