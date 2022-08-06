import cats.Monad;
import cats.effect.SyncIO;
import cats.syntax.applicative._
import cats.implicits.toFlatMapOps

class MainTaskTest extends munit.FunSuite {
  override def test(name:String)(run : => Any)(implicit loc: munit.Location):Unit = {
    println()
    println(name)
    println("="*60)
    super.test(name)(run)(loc)
  }

  enum Status:
    case Exists
    case None

  trait Service[F[_]] {
    def compute(id:Int):F[Status]
  }

  class ServiceImpl[F[_]:Monad] extends Service[F] {
    def compute(id: Int): F[Status] = {
      summon[Monad[F]].pure( if id%2==0 then Status.Exists else Status.None )
    }
  }

  def compute[F[_]:Monad]( ids:List[Int], srvc:Service[F] ):F[Map[Int,Status]] = {  
    ids.foldLeft( Map[Int,Status]().pure[F] )( (acc,id) => acc.flatMap { map =>
      srvc.compute(id).flatMap { stat => 
        (map + (id -> stat)).pure[F]
      }
    })
  }

  test("ServiceImpl[SyncIO]") {
    val srv = ServiceImpl[SyncIO]()
    println( srv.compute(1).unsafeRunSync() )
  }

  test("compute") {
    val srv = ServiceImpl[SyncIO]()
    println( compute(List(1,2,3,4,5,6),srv).unsafeRunSync() )
  }
}
