package Lab4 

import com.cra.figaro.library.compound._
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.factored._

object Ex2 {
	def main(args: Array[String]) {
		
	val sunnyToday = Flip(0.2)
    val greetingToday = If(sunnyToday,
         Select(0.6 -> "Hello, world!", 0.4 -> "Howdy, universe!"),
         Select(0.2 -> "Hello, world!", 0.8 -> "Oh no, not again"))

	greetingToday.observe("Oh no, not again")
    println(VariableElimination.probability(sunnyToday, true))
    println(VariableElimination.probability(sunnyToday, false))

	}
}