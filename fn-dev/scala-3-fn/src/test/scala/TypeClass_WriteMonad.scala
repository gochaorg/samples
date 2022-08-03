class MySuite extends munit.FunSuite {
  def true_0:Boolean = true
  def false_0:Boolean = false

  def neg(x:Boolean) = !x
  def and(a:Boolean,b:Boolean) = a && b
  def or(a:Boolean,b:Boolean) = a || b

  def my_fun0( a:Boolean, b:Boolean, c:Boolean ) = or(and(a,b),neg(c))

  test("basic fun") {
    println("\nbasic fun")
    println("="*30)
    val r = my_fun0(true_0,true_0,false_0)
    println(r)
  }

  trait MyBool[T] {
    def True:T
    def False:T

    def neg(a:T):T
    def and(a:T,b:T):T
    def or(a:T,b:T):T
  }

  given MyBool[Boolean] with
    def True=true
    def False=false
    def and(a:Boolean,b:Boolean)= a && b
    def or(a:Boolean,b:Boolean)= a || b
    def neg(a:Boolean) = !a

  def my_fun1[T:MyBool]( a:T, b:T, c:T )=
    val t = summon[MyBool[T]]
    t.or(t.and(a,b),t.neg(c))

  test("use trait for MyBool[Boolean]") {
    println( "\nuse trait for MyBool[Boolean]" )
    println( "="*30 )
    val t = summon[MyBool[Boolean]]
    var r = my_fun1(t.True, t.True, t.False)
    println( r )
  }

  trait SemiGroup[T]:
    extension (x: T) def combine (y: T): T

  trait Monoid[T] extends SemiGroup[T]:
    def unit: T

  given Monoid[String] with
    extension (x: String) def combine (y: String): String = 
      (x.trim().length() < 1) match {
        case true => y
        case _ => x + "\n" + y
      }
    def unit: String = ""

  test("string monoid") {
    println("\nstring monoid")
    println("="*30)
    val init = summon[Monoid[String]].unit
    val res = init.combine("a").combine("b").combine("c")
    println(res)
  }

  given MyBool[(Boolean,String)] with
    def True=(true,"True")
    def False=(false,"False")
    def and(a:(Boolean,String),b:(Boolean,String))=
      val r = a._1 && b._1
      (r, s"and(a=${a._1}, b=${b._1})=$r")    
    def or(a:(Boolean,String),b:(Boolean,String))=
      val r = a._1||b._1
      (r, s"or(a=${a._1}, b=${b._1})=$r")
    def neg(a:(Boolean,String))=
      val r = !a._1
      (r, s"neg(${a._1})=$r")

  test("use trait for MyBool[(Boolean,String)]") {
    println( "\nuse trait for MyBool[(Boolean,String)]" )
    println( "="*30 )
    val t = summon[MyBool[(Boolean,String)]]
    var r = my_fun1(t.True, t.True, t.False)
    println( r )
  }

  def my_fun2[T:MyBool]( a:T, b:T, c:T )=
    val t = summon[MyBool[T]]
    val or = t.or
    val and = t.and
    val neg = t.neg

    or(and(a,b),neg(c))
  
  test("use trait for MyBool[(Boolean,String)] with fun2") {
    println( "\nuse trait for MyBool[(Boolean,String)] with fun2" )
    println( "="*30 )
    val t = summon[MyBool[(Boolean,String)]]
    var r = my_fun2(t.True, t.True, t.False)
    println( r )
  }

  def my_fun3[T](or:(T,T)=>T, and:(T,T)=>T, neg:T=>T)( a:T, b:T, c:T )=
    or(and(a,b),neg(c))
  
  test("use trait for MyBool[(Boolean,String)] with fun3") {
    println( "\nuse trait for MyBool[(Boolean,String)] with fun3" )
    println( "="*30 )
    val t = summon[MyBool[(Boolean,String)]]
    val r = my_fun3(t.or, t.and, t.neg)(t.True, t.True, t.False)
    println( r )
  }

  def traverse0[A]( f:((A,String))=>((A,String)) ):((A,String))=>((A,String)) =
    (a,from) => 
      val r = f(a,from)
      (r._1, from + r._2)
    
  def traverse0[A]( f:((A,String),(A,String))=>(A,String) ):((A,String),(A,String))=>(A,String) = {
    case ((a,a_log),(b,b_log)) =>
      val (r,f_log) = f((a,a_log), (b,b_log))
      (r, a_log + b_log + f_log)
  }
    
  test("traverse for (Boolean,String) on fun3") {
    println("traverse for (Boolean,String) on fun3")
    println("="*40)

    val t = summon[MyBool[(Boolean,String)]]
    val and = traverse0(t.and)
    val or = traverse0(t.or)
    val neg = traverse0(t.neg)
    val r = my_fun3(or, and, neg)(t.True, t.True, t.False)
    println( r )
  }

  class Trv[A]( 
    base:MyBool[A], 
    f1:(A=>A)=>(A=>A),
    f2:((A,A)=>A)=>((A,A)=>A),
  ) extends MyBool[A] {
    def True: A = base.True
    def False: A = base.False

    lazy val and_f = f2(base.and)
    def and(a: A, b: A): A = and_f(a,b)

    lazy val neg_f = f1(base.neg)
    def neg(a: A): A = neg_f(a)

    lazy val or_f = f2(base.or)
    def or(a: A, b: A): A = or_f(a,b)
  }

  test("traverse for (Boolean,String) on fun1") {
    println("traverse for (Boolean,String) on fun1")
    println("="*40)

    val t = summon[MyBool[(Boolean,String)]]
    val tr = Trv[(Boolean,String)](
      t,
      traverse0,
      traverse0,
    )
    val r = my_fun1(t.True, t.True, t.False)(tr)
    println( r )
  }

  //////////////////////////////////////

  def combine[A,B:Monoid]( f:((A,B))=>(A,B) ):((A,B))=>((A,B)) =
    (a,b) =>
      val (fr,fb) = f(a,b)
      (fr,b.combine(fb))

  def combine2[A,B:Monoid]( f:((A,B),(A,B))=>(A,B) ):((A,B),(A,B))=>(A,B) = {
    case ((a,ab),(b,bb)) =>
      val (fr,fb) = f((a,ab),(b,bb))
      (fr,ab.combine(bb).combine(fb))
  }

  test("combine for (Boolean,String) on fun3") {
    println("combine for (Boolean,String) on fun3")
    println("="*40)

    val t = summon[MyBool[(Boolean,String)]]
    val and = combine2(t.and)
    val or = combine2(t.or)
    val neg = combine(t.neg)
    val (res,log) = my_fun3(or, and, neg)(t.True, t.True, t.False)
    println( s"""|res = $res
                 |log = $log
                 |""".stripMargin )
  }


  test("combine for (Boolean,String) on fun1") {
    println("combine for (Boolean,String) on fun1")
    println("="*40)

    val t = summon[MyBool[(Boolean,String)]]
    val tr = Trv[(Boolean,String)](
      t,
      combine,
      combine2,
    )
    val (res,log) = my_fun1(t.True, t.True, t.False)(tr)
    println( s"""|res = $res
                 |log = $log
                 |""".stripMargin )
  }
}
