package xyz.cofe.sample

import org.scalatest.flatspec.AnyFlatSpec

/**
 * Тест монады 1
 */
class MonadaTest extends AnyFlatSpec {
  val sample = "100;abc;true"
  
  case class Parse[TARGET,FROM]( private val parseFn:FROM=>(TARGET,FROM) ){
    def parse(from:FROM):TARGET = parseFn(from)._1
    def flatMap[U]( f:TARGET=>Parse[U,FROM] ):Parse[U,FROM] = Parse[U,FROM] { src =>
      val r = parseFn(src)
      f(r._1).parseFn(r._2)
    }
    def map[U]( f:TARGET=>U ):Parse[U,FROM] = Parse[U,FROM] { src =>
      val r = parseFn(src)
      (f(r._1),r._2)
    }
  }
  
  val fieldParser:Parse[String,String] = Parse { src =>
    val idx = src.indexOf(";")
    if( idx>=0 ){
      (src.substring(0,idx), src.substring(idx+1))
    }else{
      (src,"")
    }
  }
  
  "Monada" should "try 1" in {
    val parser = fieldParser.flatMap { f1 =>
      fieldParser.flatMap { f2 =>
        fieldParser.map { f3 => (f1,f2,f3) }
      }
    }
    val r = parser.parse(sample)
    println(s"from '${sample}' result: ${r}")
  }
}
