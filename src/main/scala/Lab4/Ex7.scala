package Lab4 

import com.cra.figaro.library.atomic.discrete.FromRange
import com.cra.figaro.library.compound._
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.factored._

object Ex7{
    def main(args: Array[String]) {
        val numberOfSides = Select(0.2 -> 4, 0.2 -> 6, 0.2 -> 8, 0.2 -> 12, 0.2 -> 20)
        val roll = Chain(numberOfSides, ((i: Int) => FromRange(1, i + 1)))
        println(VariableElimination.probability(numberOfSides,12)*VariableElimination.probability(numberOfSides,7))
        println(VariableElimination.probability(numberOfSides,7)*VariableElimination.probability(numberOfSides,12))
    }
}