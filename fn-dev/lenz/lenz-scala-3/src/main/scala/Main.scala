case class AppConfig(mainWindow:MainWinConfig=MainWinConfig())
case class MainWinConfig(localtion:Location=Location())
case class Location(x:Int=0, y:Int=0)

case class Lens[A,B]( get:A=>B, set:(A,B)=>A ):
  def apply(a:A):B = get(a)
  def apply(a:A,b:B) = set(a,b)
  def compose[C](lens:Lens[B,C]):Lens[A,C]=
    Lens(
      get= a => lens.get(get(a)),
      set= (a,c) => set(a, lens.set(get(a), c))
    )
  def +[C](lens:Lens[B,C]):Lens[A,C] = compose(lens)

val xLocaltion = Lens[Location,Int]( 
  get= l => l.x,
  set= (l,x) => l.copy(x = x)
)
val yLocaltion = Lens[Location,Int]( 
  get= l => l.y,
  set= (l,y) => l.copy(y = y)
)

val locationMainWinConfig = Lens[MainWinConfig,Location](
  get= w => w.localtion,
  set= (w,l) => w.copy(localtion = l)
)

val mainWinAppConf = Lens[AppConfig,MainWinConfig](
  get= a=> a.mainWindow,
  set= (a,b) => a.copy(mainWindow = b)
)

@main def hello: Unit = 
  println("Hello world!")
  println(msg)
  
  val appConf = AppConfig()
  println(appConf)
  val mwndConf = mainWinAppConf(appConf)
  println(mwndConf)
  val loc = locationMainWinConfig(mwndConf)
  println(loc)
  val xloc = xLocaltion(loc)
  println(xloc)

  val lens_x = mainWinAppConf + locationMainWinConfig + xLocaltion
  println(lens_x(appConf))

  val newConf = lens_x(appConf, 1)
  println(s"old conf $appConf")
  println(s"new conf $newConf")

def msg = "I was compiled by Scala 3. :)"
