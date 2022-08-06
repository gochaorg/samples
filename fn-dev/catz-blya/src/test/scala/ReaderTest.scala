import cats.data.Reader
import cats.syntax.applicative._

class ReaderTest extends munit.FunSuite {
  
  case class Db(
    usernames:Map[Int,String],
    passwords:Map[String,String]
  )

  type DbReader[A] = Reader[Db,A]

  def findUsername( usrId: Int ):DbReader[Option[String]] = Reader( _.usernames.get(usrId) )
  def checkPassword( username:String, password:String ):DbReader[Boolean] = Reader( _.passwords.get(username).map(_ == password).getOrElse(false) )
  def checkLogin( usrId:Int, pswd:String ):DbReader[Boolean] = 
    for {
      login  <- findUsername(usrId)
      passOk <- login.map { 
                  usrName => checkPassword(usrName, pswd)
                }.getOrElse { false.pure[DbReader] }
    } yield passOk

  test("") {
    val users = Map(
      1 -> "dade",
      2 -> "kate",
      3 -> "margo"
    )
    val pswd = Map(
      "dade" -> "123",
      "kate" -> "234",
      "margo" -> "345"
    )
    val db = Db(users,pswd)

    println( checkLogin(1, "123").run(db) )
  }
}
