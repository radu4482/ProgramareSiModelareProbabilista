package Lab11

import com.cra.figaro.library.atomic.{discrete,continuous}
import com.cra.figaro.library.atomic.continuous.Normal
import com.cra.figaro.library.atomic.discrete.{FromRange,Poisson}
import com.cra.figaro.library.compound._
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.filtering.ParticleFilter
import com.cra.figaro.library.collection._
import com.cra.figaro.algorithm.factored.VariableElimination

object Ex2 {
	def main(args: Array[String]) {

	val populationSUA = 40000000.0
	val elem1 = Select(0.5 -> "dreptaci", 0.5 -> "stangaci")
    val elem2 = Chain(elem1, (x: String) => 
            if (x == "dreptaci") 0.9 *populationSUA
            else 0.1 * populationSUA
        )
    
    def main(args: Array[String]) {
        val algorithm1 = VariableElimination(elem1, elem2)
        algorithm1.start()
        println(algorithm1.probability(elem2, 10.0))
        println(algorithm1.probability(elem1, (x: Double) => x > 2.5))
        println(algorithm1.distribution(elem1).toList)
        println(algorithm1.mean(elem1))
        println(algorithm1.expectation(elem1, (x: Double) => x))
        elem1.observe(5)
        val algorithm2 = VariableElimination(elem2)
        algorithm2.start()
        println(algorithm2.distribution(elem2).toList)
        println(algorithm2.mean(elem2))
        println(algorithm2.expectation(elem2, (x: Double) => x))
        println(algorithm2.expectation(elem2, (x: Double) => x * x))
    }
}