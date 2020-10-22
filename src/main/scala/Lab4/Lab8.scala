package Lab4 

import com.cra.figaro.library.compound._
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.factored._

object Ex7{
    def main(args: Array[String]) {
        val numberOfSides1 = Select(0.2 -> 4, 0.2 -> 6, 0.2 -> 8, 0.2 -> 12, 0.2 -> 20)
        val roll1 = Chain(numberOfSides1, ((i: Int) => FromRange(1, i + 1)))
        val numberOfSides2 = Select(0.2 -> 4, 0.2 -> 6, 0.2 -> 8, 0.2 -> 12, 0.2 -> 20)
        val roll2 = Chain(numberOfSides2, ((i: Int) => FromRange(1, i + 1)))
        def stickinessConstraint(sidesPair: (Int, Int)) =
        if (sidesPair._1 == sidesPair._2) 1.0 else 0.5
        val pairOfSides = ^^(numberOfSides1, numberOfSides2)
        pairOfSides.addConstraint(stickinessConstraint)
        println(VariableElimination.probability(roll2, 7))
        // prints 0.05166666666666667
        roll1.observe(7)
        println(VariableElimination.probability(roll2, 7))
        // prints 0.09704301075268815
    }
}