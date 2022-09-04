// https://blog.rockthejvm.com/type-level-programming-part-2/

// I mentioned earlier that I’m using Scala 2 - I’ll update this series once Scala 3 arrives - so you’ll need the following to your build.sbt:
//
//   libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
//
// and in order to print a type signature, I have the following code:
object TypeLevelProgramming {
  //import scala.reflect.runtime.universe._
  //def show[T](value: T)(implicit tag: TypeTag[T]) =
  //  tag.toString.replace("myPackage.TypeLevelProgramming.", "")
}

// Again, we aren’t cheating so that we manipulate types at runtime - the compiler 
// will figure out the types before the code is compiled, and we use the above code just to print types.