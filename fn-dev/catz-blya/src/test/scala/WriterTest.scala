import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

class WriterTest extends munit.FunSuite {
  type Logged[A] = Writer[Vector[String],A]

  test("factorial 5"){
    def factorial( n:Logged[Int] ):Logged[Int] = {
      for {
        x <- if( n.value==0 ) 1.pure[Logged] else {
          factorial( n.map(_ - 1) ).map( _ * n.value )
        }
        _ <- Vector(s"fact ${n.value} is $x").tell
      } yield x
    }

    val res = factorial( 5.pure[Logged] )
    println(res)
  }
}
