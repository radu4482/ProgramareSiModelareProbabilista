// package Lab4 

// import com.cra.figaro.library.compound._
// import com.cra.figaro.language._
// import com.cra.figaro.algorithm.sampling._
// import com.cra.figaro.algorithm.factored._

// //limita logica pentru asta problema este de 11 zaruri
// object Ex3_a {
// 	def main(args: Array[String]) {

//         val die1 = FromRange(1, 7)
//         val die2 = FromRange(1, 7)
//         val total = Apply(die1, die2, (i1: Int, i2: Int) => i1 + i2)
//         println(VariableElimination.probability(total, 11))

//         val newDie1= FromRange(1,7)
//         val newDie2= FromRange(1,7)
//         val newDie3= FromRange(1,7)
//         val newTotal = Apply(newDie1,newDie2,newDie3,(i1:Int,i2:Int,i3:Int)=>i1+i2+i3)
//         println(VariableElimination.probability(newTotal, 11))
//     }
// }