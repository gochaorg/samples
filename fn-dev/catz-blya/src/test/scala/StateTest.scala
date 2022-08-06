import cats.data.State
import cats.syntax.applicative._

class StateTest extends munit.FunSuite {
  type CalcState[A] = State[List[Int],A]

  def evalOne( sym:String ):CalcState[Int] =
    sym match
      case "+" => operator( _ + _ )
      case "-" => operator( _ - _ )
      case "*" => operator( _ * _ )
      case "/" => operator( _ / _ )
      case num => operand( num.toInt )

  def operand(num:Int): CalcState[Int] = State[List[Int],Int] { stack => 
    (num :: stack, num)
  }

  def operator( func: (Int,Int)=>Int ): CalcState[Int] =
    State[List[Int],Int] { stack => stack match
      case b :: a :: tail =>
        val ans = func(a,b)
        (ans :: tail, ans)
      case _ => 
        sys.error("Fail!")
    }

  test("evalOne") {
    println( "evalOne" )
    println( "="*40 )
    println( "21" )
    println( evalOne("21").run(Nil).value )

    val prog = for {
      _ <- evalOne("1")
      _ <- evalOne("2")
      ans <- evalOne("+")
    } yield ans

    println( prog.run(Nil).value )
  }

  def evalAll( input:List[String] ):CalcState[Int] =
    input.foldLeft(0.pure[CalcState]) { (acc,b) => acc.flatMap( _ => evalOne(b)) }

  test("evalAll") {
    println( "evalAll" )
    println( evalAll(List("1", "2", "+", "3", "*")).run(Nil).value )
  }
}
