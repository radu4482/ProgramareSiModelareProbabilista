package Lab12

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.compound.{If}
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.library.atomic.continuous.Normal
import scalax.chart.api._
object Ex5 {
	def main(args: Array[String]) {
        val x0 = Apply(Normal(0.75, 0.3), (d: Double) => d.max(0).min(1))
        val y0 = Apply(Normal(0.4, 0.2), (d: Double) => d.max(0).min(1))
        val x = Flip(x0)
        val y = Flip(y0)
        val z = If(x === y, Flip(0.8), Flip(0.2))
		z.observe(false)
		val veAnswer = VariableElimination.probability(y, true)

		val series = new XYSeries("Importance sampling")		
		val chart = XYLineChart(series)
		chart.show()

		for { i <- 10000 to 100000 by 10000 } {
			var totalSquaredError = 0.0
			for { j <- 1 to 100 } {
				Universe.createNew()
				val x0 = Apply(Normal(0.75, 0.3), (d: Double) => d.max(0).min(1))
        		val y0 = Apply(Normal(0.4, 0.2), (d: Double) => d.max(0).min(1))
        		val x = Flip(x0)
        		val y = Flip(y0)
       	 		val z = If(x === y, Flip(0.8), Flip(0.2))
				val z1= Flip(0.9999)
				val z2= Flip(0.0001)
				val scheme =DisjointScheme(
					0.1 -> (() => ProposalScheme(z1)),
					0.1 -> (() => ProposalScheme(z2)),	
					0.8 -> (() => ProposalScheme(x, y))
				)
				z.observe(false)
				val mh = MetropolisHastings(i, scheme,z1)
				mh.start()
				val mhAnswer = mh.probability(z1, true)
				val diff = veAnswer - mhAnswer
				totalSquaredError += diff * diff
				}
			val rmse = math.sqrt(totalSquaredError / 100)
			println(i + " samples: RMSE = " + rmse)
			series.add(i/10000,rmse)
			}
	}
}