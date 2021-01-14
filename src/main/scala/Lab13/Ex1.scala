package Lab13

import com.cra.figaro.language.{Flip,Universe,Element}
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.compound._
import com.cra.figaro.algorithm.factored._
import com.cra.figaro.library.atomic.continuous.Normal
import com.cra.figaro.algorithm.factored.beliefpropagation._
import com.cra.figaro.algorithm._
import scalax.chart.api._

object Ex1 {
		val pixels = Array.fill(4, 4)(Flip(0.4))
		def makeConstraint(pixel1: Element[Boolean],
			pixel2: Element[Boolean]) {
			val pairElem = ^^(pixel1, pixel2)
			pairElem.setConstraint(pair => if (pair._1 == pair._2) 1.0 else 0.5)
			}

		abstract class State {
			val confident: Element[Boolean]
			def possession: Element[Boolean] =
			If(confident, Flip(0.7), Flip(0.3))
			}

		for {
			i <- 0 until 4
			j <- 0 until 4
			} {
			if (i > 0) makeConstraint(pixels(i-1)(j), pixels(i)(j))
			if (j > 0) makeConstraint(pixels(i)(j-1), pixels(i)(j))
		}


		class InitialState() extends State {
			val confident = Flip(0.4)
			}


		class NextState(current: State) extends State {
			val confident =
				If(current.confident, Flip(0.6), Flip(0.3))
			}


		//produce a state sequence in reverse order of the given length	
		def stateSequence(n: Int): List[State] = {
			if (n == 0) 
				List(new InitialState())
			else {
				val last :: rest = stateSequence(n - 1)
				new NextState(last) :: last :: rest
				}
			}

		def run(algorithm: OneTimeMPE) {
			algorithm.start()
				for { i <- 0 until 4 } {
					for { j <- 0 until 4 } {
						print(algorithm.mostLikelyValue(pixels(i)(j)))
						print("\t")
						}
					println()
					}
			println()
			algorithm.kill()
			}

		// unroll the hmm and measure the amount of time to infer the last hidden state
		def timing(obsSeq: List[Boolean]): Double = {
			Universe.createNew() // ensures that each experiment is separate
			val stateSeq = stateSequence(obsSeq.length)
			for { i <- 0 until obsSeq.length } {
				stateSeq(i).possession.observe(obsSeq(obsSeq.length - 1 - i))
				}
			val alg = VariableElimination(stateSeq(0).confident)
			val time0 = System.currentTimeMillis()
			alg.start()
			val time1 = System.currentTimeMillis()
			(time1 - time0) / 1000.0 // inference time in seconds
			}

		def main(args: Array[String]) {

		val steps = 1000
		val obsSeq = List.fill(steps)(scala.util.Random.nextBoolean())
		println(steps + ": " + timing(obsSeq))
		println("MPE variable elimination")
		run(MPEVariableElimination())
		println("MPE belief propagation")
		run(MPEBeliefPropagation(10))
		println("Simulated annealing")
		run(MetropolisHastingsAnnealer(100000, 
		ProposalScheme.default, Schedule.default(1.0)))
	}
}