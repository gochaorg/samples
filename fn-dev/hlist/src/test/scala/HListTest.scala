class HListTest extends munit.FunSuite {
    sealed trait HList
    object HList {
        case class Nil() extends HList {
            def ::[H](h: H): Head[H, Nil] = Head(h, nil)
        }

        val nil:Nil = Nil()

        case class Head[H, T](head: H, tail: T) extends HList {
            def ::[A](h: A): Head[A, Head[H, T]] = Head(h, this)
        }

        trait Fetch[What] {
            def fetch: What
        }
    }

    test("aaa") {
        import HList._
        val third = "third" :: true :: 10 :: nil
    }

}
