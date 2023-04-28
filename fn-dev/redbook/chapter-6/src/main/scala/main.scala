trait RNG {
  def nextInt: (Int,RNG)
}

case class SimpleRNG( seed: Long ) extends RNG {
  override def nextInt: (Int, RNG) =
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.1
//
// Write a function that uses RNG.nextInt to generate a random integer between 0 and Int.maxValue (inclusive).
// Make sure to handle the corner case when nextInt returns Int.MinValue, which doesn’t have a non-negative counterpart.
//
// def nonNegativeInt(rng: RNG): (Int, RNG)

def nonNegativeInt(rng: RNG):(Int, RNG) =
  val (n,s) = rng.nextInt
  (Math.abs(n),s)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.2
//
// Write a function to generate a Double between 0 and 1, not including 1.
// Note: You can use Int.MaxValue to obtain the maximum positive integer value,
// and you can use x.toDouble to convert an x: Int to a Double.
//
// def double(rng: RNG): (Double, RNG)

def double(rng: RNG): (Double, RNG) =
  val (n,s) = nonNegativeInt(rng)
  if n==Int.MaxValue then double(s) else (n.toDouble / Int.MaxValue, s)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.3
//
// Write functions to generate an (Int, Double) pair, a (Double, Int) pair, and a (Double, Double, Double) 3-tuple.
// You should be able to reuse the functions you’ve already written.
//
// def intDouble(rng: RNG): ((Int,Double), RNG)
// def doubleInt(rng: RNG): ((Double,Int), RNG)
// def double3(rng: RNG): ((Double,Double,Double), RNG)

def intDouble(rng: RNG): ((Int,Double), RNG) =
  val (intN, s1)   = nonNegativeInt(rng)
  val (doubleN,s2) = double(s1)
  ((intN, doubleN), s2)

def doubleInt(rng: RNG): ((Double,Int), RNG) =
  val ((i,d),s) = intDouble(rng)
  ((d,i),s)

def double3(rng: RNG): ((Double,Double,Double), RNG) =
  val (d1, s1) = double(rng)
  val (d2, s2) = double(s1)
  val (d3, s3) = double(s2)
  ((d1,d2,d3),s3)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.4
//
// Write a function to generate a list of random integers.
//
// def ints(count: Int)(rng: RNG): (List[Int], RNG)
def ints(count: Int)(rng: RNG): (List[Int], RNG) =
  (0 until count).foldLeft((List.empty[Int],rng)) { case ((lst,r),_) =>
    val (n,s) = r.nextInt
    (n :: lst, s)
  }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.5
//
// Use map to reimplement double in a more elegant way. See exercise 6.2.
//...............
def repeats[R](rng: RNG, count: Int, f:RNG => (R,RNG) ): (List[R], RNG) =
  (0 until count).foldLeft((List.empty[R],rng)) { case ((lst,r),_) =>
    val (n,s) = f(r)
    (n :: lst, s)
  }

def doubles(count:Int)(rng: RNG):(List[Double],RNG) =
  repeats(rng, count, double)


type Rand[+A] = State[RNG,A] //RNG => (A, RNG)

val int: Rand[Int] = _.nextInt
def unit[S,A](a: A): S=>(A,S) = rng => (a, rng)
def map[S,A,B](s: S=>(A,S))(f: A => B): S=>(B,S) = rng => {
  val (a, rng2) = s(rng)
  (f(a), rng2)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.6
//
// Write the implementation of map2 based on the following signature.
// This function takes two actions, ra and rb, and a function f for combining their results,
// and returns a new action that combines them:
//
// def map2[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C]

// ..................... impl ................

def map2[S,A,B,C](ra: S=>(A,S), rb: S=>(B,S))(f: (A, B) => C): S=>(C,S) =
  rng =>
    val (a,sa) = ra(rng)
    val (b,sb) = rb(sa)
    val c = f(a,b)
    (c,sb)

def both[A,B](ra: Rand[A], rb: Rand[B]): Rand[(A,B)] = map2(ra, rb)((_, _))
val randIntDouble: Rand[(Int, Double)] = both(int, double)
val randDoubleInt: Rand[(Double, Int)] = both(double, int)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.7
//
// Hard: If you can combine two RNG transitions, you should be able to combine a whole list of them.
// Implement sequence for combining a List of transitions into a single transition.
// Use it to reimplement the ints function you wrote before.
// For the latter,you can use the standard library function List.fill(n)(x) to make a list with x
// repeated n times.
//
// def sequence[A](fs: List[Rand[A]]): Rand[List[A]]

def sequence[S,A](fs: List[S=>(A,S)]): S=>(List[A],S) =
  rng =>
    fs.foldLeft( (List.empty[A],rng) ){ case ((lst,r),itm) =>
      val (i,ir) = itm(r)
      (i :: lst, ir)
    }

// One such function is nonNegativeLessThan, which generates an integer
// between 0 (inclusive) and n (exclusive):
//
// def nonNegativeLessThan(n: Int): Rand[Int]
//
// A first stab at an implementation might be to generate a non-negative integer modulo n:
//
// def nonNegativeLessThan(n: Int): Rand[Int] =
//   map(nonNegativeInt) { _ % n }
//
// This will certainly generate a number in the range, but it’ll be skewed
// because Int.MaxValue may not be exactly divisible by n.
//
// So numbers that are less than the remainder of that division will come up more frequently.
//
// When nonNegativeInt gen- erates numbers higher than the largest multiple of n
// that fits in a 32-bit integer, we should retry the generator and hope to get a smaller number.
// We might attempt this:
//
//
//
//   def nonNegativeLessThan(n: Int): Rand[Int] =
//     map(nonNegativeInt) { i => n that fits in a 32-bit Int.
//       val mod = i % n
//       if (i + (n-1) - mod >= 0) mod else nonNegativeLessThan(n)(???)
//     }
//                                                                ↑
//                                                                ⎜
//                                          ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
//                                          ⎜Retry recursively if the Int we got    ⎜
//                                          ⎜is higher than the largest multiple of ⎜
//                                          ⎜n than first in 32 bit Int             ⎜
//
// This is moving in the right direction, but nonNegativeLessThan(n)
// has the wrong type to be used right there. Remember, it should return a Rand[Int]
// which is a function that expects an RNG!
//
// But we don’t have one right there. What we would like is to chain things together
// so that the RNG that’s returned by nonNegativeInt is passed along
// to the recursive call to nonNegativeLessThan. We could pass it along explicitly
// instead of using map, like this:

//......................................................
// это из книги и по ходу с ошибкой
def nonNegativeLessThan_Book(n: Int): Rand[Int] =
  rng =>
    val (i, rng2) = nonNegativeInt(rng)
    val mod = i % n
    if (i + (n-1) - mod >= 0)
      (mod, rng2)
    else
      nonNegativeLessThan(n)(rng) // а какого rng, а не rng2 ???
                                  // при тех же данных (n,rng)
                                  // получим тот же результат и бесконечную рекурсию

def nonNegativeLessThan(n: Int): Rand[Int] =
  rng =>
    val (i, rng2) = nonNegativeInt(rng)
    val mod = i % n
    if (i + (n-1) - mod >= 0)
      (mod, rng2)
    else
      nonNegativeLessThan(n)(rng2)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.8
//
// Implement flatMap, and then use it to implement nonNegativeLessThan.
//
// def flatMap[A,B](f: Rand[A])(g: A => Rand[B]): Rand[B]

def flatMap[S,A,B](f: S=>(A,S))(g: A=>S=>(B,S)): S=>(B,S) =
  rng =>
    val (a,aS) = f(rng)
    g(a)(aS)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.9
//
// Reimplement map and map2 in terms of flatMap.
// The fact that this is possible is what we’re referring to when we say that flatMap is more powerful than map and map2.

def map_r[A,B](s:Rand[A])(f: A=>B):Rand[B] =
  flatMap(s){ a =>
    rng =>
      val b = f(a)
      (b,rng)
  }

def map2_r[A,B,C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C):Rand[C] =
    flatMap(ra){ a =>
      flatMap(rb){ b =>
        rng => (f(a,b),rng)
      }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.10
//
// Generalize the functions unit, map, map2, flatMap, and sequence.
// Add them as meth- ods on the State case class where possible.
// Otherwise you should put them in a State companion object.

type State[S,+A] = S => (A,S)
//case class State[S,+A](run: S => (A,S))

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ex 6.11
// Hard: To gain experience with the use of State, implement a finite state automaton that models a simple candy dispenser.
// The machine has two types of input: you can insert a coin, or you can turn the knob to dispense candy.
// It can be in one of two states: locked or unlocked. It also tracks how many candies are left and how many coins it contains.
//
// Сложный: чтобы получить опыт использования состояния,
// реализуйте автомат с конечным состоянием, который моделирует простой раздатчик конфет.
// Автомат имеет два типа ввода:
//   вы можете вставить монету,
//   или вы можете повернуть ручку, чтобы выдать конфеты.
// Он может находиться в одном из двух состояний: заблокирован или разблокирован.
// Он также отслеживает, сколько конфет осталось и сколько в нем монет.
//
//  sealed trait Input
//  case object Coin extends Input
//  case object Turn extends Input
//  case class Machine(locked: Boolean, candies: Int, coins: Int)
//
//  The rules of the machine are as follows:
//
//   Inserting a coin into a locked machine will cause it to unlock if there’s any candy left.
//   Turning the knob on an unlocked machine will cause it to dispense candy and become locked.
//   Turning the knob on a locked machine or inserting a coin into an unlocked machine does nothing.
//   A machine that’s out of candy ignores all inputs.
//
//  Правила машины таковы:
//   Вставив монету в запертый автомат, он разблокируется, если в нем остались конфеты.
//   Поворот ручки на разблокированном автомате приведет к тому, что он выдаст конфеты и заблокируется.
//   Поворот ручки на заблокированной машине или вставка монеты в разблокированную машину ничего не дает.
//   Машина, в которой нет конфет, игнорирует все входные данные.
//
// The method simulateMachine should operate the machine based on the list of inputs and return
// the number of coins and candies left in the machine at the end.
//
// For example, if the input Machine has 10 coins and 5 candies,
// and a total of 4 candies are suc- cessfully bought, the output should be (14, 1).
//
//
// Метод SimulationMachine должен управлять машиной на основе списка входных данных и возврата
// количество монет и конфет, оставшихся в автомате в конце.
//
// Например, если на входе Machine 10 монет и 5 конфет,
// и всего успешно куплено 4 конфеты, вывод должен быть (14, 1).
//
//
// def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)]

sealed trait Input
case object Coin extends Input
case object Turn extends Input
case class Machine(locked: Boolean, candies: Int, coins: Int)
def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] =
  machineStart =>
    val lst: Seq[Machine => Machine] = inputs.map { input =>
      (state:Machine) => {
        if state.candies>0
        then
          input match
            case Coin =>
              if state.locked
              then state.copy(locked = false, coins = state.coins+1)
              else state
            case Turn =>
              if ! state.locked
              then state.copy(locked = true, candies = state.candies-1)
              else state
        else
          state
      }
    }
    val finalState = lst.foldLeft( machineStart ){ case (currentState, transition) => transition(currentState) }
    ((finalState.coins, finalState.candies), finalState)


@main
def main(): Unit = {
  val initial = Machine(locked = true, candies = 5, coins = 10)
  val ((coins,candies),finalState) = simulateMachine(List(Coin,Turn,Coin,Turn,Coin,Turn,Coin,Turn))(initial)
  println(
    s"""|coins      = $coins
        |candies    = $candies
        |finalState = $finalState
        |""".stripMargin)
}