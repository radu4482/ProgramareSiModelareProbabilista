package Lab6

import com.cra.figaro.language.Apply
import com.cra.figaro.language.Flip
import com.cra.figaro.language.Element
import com.cra.figaro.language.Constant
import com.cra.figaro.algorithm.sampling._

object Ex1 {
	def main(args: Array[String]) {
		val test = Constant("Test")

		val algorithm = Importance(1000, test)
		algorithm.start()
		
		println(algorithm.probability(test, "Test"))
	}


def tennis( probP1ServeWin: Double, probP1Winner: Double, probP1Error: Double, probP2ServeWin: Double, probP2Winner: Double, probP2Error: Double):Element[Boolean] ={

def rally(firstShot: Boolean, player1: Boolean): Element[Boolean] = {
	//Se verifica daca s-a castigat din serva(lovitura de inceput) si daca playerul1 a castigat
	//depinzand de caz , pWinner va fi primi probabilitatea corespunzatoare evenimentului

	val pWinner = if (firstShot && player1) probP1ServeWin
					else if (firstShot && !player1) probP2ServeWin
					else if (player1) probP1Winner
					else probP2Winner

	val pError = if (player1) probP1Error else probP2Error
	val winner = Flip(pWinner)
	val error = Flip(pError)

	If(winner, Constant(player1),
			   If(error,Constant(!player1),
			   			rally(false, !player1)))
 }


 def game(p1Serves: Boolean, p1Points: Element[Int], p2Points: Element[Int]): Element[Boolean] = {
	
	val p1WinsPoint = rally(true, p1Serves)
	
	//Aici putem observa sistemul de joc al unui Game
	//wins ia valoarile pentru: p1->1, p2->0 .

	//In caz de marcheaza unul din cei doi jucatori, se vor adauga in variabila de 
	//scor ("newP'i'Points", 'i'={1sau2}) un punct
	
	//In cazul in care scorul celor 2 ajunge de forma: (5 vs n)unde n<4 sau (4 vs n) n<3
	//cel cu scorul mai mare castiga 
	val newP1Points =
	Apply(p1WinsPoint, p1Points, 
	(wins: Boolean, points: Int) =>
	if (wins) points + 1 else points)

	val newP2Points =
	Apply(p1WinsPoint, p2Points, 
	(wins: Boolean, points: Int) =>
	if (wins) points else points + 1)

	val p1WinsGame =
	Apply(newP1Points, newP2Points, 
	(p1: Int, p2: Int) =>
	p1 >= 4 && p1 - p2 >= 2)

	val p2WinsGame =
	Apply(newP2Points, newP1Points, 
	(p2: Int, p1: Int) =>
	p2 >= 4 && p2 - p1 >= 2)

	val gameOver = p1WinsGame || p2WinsGame
	If(gameOver, p1WinsGame, game(p1Serves, newP1Points, newP2Points))
 }

def play(p1Serves: Boolean, p1Sets: Element[Int], p2Sets: Element[Int], p1Games: Element[Int], p2Games: Element[Int]): 
Element[Boolean] = {
	val p1WinsGame = game(p1Serves, Constant(0), Constant(0))
	
	
	//wins ia valoarile pentru: p1->1, p2->0 .
	
	//In caz de castiga unul dintre cei doi un Game se va adauga un punct in variabila respectivului.
	
	//In caz de unul din cei doi jucatori strange 5 Gameuri , variabilele Games crespunzatoare celor doi
	//se vor reseta si se va adauga un punct in variabila Sets corespunzatoare jucatorului.

	//Daca unul din cei doi va strange 2 Seturi in variabila sa , matchOver va fi 1, in caz contrar 0.
	val newP1Games =
	Apply(p1WinsGame, p1Games, p2Games,
	(wins: Boolean, p1: Int, p2: Int) =>
	if (wins) {if (p1 >= 5) 0 else p1 + 1} 
	else {if (p2 >= 5) 0 else p1 })


	val newP2Games = 
	Apply(p1WinsGame, p1Games, p2Games,
	(wins: Boolean, p1: Int, p2: Int) =>
	if (wins) {if (p1 >= 5) 0 else p2} 
	else {if (p2 >= 5) 0 else p2 + 1})

	val newP1Sets = 
	Apply(p1WinsGame, p1Games, p1Sets,
	(wins: Boolean, games: Int, sets: Int) => 
	if (wins && games == 5) sets + 1 else sets)

	val newP2Sets = 
	Apply(p1WinsGame, p2Games, p2Sets,
	(wins: Boolean, games: Int, sets: Int) =>
	if (!wins && games == 5) sets + 1 else sets)

	val matchOver = Apply(newP1Sets, newP2Sets, 
	(p1: Int, p2: Int) => p1 >= 2 || p2 >= 2)
	


	If(matchOver, Apply(newP1Sets, (sets: Int) => sets >= 2),
	              play(!p1Serves, newP1Sets, newP2Sets, newP1Games, newP2Games))
	}

	play(true, Constant(0), Constant(0), Constant(0), Constant(0))
 }
}