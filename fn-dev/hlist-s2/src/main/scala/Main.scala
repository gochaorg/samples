sealed trait HList
object HList {
  case class Nil() extends HList {
    def ::[H](h: H): Head[H, Nil] = {
      Head(h, nil)
    }
  }

  val nil:Nil = Nil()

  case class Head[H, T](head: H, tail: T) extends HList {
    def ::[A](h: A): Head[A, Head[H, T]] = Head(h, this)
  }

  trait Fetch[W,HL] {
    def fetch(hl:HL):W
  }

  implicit def base[W,T]:Fetch[W, Head[W,T]] = hl => hl.head
  implicit def induct[W,H,T](implicit f:Fetch[W,T]):Fetch[W, Head[H,T]] = hl => f.fetch(hl.tail)

  implicit class Find[HL <: HList]( hl:HL ) {
    def find[W](implicit fetch:Fetch[W,HL]):W = fetch.fetch(hl)
  }
}

object Main extends App {
  import HList._

  val third: Head[String, Head[Boolean, Head[Int, Nil]]] = "third" :: true :: 10 :: nil

  println("start")
  println(third.find[String])
  println(third.find[Boolean])
  println(third.find[Int])
  println("finish")
}