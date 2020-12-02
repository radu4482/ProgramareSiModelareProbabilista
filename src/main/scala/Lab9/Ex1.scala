package Lab9

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._

object Ex1 {
	def main(args: Array[String]) {
		val length=10;

		val invatasiIndeajuns:Array[Element[Boolean]]=
			Array.fill(length)(Constant(false))
		val trecutTest:Array[Element[Boolean]]=
			Array.fill(length)(Constant(false))

		invatasiIndeajuns(0)=Flip(0.5)

		for{i<- 1 until length}{
			invatasiIndeajuns(i)=If(invatasiIndeajuns(i-1),Flip(0.65),Flip(0.35))
		}

		for{i<- 0 until length}{
			trecutTest(i)=If(invatasiIndeajuns(i),Flip(0.8),Flip(0.3))
		}
		trecutTest(0).observer(true)
		trecutTest(1).observer(true)
		trecutTest(2).observer(true)

		println("daca a trecut primele 3 teste , sansa sa il ia pe ultimul este de : "+ VariableElimination.probability(trecutTest(9),true))
	}
}