package Partial1

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.language.{Select, Apply, Constant, Element, Chain, Universe, Flip}
import com.cra.figaro.library.compound.{If, CPD, RichCPD, OneOf, *, ^^}
import com.cra.figaro.algorithm.factored.VariableElimination
import com.cra.figaro.algorithm.sampling.Importance
import com.cra.figaro.algorithm.factored.beliefpropagation.BeliefPropagation

object Ex1
{
	def main(args: Array[String])
	{
		val chapters = 10
		val monitorizare = 5
		val vreme: Array[Element[Symbol]] = Array.fill(chapters)(Constant('Insorit))
		val umbrela: Array[Element[Symbol]] = Array.fill(chapters)(Constant('FaraUmbrela))

		//vom face un model markov in care fiecare chapter va reprezenta probabilitatea
		//vremii (insorit , innorat, ploios) timp de 10 chaptere , stocanduse in variabila vreme
		vreme(0) = Select(0.5 -> 'Insorit, 0.3 -> 'Innorat, 0.2 -> 'Ploios)
		for { chapter <- 1 until chapters }
		{
			vreme(chapter) = CPD(vreme(chapter - 1),
				'Insorit -> Select(0.6 -> 'Insorit, 0.3 -> 'Innorat, 0.1 -> 'Ploios),
				'Innorat -> Select(0.15 -> 'Insorit, 0.5 -> 'Innorat, 0.35 -> 'Ploios),
				'Ploios -> Select(0.15 -> 'Insorit, 0.4 -> 'Innorat, 0.45 -> 'Ploios))
		}

		//cu probabilitatile vremii fiind stiute , vom calcula probabilitatea de a lua umbrela
		//stocand in variabila umbrela rezultatele pentru fiecare chapter
		for { chapter <- 0 until chapters }
		{
			umbrela(chapter) = CPD(vreme(chapter),
				'Insorit -> Select(0.15 -> 'CuUmbrela, 0.85 -> 'FaraUmbrela),
				'Innorat -> Select(0.65 -> 'CuUmbrela, 0.35 -> 'FaraUmbrela),
				'Ploios -> Select(0.75 -> 'CuUmbrela, 0.25 -> 'FaraUmbrela))
		}

		//monitorizam pe o distanta de 5 iteratii probabilitatea vremii(insorit, innorat si ploios)
		//afisand fiecare probabilitate pentru fiecare ora si probabilitatea pentru luarea umbrelei
		for { chapter <- 0 until monitorizare }
		{
				println("Ora " + chapter +" , probabilitatea vremii :\n ")
			println("Probabilitatea de a fi Insorit:" +
                VariableElimination.probability(vreme(chapter), 'Insorit) + "\n")
			println("Probabilitatea de a fi Innorat:" +
                VariableElimination.probability(vreme(chapter), 'Innorat) + "\n")
			println("Probabilitatea de a fi Ploios:" +
                VariableElimination.probability(vreme(chapter), 'Ploios) + "\n")
			println(" Aveti probabilitatea de " + 
			VariableElimination.probability(umbrela(chapter), 'CuUmbrela) + "de a va lua umbrela\n")
		}

		//punem sub observatia ora 4 si 5 (incepem ca prima ora , ora 1 ca fiind iteratia 0)
		//<de aceea voi observa 3 pentru 4 si 4 pentru 5>
		// si voi verifica probabilitatea pentru ca in ora 6 (iteratia 5) pentru variabila umbrelei
		vreme(3).observe('Innorat)
		vreme(4).observe('Innorat)
		println(VariableElimination.probability(umbrela(5), 'CuUmbrela))
		vreme(4).unobserve()
		vreme(3).unobserve()

		//punem sub observare iteratia 5 in variabila umbrela ca fiind 'FaraUmbrela si verificam
		//probabilitatea de a fi ploios in iteratia 2
		umbrela(4).observe('FaraUmbrela)
		println(VariableElimination.probability(vreme(1), 'Ploios))
		umbrela(4).unobserve()
	}
}