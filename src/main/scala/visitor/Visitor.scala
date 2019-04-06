package visitor

import org.apache.jena.sparql.algebra._
import org.apache.jena.sparql.algebra.op._

class Visitor extends OpVisitor{

  override def visit(opTable: OpTable): Unit = ???

  override def visit(opNull: OpNull): Unit = ???

  override def visit(opProc: OpProcedure): Unit = ???

  override def visit(opTop: OpTopN): Unit = ???

  override def visit(opGroup: OpGroup): Unit = ???

  override def visit(opSlice: OpSlice): Unit = ???

  override def visit(opDistinct: OpDistinct): Unit = ???

  override def visit(opReduced: OpReduced): Unit = ???

  override def visit(opProject: OpProject): Unit = {
    println("visitor project")
    println("var = " + opProject.getVars.get(0))
  }

  override def visit(opPropFunc: OpPropFunc): Unit = ???

  override def visit(opFilter: OpFilter): Unit = {
    println("visitor filter")
    println("var1 = " + opFilter.getExprs.getList.get(0))
  }

  override def visit(opGraph: OpGraph): Unit = ???

  override def visit(opService: OpService): Unit = ???

  override def visit(opPath: OpPath): Unit = ???

  override def visit(opQuad: OpQuad): Unit = ???

  override def visit(opTriple: OpTriple): Unit = {
    println("visitor triple")
  }

  override def visit(quadBlock: OpQuadBlock): Unit = ???

  override def visit(quadPattern: OpQuadPattern): Unit = ???

  override def visit(opBGP: OpBGP): Unit = {
    println("visitor bgp")
    println("var = " + opBGP.getPattern.getList)
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
