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

object Ex1 {
	def main(args: Array[String]) {

		val test = Constant("Test")
		val populationSUA = 40000000.0
		val populationSUA_absolventHarvard = 1/20000

		val presedinte_Dreptaci = 0.5

		val posibilitate_presedinte_Dreptaci= If( Flip (presedinte_Dreptaci) ,
		0.9/populationSUA , //  0.000000045 = 1/40000000 * 9/10 
		0.1/populationSUA  //  0.000000005 = 1/40000000 * 1/10
		) 


		val presedinte_absolventHarvard = Flip(0.15)
		val posibilitate_presedinte_absolventHarvard = If ( presedinte_absolventHarvard , 
		  populationSUA  / populationSUA_absolventHarvard ,
		  populationSUA  / (1.0 - populationSUA_absolventHarvard)
		)

		val posibilitate_presedinte_Dreptaci_absolverntHarvard = posibilitate_presedinte_Dreptaci * posibilitate_presedinte_absolventHarvard


		val algorithm = Importance(1000, test)
		algorithm.start()
		
 		println(posibilitate_presedinte_absolventHarvard)
	}
}