package Lab9

import com.cra.figaro.library.atomic.{discrete,continuous}
import com.cra.figaro.library.atomic.continuous.Normal
import com.cra.figaro.library.atomic.discrete{FromRange.Poisson}
import com.cra.figaro.library.compound{If,^^,RichCPD,Oneof,*}
import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.algorithm.filtering.ParticleFilter
import com.cra.figaro.library.collection._
import com.cra.figaro.algorithm.factored.VariableElimination

object Ex1 {
	val length = 10;
	val initial = Universe.createNew()

	Constant(3)("score",initial)
	Constant(8)("learned",initial)
	Constant(5)("difficulty",initial)

	def transition(learned: Boolean, difficulty: Double): Element[(Double,Boolean,Boolean)] = {
		val greutate_test=difficulty-0.05
		val invatatDestul=if(learned,Flip(0.65),Flip(0.35))
		val scoreSuficient=if(invatatDestul,Flip(0.8*greutate_test),Flip(0.3*greutate_test))
		^^(greutate_test,invatatDestul,scoreSuficient)
	}

	def nextUnivers(previous: Universe): Universe={
		val next= Universe.createNew()

		val previousGreutate=prevoius.get[Float]("difficulty")
		val previousInvatat=previous.get[Boolean]("learned")
		
		val state= Chain(previousInvatat,previousGreutate, transition _)

		Apply( state, (s: (Double,Boolean,Boolean)) => s._1)("difficulty",next)
		Apply( state, (s: (Double,Boolean,Boolean)) => s._2)("learned",next)
		Apply( state, (s: (Double,Boolean,Boolean)) => s._3)("score",next)

		next
	}
	def main(args: Array[String]) {
		val arrivingOvservation = List(Some(1),Some(1),Some(1),None,None,None,None,None,None,None,None,None)
		val alg = ParticleFilter(initial, nextUnivers,100)
		alg.start()
		for{i<- 1 until 10}{
			val evidence = {
				arrivingObservation(time) match {
				case None => List()
				case Some(n) => List(NamedEvidence("arriving", Observation(n)))
				}
			}
			alg.advanceTime(evidence)
			print("Chapte " + time + ":")
			println("expected learning = "+ alg.currentExpectation("score", (i:Int)=>i))
		}
	}
}