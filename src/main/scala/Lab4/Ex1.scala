package Lab4 

import com.cra.figaro.library.compound._
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.factored._

object Ex1 {
	def main(args: Array[String]) {
		
	val side = Flip(0.65)
	val bedSide = If(side,
   	  Select(0.6 -> "Hello, world!", 0.4 -> "Hello, world!"),
   	  Select(0.2 -> "Oh no, not again", 0.8 -> "Oh no, not again"))

	println(VariableElimination.probability(bedSide, "Hello, world!"))
	
		
		//println(algorithm.probability(test, "Test"))
	}
}