package Lab9

import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._

object Ex2 {
	def main(args: Array[String]) {
		val length=10;

		val invatasiIndeajuns:Array[Element[Boolean]]=
			Array.fill(length)(Constant(false))
		val trecutTest:Array[Element[Int]]=
			Array.fill(length)(Constant(0))

		invatasiIndeajuns(0)=Flip(0.5)

		for{i<- 1 until length}{
			invatasiIndeajuns(i)=If(invatasiIndeajuns(i-1),Flip(0.65),Flip(0.35))
		}

		for{i<- 0 until length}{
			trecutTest(i)=If(invatasiIndeajuns(i),
            Select(0.1->1, 0.1->2 , 0.1->3 , 0.1->4 , 0.1->5 , 0.1->6, 0.1->7 , 0.1->8 , 0.1->9 , 0.1->10),
            Select(0.1->1, 0.1->2 , 0.1->3 , 0.1->4 , 0.1->5 , 0.1->6, 0.1->7 , 0.1->8 , 0.1->9 , 0.1->10))
		}
		
	}
}