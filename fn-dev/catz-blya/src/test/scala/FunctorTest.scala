import cats.effect.{IO, IOApp}

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }

  test("ssd") {
    import cats.effect.unsafe.implicits.global
    val run = IO.println("Hello, World!")
    run.unsafeRunSync()
  }

  case class Box[A](value: A)

  trait Printable[A] {
    self =>
    def format(value: A): String
    def contramap[B](func: B => A): Printable[B] =
      new Printable[B] {
        def format(value: B): String =
          self.format(func(value))
      }
  }

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  implicit val stringPrintable: Printable[String] =
    new Printable[String] {
      def format(value: String): String = "\"" + value + "\""
    }
  
  implicit val booleanPrintable: Printable[Boolean] =
    new Printable[Boolean] {
      def format(value: Boolean): String = if(value) "yes" else "no"
    }

  implicit def boxPrintable[A](implicit p: Printable[A]):Printable[Box[A]] =
    p.contramap[Box[A]](_.value)

  test("aaa") {
    println( format(true) )
    println( format("abc") )
    println( format(Box("bbox")) )
  }
}
