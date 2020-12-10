package Lab10

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
	val length = 10;
	val initial1 = Universe.createNew()
	val initial2 = Universe.createNew()
	val initial3 = Universe.createNew()
	Constant(450)("capital", initial1)
	Constant(100)("investment", initial1)
	Constant(70)("profit", initial1)

	Constant(450)("capital", initial2)
	Constant(100)("investment", initial2)
	Constant(70)("profit", initial2)

	Constant(450)("capital", initial3)
	Constant(100)("investment", initial3)
	Constant(70)("profit", initial3)


	def transition1(capital: Double, profit: Double, investment: Double): (Element[(Double, Double, Double)]) = {

		val newInvestment = Apply(Flip(0.6), Constant(capital), (myBool : Boolean, c:Double) 
		=> if(myBool) 0.7 * c else  0.5 * c)

		val newProfit = Apply(Constant(investment), newInvestment, (oldInvestment : Double, newInvestment: Double) 
		=> if(oldInvestment < newInvestment) profit *1.5 
			else profit *0.7 )

		val newCapital = for {
		myProfit <- newProfit 
		myInvestment <- newInvestment } yield capital + myProfit - myInvestment
		
		^^(newCapital, newProfit, newInvestment)
	}



	def transition2(capital: Double, profit: Double, investment: Double): (Element[(Double, Double, Double)]) = {

		val newInvestment = Apply(Flip(0.6), Constant(capital), (myBool : Boolean, c:Double) 
		=> if(myBool) 0.5 * c else  0.3 * c)

		val newProfit = Apply(Constant(investment), newInvestment, (oldInvestment : Double, newInvestment: Double)
		=> if(oldInvestment < newInvestment) profit *1.5 
			else profit *0.7 )

		val newCapital = for {
		myProfit <- newProfit 
		myInvestment <- newInvestment } yield capital + myProfit - myInvestment

		^^(newCapital, newProfit, newInvestment)
	}



	def transition3(capital: Double, profit: Double, investment: Double): (Element[(Double, Double, Double)]) = {

		val newInvestment = Apply(Flip(0.6), Constant(capital), (myBool : Boolean, c:Double) 
		=> if(myBool) 0.3 * c else  0.2 * c)

		val newProfit = Apply(Constant(investment), newInvestment, (oldInvestment : Double, newInvestment: Double) 
		=> if(oldInvestment < newInvestment) profit *1.5 
			else profit *0.7 )

		val newCapital = for {
		myProfit <- newProfit 
		myInvestment <- newInvestment } yield capital + myProfit - myInvestment

		^^(newCapital, newProfit, newInvestment)
	}



	def nextUniverse1(previous: Universe): Universe = {
		val next = Universe.createNew()
		val previousInvestment = previous.get[Double]("investment")
		val previousCapital = previous.get[Double]("capital")
		val previousProfit = previous.get[Double]("profit")
		val newState = Chain(^^(previousCapital, previousProfit, previousInvestment), (transition1 _).tupled)
		Apply(newState, (s: (Double, Double, Double)) => s._1)("investment", next)
		Apply(newState, (s: (Double, Double, Double)) => s._2)("profit", next)
		Apply(newState, (s: (Double, Double, Double)) => s._3)("capital", next)
		next
	}



	def nextUniverse2(previous: Universe): Universe = {
		val next = Universe.createNew()
		val previousInvestment = previous.get[Double]("investment")
		val previousCapital = previous.get[Double]("capital")
		val previousProfit = previous.get[Double]("profit")
		val newState = Chain(^^(previousCapital, previousProfit, previousInvestment), (transition2 _).tupled)
		Apply(newState, (s: (Double, Double, Double)) => s._1)("investment", next)
		Apply(newState, (s: (Double, Double, Double)) => s._2)("profit", next)
		Apply(newState, (s: (Double, Double, Double)) => s._3)("capital", next)
		next
	}



	def nextUniverse3(previous: Universe): Universe = {
		val next = Universe.createNew()
		val previousInvestment = previous.get[Double]("investment")
		val previousCapital = previous.get[Double]("capital")
		val previousProfit = previous.get[Double]("profit")
		val newState = Chain(^^(previousCapital, previousProfit, previousInvestment), (transition3 _).tupled)
		Apply(newState, (s: (Double, Double, Double)) => s._1)("investment", next)
		Apply(newState, (s: (Double, Double, Double)) => s._2)("profit", next)
		Apply(newState, (s: (Double, Double, Double)) => s._3)("capital", next)
		next
	}



	def main(args: Array[String]) {
		val arrivingObservation = List(Some(1),Some(1),Some(1),None,None,None,None,None,None,None,None,None)
		val alg1 = ParticleFilter(initial1, nextUniverse1,100)
		val alg2 = ParticleFilter(initial2, nextUniverse2,100)
		val alg3 = ParticleFilter(initial3, nextUniverse3,100)

		alg1.start()
		alg2.start()
		alg3.start()
		for{time<- 1 until 10}{
			val evidence = {
				arrivingObservation(time) match {
				case None => List()
				case Some(n) => List(NamedEvidence("capital", Observation(n)))
				}
			}
			alg1.advanceTime(evidence)
			alg2.advanceTime(evidence)
			alg3.advanceTime(evidence)
			println("This time is : " + time )
			print("Universe 1 : Capital " + time + ":")
			println("expected learning = "+ alg1.currentExpectation("capital", (i:Int)=>i))

			print("Universe 2 : Capital " + time + ":")
			println("expected learning = "+ alg2.currentExpectation("capital", (i:Int)=>i))

			print("Universe 3 : Capital " + time + ":")
			println("expected learning = "+ alg3.currentExpectation("capital", (i:Int)=>i))

			println("------")
		}
	}
}
