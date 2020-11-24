package Partial

import com.cra.figaro.language.{Select, Constant, Flip, Chain}	
import com.cra.figaro.library.compound.{CPD}
import com.cra.figaro.algorithm.factored.VariableElimination

object Ex1 {
    // fig. 5.7, 5.8
    val material = Select(0.7 -> 'oil, 0.3 -> 'acrylic)
    //Rata de intarziere a autobuzului este 20%
    val intarziere_buss=Select(0.2 -> 'intarziat_autobuz, 0.8 -> 'venit_autobuz)
    //Rata in care se trezeste prea tarziu din cauza ca inchide ceasul e 10%
    val trezit_tarziu=Select(0.1 -> 'adormit, 0.9 -> 'neadormit)
    //Rata ca uita alarma este 10%
    val uitat_alarma=Flip(0.1)

      //probabilitatile din tabel
    val valori_tabel = CPD(trezit_tarziu, intarziere_buss,
                        //Pentru cazurile specificate, completam cu probabilitatile din tabel
                         ('adormit, 'intarziat_autobuz)   -> Select(0.1 -> 'neintarziat, 0.9 -> 'intarziat),
                         ('adormit, 'venit_autobuz)   -> Select(0.3 -> 'neintarziat, 0.7 -> 'intarziat),
                         ('neadormit, 'intarziat_autobuz)   -> Select(0.2 -> 'neintarziat, 0.8 -> 'intarziat),
                         ('neadormit, 'venit_autobuz)   -> Select(0.9 -> 'neintarziat, 0.1 -> 'intarziat)
                        )
    def main(args: Array[String]) {	
           
           //luam in considerare ca a dormit si ca a uitat alarma
          valori_tabel.observe('adormit)
          uitat_alarma.observe()
        println("Probabilitatea de a ajunge la timp in caz de ai adormit: " + 
              VariableElimination.probability(valori_tabel, 'neintarziat))
            valori_tabel.unobserve()

            //luam in considerare ca a incarcat alarma
            valori_tabel.observe()
        println("Probabilitatea de a fi setata alarma si sa fi ajuns la timp la serviciu: " + 
              VariableElimination.probability(valori_tabel, 'neintarziat))
              
            //luam in calcul ca a adormit
              valori_tabel.observe('adormit)
        println("Probabilitatea de a te trezi prea tarziu:" + 
              VariableElimination.probability(valori_tabel, 'intarziat))
              valori_tabel.unobserve()
      //Ex3 Ar putea fi schimbata din variabila globalauitata_alarma
    }
}
