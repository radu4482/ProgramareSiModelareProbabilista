package Lab4 

import com.cra.figaro.library.atomic.discrete
import com.cra.figaro.library.compound._
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.factored._

object Ex6{
    def main(args: Array[String]) {
       def doubles = {
            val die1 = FromRange(1, 7)
            val die2 = FromRange(1, 7)
            die1 === die2
        }
        
        val jail = doubles && doubles && doubles
        println(VariableElimination.probability(jail, true))
    }
}