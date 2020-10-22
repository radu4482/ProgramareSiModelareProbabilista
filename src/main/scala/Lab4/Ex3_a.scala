// package Lab4 

// import com.cra.figaro.library.compound._
// import com.cra.figaro.language._
// import com.cra.figaro.algorithm.sampling._
// import com.cra.figaro.algorithm.factored._


// //in primul caz (x,y,z,v) z=x => x===z => v=1.0

// //in cazul al doilea (a,b,c,w) c=b , insa nu este o sansa sigura ca a===c
// //ceea ce inseamna ca pot fi 2 cazuri
// // I: a==true,b==true => o probabilitate de 0.4*0.4=0.16
// // II: a==false,b==false => o probabilitate de 0.6*0.6=0.36
// // raportul final va fi 0.16+0.36= 0.52 
// object Ex3_a {
// 	def main(args: Array[String]) {
		
//     val x = Flip(0.4)
//     val y = Flip(0.4)
//     val z = x
//     val v = x === z
//     println(VariableElimination.probability(v, true))

//     val a = Flip(0.4)
//     val b = Flip(0.4)
//     val c = b
//     val w = a === c
//     println(VariableElimination.probability(w, true))

// 	}
// }