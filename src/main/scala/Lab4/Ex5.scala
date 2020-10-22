// package Lab4 

// import com.cra.figaro.library.compound._
// import com.cra.figaro.language._
// import com.cra.figaro.algorithm.sampling._
// import com.cra.figaro.algorithm.factored._

// object Ex5{
//     def main(args: Array[String]) {
//         val die1 = FromRange(1, 7)
//         val die2 = FromRange(1, 7)
//         val total = Apply(die1, die2, (i1: Int, i2: Int) => i1 + i2)
//         total.addCondition((i: Int) => i > 8)
//         println(VariableElimination.probability(die1, 6))
//     }
// }