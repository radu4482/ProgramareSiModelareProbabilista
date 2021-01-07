package Lab12

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.compound.{If}
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.library.atomic.continuous.Normal
import scalax.chart.api._
object Ex4 {
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

		for { i <- 10000 to 1000000 by 10000 } {
			var totalSquaredError = 0.0
			for { j <- 1 to 100 } {
				Universe.createNew()
				val x0 = Apply(Normal(0.75, 0.3), (d: Double) => d.max(0).min(1))
        		val y0 = Apply(Normal(0.4, 0.2), (d: Double) => d.max(0).min(1))
        		val x = Flip(x0)
        		val y = Flip(y0)
       	 		val z = If(x === y, Flip(0.8), Flip(0.2))
				z.observe(false)
				val mh = MetropolisHastings(i, ProposalScheme.default, y)
				mh.start()
				val mhAnswer = mh.probability(y, true)
				val diff = veAnswer - mhAnswer
				totalSquaredError += diff * diff
				}
			val rmse = math.sqrt(totalSquaredError / 100)
			println(i + " samples: RMSE = " + rmse)
			series.add(i/10000,rmse)
			}
	}
}
/*Problema este ca, avand probabilitatile extreme, este foarte greu pentru
 Metropolis-Hastings sa treaca intre state.

In special, starea in care x este adevarat, y este fals, prima consecinta
a If a z este adevarata, iar a doua consecinta este falsa, este cel mai 
probabil dat dovada.

Cu toate acestea, din aceasta stare, orice propunere care propune modificarea
x sau y va fi respinsa, deoarece aceasta va face x egal cu y, caz in care
se va aplica prima consecinta, ceea ce este incompatibil cu dovezile.

In mod similar, schimbarea valorii celui de-al doilea rezultat va avea ca 
rezultat o stare incompatibila cu dovezile.

Singura schimbare posibila care va fi acceptata din aceasta stare este
schimbarea primei consecinte In fals.

Acest lucru se va intampla foarte rar si, mai mult, chiar si atunci cand se va Intampla, ar putea fi inversat rapid.

 Ca urmare, Metropolis-Hastings va converge foarte incet catre distributia corecta*/