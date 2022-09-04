// https://blog.rockthejvm.com/type-level-programming-scala-3/
// Программирование на уровне типов в Scala 3, часть 1: сравнение типов
//
// This article will introduce you to type-level programming on Scala 3 — the capability to 
// express computations on types instead of values, by making the compiler figure out some type relationships at compile time. 
// By encoding some computational problem as a type restriction or as an enforcement of a type relationship, 
// we can make the compiler “solve” our computation by searching, inferring or validating the proper use of types.
//
// Эта статья познакомит вас с программированием на уровне типов в Scala 3 — возможностью 
// выражать вычисления для типов вместо значений, 
// заставляя компилятор выяснять некоторые отношения типов во время компиляции. 
// Закодировав некоторую вычислительную задачу как ограничение типа или принудительное выполнение отношения типов, 
// мы можем заставить компилятор «решать» наши вычисления путем поиска, 
// вывода или проверки правильности использования типов.

class MathTest extends munit.FunSuite {
  // 1. Encoding Type-Level Numbers
  // 1. Кодирование номеров уровня типа
  // --------------------------------------
  //
  // The natural numbers set consists of all the numbers from 0 to infinity, counted one by one. 
  // One way of thinking about the naturals set is to imagine all the numbers extending forever. 
  // An alternative way — which is also much more useful for us — is 
  // to define these numbers in terms of their relationship to each other. 
  // This is called the Peano representation of numbers.

  // Набор натуральных чисел состоит из всех чисел от 0 до бесконечности, сосчитанных одно за другим. 
  // Один из способов представить набор натуральных чисел — представить, что все числа простираются бесконечно. 
  // Альтернативный способ — который также гораздо полезнее для нас — состоит в том, 
  // чтобы определить эти числа с точки зрения их отношения друг к другу. Это называется представлением чисел Пеано.
  trait Nat
  class _0 extends Nat
  class Succ[A <: Nat] extends Nat



  // The lines above fully represent the entire set of natural numbers: 
  // the number zero, and the succession relationship between any two numbers. 
  // By these two rules alone, we can obtain any number in the naturals set. 
  // For example, the number 127 is Succ[Succ[Succ[...[_0]]]], 
  // where Succ occurs 127 times. For the following examples, we should add a few type aliases:

  // Строки выше полностью представляют весь набор натуральных чисел: 
  // число ноль и отношение последовательности между любыми двумя числами. 
  // Только с помощью этих двух правил мы можем получить любое число из множества натуральных чисел. 
  // Например, число 127 — это Succ[Succ[Succ[...[_0]]]], 
  // где Succ встречается 127 раз. Для следующих примеров мы должны добавить несколько псевдонимов типов:

  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]

  // Note that this notation has nothing to do with the literal types recently added in Scala 3. 
  // The literal types have no relationship to each other, so we’ll need to model this relationship ourselves.

  // Обратите внимание, что эта нотация не имеет ничего общего с типами литералов, недавно добавленными в Scala 3. 
  // Типы литералов не имеют отношения друг к другу, поэтому нам нужно самим смоделировать это отношение.

  
  // 2. Ordering Numbers at the Type Level
  // 2. Номера для заказа на уровне типа
  // --------------------------------------------------------

  // As in the Scala 2 version of the article, we’ll create a small trait to 
  // describe the ordering relationship between types, we’ll name this <, because we can.

  // Как и в версии статьи для Scala 2, мы создадим небольшой трейт 
  // для описания отношения порядка между типами, назовем его <, потому что можем.

  trait <[A <: Nat, B <: Nat]

  // For example, we’ll say that the number 1 is less than 2 if there exists an instance of <[_1, _2]. 
  // This is how we’ll interpret the “less than” relationship, between types, at the type level.

  // Например, мы скажем, что число 1 меньше 2, если существует экземпляр <[_1, _2]. 
  // Вот как мы будем интерпретировать отношение «меньше чем» между типами на уровне типов.

  // In order to model this relationship, we’ll make the compiler prove the truth value of the question 
  // “is 1 less than 3” by creating given instances of <. 
  // If the compiler can have access to one — either by our explicit definition or 
  // by a synthetic construction by the compiler — then the relationship is proven. 
  // If the compiler cannot access or create a given instance of the appropriate type, 
  // the relationship cannot be proven and therefore is deemed to be false.

  // Чтобы смоделировать эту связь, мы заставим компилятор доказать истинность вопроса 
  // «на 1 меньше, чем на 3», создав заданные экземпляры <. 
  // Если компилятор может иметь доступ к одному — либо по нашему явному определению, 
  // либо по синтетической конструкции компилятора — тогда связь доказана. 
  // Если компилятор не может получить доступ или создать данный экземпляр соответствующего типа, 
  // связь не может быть доказана и, следовательно, считается ложной.

  
  // 3. Proving Ordering at the Type Level
  // 3. Доказательство порядка на уровне типа
  // --------------------------------------------------

  // First, we need to think about how we can prove this relationship in mathematical terms. 
  // How can we determine that _3 is “less than” _5 when all we have is 
  // the “number” _0 and the succession relationship between two consecutive numbers?

  // Во-первых, нам нужно подумать о том, как мы можем доказать эту связь в математических терминах. 
  // Как мы можем определить, что _3 «меньше» _5, 
  // если у нас есть только «число» _0 и отношение последовательности между двумя последовательными числами?

  // We can solve this problem by writing the axioms (basic truths) of the < relationship. Here is how to think about it:
  // Мы можем решить эту проблему, написав аксиомы (основные истины) отношения <. Вот как об этом думать:

  // The number 0 is smaller than any other natural number. 
  // Besides zero, every other natural number is the successor of some other number. 
  // So it’s safe to say that, for every number in the naturals set, the successor to that number is greater than zero.

  // Число 0 меньше любого другого натурального числа. 
  // Помимо нуля, любое другое натуральное число является потомком некоторого другого числа. 
  // Поэтому можно с уверенностью сказать, что для каждого числа в наборе натуральных чисел последующее число больше нуля.

  // If we can somehow prove that number A is smaller than number B, 
  // then it’s also true that the successor of A is smaller than the successor of B. 
  // This axiom can also be expressed backwards: we can say that A is less than B 
  // if and only if the predecessor of A is less than the predecessor of B.

  // Если мы можем каким-то образом доказать, что число А меньше числа В, 
  // то верно и то, что последующее число А меньше, чем последующее число В. 
  // Эту аксиому также можно выразить в обратном порядке: мы можем сказать, 
  // что А меньше, чем В, если и только если предшественник A меньше предшественника B.

  // Let’s walk through an example: say we want to determine if _3 < _5.
  // Давайте рассмотрим пример: скажем, мы хотим определить, является ли _3 < _5.

  // By the second axiom, _3 < _5 if and only _2 < _4 because _3 = Succ[_2] and _5 = Succ[_4]. So we need to prove _2 < _4.

  // По второй аксиоме _3 < _5 тогда и только тогда, когда _2 < _4, 
  // потому что _3 = Succ[_2] и _5 = Succ[_4]. Итак, нам нужно доказать, что _2 < _4.

  //  Again, by the second axiom, _2 < _4 if and only if _1 < _3, for the same reason.
  // Опять же, по второй аксиоме, _2 < _4 тогда и только тогда, когда _1 < _3, по той же причине.

  //  Second axiom again: _1 < _3 if and only if _0 < _2.
  // Снова вторая аксиома: _1 < _3 тогда и только тогда, когда _0 < _2.

  //  _0 < _2 is true, by virtue of the first axiom.
  // _0 < _2 верно в силу первой аксиомы.

  // Therefore, walking back, _3 < _5 is true.
  // Следовательно, возвращаясь назад, _3 < _5 верно.

  // How can we embed that in Scala code? We’ll make the compiler search for, or create, given instances for the < type.
  // Как мы можем встроить это в код Scala? Мы заставим компилятор искать или создавать заданные экземпляры для типа <.

  // For the first axiom, we’ll make the compiler generate a given <[_0, Succ[N]], for every N which is a natural.
  // Для первой аксиомы мы заставим компилятор генерировать данный <[_0, Succ[N]], для каждого N, который является натуральным.

  // For the second axiom, we’ll make the compiler generate a given <[Succ[A], Succ[B]] if it can find a given <[A, B] already in scope.
  // Для второй аксиомы мы заставим компилятор генерировать данный <[Succ[A], Succ[B]], если он может найти заданный <[A, B] уже в области видимости.

  // Getting that in code leads to the following givens:
  // Получение этого в коде приводит к следующим данным:

  // given basic[B <: Nat]: <[_0, Succ[B]] with {}
  // given inductive[A <: Nat, B <: Nat](using lt: <[A, B]): <[Succ[A], Succ[B]] with {}

  // Note that in Scala 3.1 (not out yet at the time of writing) you’ll be able to say <[_0, Succ[B]]() instead of <[_0, Succ[B]] with {}, for conciseness.
  // Обратите внимание, что в Scala 3.1 (еще не вышедшем на момент написания) вы сможете сказать <[_0, Succ[B]]() вместо <[_0, Succ[B]] с {} для краткости.

  // For ergonomics, we’re going to store the above givens in the companion of <, so that the code will look like this:
  // Для эргономики мы собираемся хранить приведенные выше данные в компаньоне <, чтобы код выглядел так:

  object < {
    given basic[B <: Nat]: <[_0, Succ[B]] with {}
    given inductive[A <: Nat, B <: Nat](using lt: <[A, B]): <[Succ[A], Succ[B]] with {}
    def apply[A <: Nat, B <: Nat](using lt: <[A, B]) = lt
  }

  // I also took the liberty of writing an apply method which simply surfaces out 
  // whatever given value of the requested type the compiler is able to find (or synthesize).

  // Я также позволил себе написать метод apply, который просто выводит любое заданное значение 
  // запрошенного типа, которое компилятор может найти (или синтезировать).


  // 4. Testing Type-Level Comparisons
  // 4. Тестирование сравнения на уровне типов
  // ---------------------------------------------------

  // With the above code in place, let’s see how the compiler validates (or not) the type relationships. 
  // The simple test is: does the code compile?

  // Имея приведенный выше код, давайте посмотрим, как компилятор проверяет (или нет) отношения типов. 
  // Простой тест: компилируется ли код?

   val validComparison = <[_3, _5]

  // This one is valid. The code compiles. Let’s see what the compiler does behind the scenes:
  // 
  //   We’re requesting a given of type <[_3, _5]. The compiler needs to find if it can generate one from either of the two given synthesizers we specified earlier.
  //   The second given works, because the compiler looks at the signature of the as-of-yet-ungenerated given: <[Succ[_2], Succ[_4]]. 
  //     However, in order to generate that, it needs to find or create a given of type <[_2, _4].
  //   Again, teh second given can be used, for the same reason. For that, the compiler needs a given of type <[_1, _3].
  //   Same deal, the compiler can create a given of type <[_1, _3] if it can find a given of type <[_0, _2].
  //   Using the first given this time around, we see that the compiler can create a given of type <[_0, _2] because _2 = Succ[_1].
  //   Walking backwards, the compiler can generate all the intermediate givens in order to create the given we requested.

  // Этот действителен. Код компилируется. Давайте посмотрим, что компилятор делает за кулисами:
  // 
  //    Мы запрашиваем данные типа <[_3, _5]. Компилятору необходимо выяснить, может ли он сгенерировать один из двух указанных ранее синтезаторов.
  //    Второй данный работает, потому что компилятор смотрит на подпись еще не сгенерированного данного: <[Succ[_2], Succ[_4]]. 
  //      Однако для того, чтобы сгенерировать это, необходимо найти или создать заданный тип <[_2, _4].
  //    Опять же, по той же причине можно использовать второе данное. Для этого компилятору нужен данный тип <[_1, _3].
  //    То же самое, компилятор может создать заданный тип <[_1, _3], если он может найти заданный тип <[_0, _2].
  //    Используя первый данный на этот раз, мы видим, что компилятор может создать данный тип <[_0, _2], потому что _2 = Succ[_1].
  //    Идя назад, компилятор может сгенерировать все промежуточные данные, чтобы создать данные, которые мы запрашивали.

  // In other words, the relationship <[_3, _5], or written infix _3 < _5 can be proven, so it’s true. 
  // Notice how similarly the compiler generated the given instances in the exact same sequence of 
  // steps we used for our mathematical induction above.

  // Другими словами, отношение <[_3, _5] или письменный инфикс _3 < _5 может быть доказано, значит, оно истинно. 
  // Обратите внимание, как точно так же компилятор сгенерировал данные экземпляры в той же самой последовательности шагов, 
  // которую мы использовали для нашей математической индукции выше.

  // By the same mechanism, we can prove any less-than relationships between types that are mathematically correct. 
  // The givens specification is identical to the mathematical description of the axioms we outlined in the previous section. 
  // Conversely, invalid relationships, e.g. _4 < _2 cannot be proven because the compiler cannot 
  // find (or create) a given of the appropriate type, and so it will not compile our code.

  // С помощью того же механизма мы можем доказать любые отношения «меньше чем» между типами, которые математически правильны. 
  // Данная спецификация идентична математическому описанию аксиом, которое мы изложили в предыдущем разделе. 
  // И наоборот, недопустимые отношения, например. _4 < _2 не может быть доказано, потому что компилятор не может 
  // найти (или создать) данные соответствующего типа, и поэтому он не будет компилировать наш код.


  // 5. Extending Type-Level Comparisons
  // 5. Расширение сравнений на уровне типов
  // ------------------------------------------------------

  // I took the liberty of using the same principle to create a “less than or equal” relationship between types. 
  // This is going to be useful for the quicksort we’re going to do in the upcoming article.
  // 
  // The axioms are similar:
  // 
  //     The number 0 is less than or equal to any other number. In Scala terms, 
  //     it means we can always generate a given <=[_0, N] for any N <: Nat.
  //
  //     The second axiom of <= is identical to the one for <: if a number-type A is 
  //     “less than or equal to” another number type B, that means that Succ[A] <= Succ[B] as well.

  // Я позволил себе использовать тот же принцип, чтобы создать отношения «меньше или равно» между типами. 
  // Это будет полезно для быстрой сортировки, которую мы собираемся сделать в следующей статье.
  // 
  // Аксиомы аналогичны:
  // 
  //      Число 0 меньше или равно любому другому числу. 
  //         В терминах Scala это означает, что мы всегда можем сгенерировать данное <=[_0, N] для любого N <: Nat.
  //
  //      Вторая аксиома <= идентична аксиоме для <: если числовой тип A «меньше или равен» другому 
  //      числовому типу B, это также означает, что Succ[A] <= Succ[B].

  // Compressed, the code will look like this:
  // В сжатом виде код будет выглядеть так:

  trait <=[A <: Nat, B <: Nat]
  object <= {
    given lteBasic[B <: Nat]: <=[_0, B] with {}
    given inductive[A <: Nat, B <: Nat](using lte: <=[A, B]): <=[Succ[A], Succ[B]] with {}
    def apply[A <: Nat, B <: Nat](using lte: <=[A, B]) = lte
  }

  // Again, the correct relationships compile:
  // Опять же, правильные отношения компилируются:

  val lteTest = <=[_3, _3]
  val lteTest2 = <=[_3, _5]

  // whereas the incorrect relationships do not:
  // тогда как неправильные отношения не:

  // val invalidLte = <=[_5, _3] // cannot find the appropriate given // не удается найти подходящий given

  test("run it") {
    println("aaa")
  }

  // https://blog.rockthejvm.com/type-level-programming-part-2/
  //
  // 6. Adding “Numbers” as Types
  // 6. Добавление «чисел» в качестве типов
  // ------------------------------------------
  
  // In a similar style that we used to compare numbers, we’ll create another type that will represent the sum of two numbers as types:
  // В том же стиле, который мы использовали для сравнения чисел, мы создадим еще один тип, который будет представлять сумму двух чисел как типы:
  
  // trait +[A <: Nat, B <: Nat, S <: Nat]

  // which means that “number” A added with “number” B will give “number” S. First, we’ll make the compiler automatically 
  // detect the truth value of A + B = S by making it construct an implicit instance of +[A, B, S]. We should be able to write

  // что означает, что «число» A, добавленное к «числу» B, даст «число» S. Во-первых, мы заставим компилятор автоматически
  // определить истинность A + B = S, создав неявный экземпляр +[A, B, S]. Мы должны быть в состоянии написать

  // val four: +[_1, _3, _4] = +[_1, _3, _4]

  // but the code should not compile if we wrote
  // но код не должен компилироваться, если мы написали

  // val five: +[_3, _1, _5] = +[_3, _1, _5]

  // so for starters, we should create a companion for the + trait and 
  // add an apply method to fetch whatever implicit instance can be built by the compiler:

  // Итак, для начала мы должны создать компаньон для трейта + и 
  // добавить метод apply для получения любого неявного экземпляра, который может быть создан компилятором:

  // object + {
  //   def apply[A <: Nat, B <: Nat, S <: Nat](implicit plus: +[A, B, S]): +[A, B, S] = plus
  // }

  // In the same companion object we will also build the implicit values that the compiler can work with.
  // В том же сопутствующем объекте мы также создадим неявные значения, с которыми сможет работать компилятор.

  // 7. Axioms of Addition as Implicits
  // 7. Аксиомы сложения как имплициты
  // -----------------------------------

  // First, we know that 0 + 0 = 0. In terms of the trait that we defined, 
  // that means the compiler must have access to an instance of +[_0, _0, _0]. So let’s build it ourselves:

  // Во-первых, мы знаем, что 0 + 0 = 0. В терминах трейта, который мы определили, 
  // это означает, что компилятор должен иметь доступ к экземпляру +[_0, _0, _0]. Итак, давайте создадим его сами:

  // implicit val zero: +[_0, _0, _0] = new +[_0, _0, _0] {}

  // That one was the easiest. Another “base” axiom is that for any number A > 0, 
  // it’s always true that A + 0 = A and 0 + A = A. We can embed these axioms as implicit methods:

  // Тот был самым легким. Другая «базовая» аксиома состоит в том, что для любого числа A > 0 
  // всегда верно, что A + 0 = A и 0 + A = A. Мы можем внедрить эти аксиомы как неявные методы:

  // implicit def basicRight[A <: Nat](implicit lt: _0 < A): +[_0, A, A] = new +[_0, A, A] {}
  // implicit def basicLeft[A <: Nat](implicit lt: _0 < A): +[A, _0, A] = new +[A, _0, A] {}

  // Notice that the type +[_0, A, A] and +[A, _0, A] are different - this is why we need two different methods there.
  // Обратите внимание, что тип +[_0, A, A] и +[A, _0, A] разные — поэтому нам нужны два разных метода.

  // At this point, you might be wondering why we need those constraints and 
  // why we can’t simply say that for any number A, it’s always true that 0 + A = A and A + 0 = A. 
  // That’s true and more general, but it’s also confusing to the compiler, 
  // because it would have multiple routes through which it can build an instance of +[_0, _0, _0]. 
  // We want to separate these cases so that the compiler can build each implicit instance by following exactly one induction path.

  // В этот момент вам может быть интересно, зачем нам нужны эти ограничения и 
  // почему мы не можем просто сказать, что для любого числа A всегда верно, что 0 + A = A и A + 0 = A. 
  // Это правда и более общее, но это также сбивает с толку компилятор, 
  // потому что у него будет несколько маршрутов, по которым он может построить экземпляр +[_0, _0, _0]. 
  // Мы хотим разделить эти случаи, чтобы компилятор мог построить каждый неявный экземпляр, следуя ровно одному пути индукции.

  // With these 3 implicits, we can already validate a number of sums:
  // С помощью этих трех имплицитов мы уже можем проверить ряд сумм:

  // val zeroSum: +[_0, _0, _0] = +[_0, _0, _0]
  // val anotherSum: +[_0, _2, _2] = +[_0, _2, _2]

  // but are yet to validate sums like +[_2, _3, _5]. 
  // That’s the subject of the following inductive axiom. 
  // The reasoning works like this: 
  //   if A + B = S, then it’s also true that Succ[A] + Succ[B] = Succ[Succ[S]]. 
  // In the compiler’s language, if the compiler can create an implicit +[A, B, S], 
  // then it must als be able to create an implicit +[Succ[A], Succ[B], Succ[Succ[S]]]:
  
  // но еще не проверили суммы вроде +[_2, _3, _5]. 
  // Это является предметом следующей индуктивной аксиомы. 
  // Рассуждение работает следующим образом: 
  //   если A + B = S, то также верно, что Succ[A] + Succ[B] = Succ[Succ[S]]. 
  // На языке компилятора, если компилятор может создать неявный +[A, B, S], 
  // то он также должен быть в состоянии создать неявный +[Succ[A], Succ[B], Succ[Succ[S]] ]:

  // implicit def inductive[A <: Nat, B <: Nat, S <: Nat](implicit plus: +[A, B, S]): +[Succ[A], Succ[B], Succ[Succ[S]]] =
  //   new +[Succ[A], Succ[B], Succ[Succ[S]]] {}

  // With these 4 implicits, the compiler is now able to validate any sum. For example:
  // С этими 4 имплицитами компилятор теперь может проверить любую сумму. Например:

  // val five: +[_2, _3, _5] = +[_2, _3, _5]

  // This compiles, because
  // 
  //     the compiler needs an implicit +[_2, _3, _5] which is in fact +[Succ[_1], Succ[2], Succ[Succ[_3]]]
  //     the compiler can run the inductive method, but it requires an implicit +[_1, _2, _3] which is +[Succ[_0], Succ[_1], Succ[Succ[_1]]]
  //     the compiler can run the inductive method again, but it requires an implicit +[_0, _1, _1]
  //     the compiler can run the basicRight method and build the implicit +[_0, _1, _1]
  //     the compiler can then build all the other dependent implicits

  // Это компилируется, потому что
  // 
  //      компилятору нужен неявный +[_2, _3, _5], который на самом деле +[Succ[_1], Succ[2], Succ[Succ[_3]]]
  //      компилятор может запустить индуктивный метод, но для этого требуется неявный +[_1, _2, _3], который равен +[Succ[_0], Succ[_1], Succ[Succ[_1]]]
  //      компилятор может снова запустить индуктивный метод, но для этого требуется неявный +[_0, _1, _1]
  //      компилятор может запустить метод basicRight и построить неявный +[_0, _1, _1]
  //      компилятор может затем построить все остальные зависимые имплициты  

  // However, if we write an incorrect “statement”, such as
  // Однако, если мы напишем неверное «утверждение», такое как

  // val four: +[_2, _3, _4] = +[_2, _3, _4]

  // the code can’t compile because the compiler can’t find the appropriate implicit instance.
  
  // Right now, our code looks like this:
  
  // trait +[A <: Nat, B <: Nat, S <: Nat]
  // object + {
  //   implicit val zero: +[_0, _0, _0] = new +[_0, _0, _0] {}
  //   implicit def basicRight[B <: Nat](implicit lt: _0 < B): +[_0, B, B] = new +[_0, B, B] {}
  //   implicit def basicLeft[B <: Nat](implicit lt: _0 < B): +[B, _0, B] = new +[B, _0, B] {}
  //   implicit def inductive[A <: Nat, B <: Nat, S <: Nat](implicit plus: +[A, B, S]): +[Succ[A], Succ[B], Succ[Succ[S]]] =
  //       new +[Succ[A], Succ[B], Succ[Succ[S]]] {}
  //   def apply[A <: Nat, B <: Nat, S <: Nat](implicit plus: +[A, B, S]): +[A, B, S] = plus
  // }

  // 8. Supercharging Addition
  // 8. Добавление наддува
  // -------------------------------

  // This is great so far! 
  // We can make the compiler validate type relationships like 
  // the “addition” of “numbers” at compile time. 
  // However, at this point we can’t make the compiler figure out what the result of an addition should be 
  // - we need to specify the result type ourselves, and the compiler will simply show a thumbs-up if the type is good.
  
  // Это здорово до сих пор! 
  // Мы можем заставить компилятор проверять отношения типов, такие как 
  // «сложение» «чисел» во время компиляции. 
  // Однако на данный момент мы не можем заставить компилятор разобраться, каким должен быть результат сложения 
  // — нам нужно самим указать тип результата, и компилятор просто покажет палец вверх, если тип подходит.

  // The next level in this Peano arithmetic implementation would be to somehow make 
  // the compiler infer the sum type by itself. For that, we’ll change the type signature of the addition:

  // Следующим уровнем в этой арифметической реализации Пеано было бы каким-то образом заставить 
  // компилятор самостоятельно определить тип суммы. Для этого изменим сигнатуру типа дополнения:

  // trait +[A <: Nat, B <: Nat] extends Nat {
  //   type Result <: Nat
  // }

  // So instead of a type argument, we now have an abstract type member. 
  // This will help us when we use the + type at the testing phase. 
  // Now, in the companion object, we’ll declare an auxiliary type:

  // Итак, вместо аргумента типа у нас теперь есть член абстрактного типа. 
  // Это поможет нам, когда мы будем использовать тип + на этапе тестирования. 
  // Теперь в объекте-компаньоне объявим вспомогательный тип:

  // object + {
  //   type Plus[A <: Nat, B <: Nat, S <: Nat] = +[A, B] { type Result = S }
  // }

  // This new type Plus is exactly the same as our previous + and 
  // we will use it in our implicit resolution. 
  // The trick here is to have the compiler automatically match the 
  // Result abstract type member to the S type argument of the auxiliary sum type.

  // Этот новый тип Plus точно такой же, как наш предыдущий +, и 
  // мы будем использовать его в нашем неявном разрешении. 
  // Хитрость здесь заключается в том, чтобы компилятор автоматически сопоставлял элемент абстрактного типа 
  // Result с аргументом типа S вспомогательного типа суммы.

  // The next step is to change our axiom (read: implicits) definitions to use this new type:
  // Следующим шагом является изменение наших определений аксиом (читай: неявных) для использования этого нового типа:

  // implicit val zero: Plus[_0, _0, _0] = new +[_0, _0] { type Result = _0 }
  // implicit def basicRight[B <: Nat](implicit lt: _0 < B): Plus[_0, B, B] = new +[_0, B] { type Result = B }
  // implicit def basicLeft[B <: Nat](implicit lt: _0 < B): Plus[B, _0, B] = new +[B, _0] { type Result = B }
  // implicit def inductive[A <: Nat, B <: Nat, S <: Nat](implicit plus: Plus[A, B, S]): Plus[Succ[A], Succ[B], Succ[Succ[S]]] =
  //   new +[Succ[A], Succ[B]] { type Result = Succ[Succ[S]] }

  // Each rewrite goes as follows:
  // 
  //     make the return type be the new Plus type instead of the old +
  //     because we can’t build Plus directly, we’ll need to build an instance of + that has the correct type member
  // 
  // Finally, the apply method will need to undergo a change as well. First of all, we’ll get rid of the third type argument:

  // Каждое переписывание происходит следующим образом:
  // 
  //      сделать возвращаемый тип новым типом Plus вместо старого +
  //      поскольку мы не можем построить Plus напрямую, нам нужно создать экземпляр + с правильным членом типа
  // 
  // Наконец, метод применения также должен быть изменен. Прежде всего, избавимся от аргумента третьего типа:  
    
  // def apply[A <: Nat, B <: Nat](implicit plus: +[A, B]): +[A, B] = plus

  // At this point, we can now say
  // Теперь мы можем сказать

  // val five: +[_2, _3] = +.apply

  // and if the code compiles, then the compiler is able to validate the existence of a sum type between _2 and _3.
  // But what’s the result?

  // и если код компилируется, то компилятор может проверить существование типа суммы между _2 и _3.
  // Но каков результат?


  // 9. The Final Blow
  // 9. Последний удар
  // --------------------------------

  // Right now, we can’t see the final result of summing the “numbers”. 
  // If we print the type tag of the sum we won’t get too much info:
  // Прямо сейчас мы не можем видеть окончательный результат суммирования «цифр». 
  // Если мы напечатаем тег type суммы, мы не получим слишком много информации:

  //   > println(show(+[_2, _3]))
  //   TypeTag[_2 + _3]

  // However, we can force the compiler to show the result type to us, 
  // because we have a Result type member in the + trait. All we need to do is change the apply method slightly:
    
  // Однако мы можем заставить компилятор показать нам тип результата, 
  // потому что у нас есть член типа Result в трейте +. Все, что нам нужно сделать, 
  // это немного изменить метод применения:

  //    def apply[A <: Nat, B <: Nat](implicit plus: +[A, B]): Plus[A, B, plus.Result] = plus

  // Instead of returning a +[A, B], we return a Plus[A, B, plus.Result]. We can use this dirty trick because
  // 
  //     Plus is nothing but a type alias
  //     we can use type members in method return types
  // 
  // With this minor change, the code still compiles, but if we show the type tag now, the tag looks different:

  // Вместо возврата +[A, B] мы возвращаем Plus[A, B, plus.Result]. Мы можем использовать этот грязный трюк, потому что
  // 
  //      Плюс не что иное, как псевдоним типа
  //      мы можем использовать члены типа в возвращаемых типах метода
  // 
  // С этим небольшим изменением код по-прежнему компилируется, но если мы сейчас покажем тег type, он будет выглядеть по-другому:

  //   > println(show(+[_2, _3]))
  //   TypeTag[Succ[Succ[_0]] + Succ[Succ[Succ[_0]]]{ type Result = Succ[Succ[Succ[Succ[Succ[_0]]]]] }]

  // In other words, the result type the compiler has is Succ[Succ[Succ[Succ[Succ[_0]]]]], which is _5!
  // The final code is below:

  // Другими словами, компилятор имеет тип результата Succ[Succ[Succ[Succ[Succ[_0]]]]], то есть _5!
  // Окончательный код ниже:

  trait +[A <: Nat, B <: Nat] { type Result <: Nat }
  object + {
    type Plus[A <: Nat, B <: Nat, S <: Nat] = +[A, B] { type Result = S }
    implicit val zero: Plus[_0, _0, _0] = new +[_0, _0] { type Result = _0 }
    implicit def basicRight[B <: Nat](implicit lt: _0 < B): Plus[_0, B, B] = new +[_0, B] { type Result = B }
    implicit def basicLeft[B <: Nat](implicit lt: _0 < B): Plus[B, _0, B] = new +[B, _0] { type Result = B }
    implicit def inductive[A <: Nat, B <: Nat, S <: Nat](implicit plus: Plus[A, B, S]): Plus[Succ[A], Succ[B], Succ[Succ[S]]] =
      new +[Succ[A], Succ[B]] { type Result = Succ[Succ[S]] }
    def apply[A <: Nat, B <: Nat](implicit plus: +[A, B]): Plus[A, B, plus.Result] = plus
  }

  // def main(args: Array[String]): Unit = {
  //     println(show(+[_2, _3]))
  // }

  // P.S. Creating a value of the sum type and then printing it will not produce the same result because of how types are attached to expressions:
  // P.S. Создание значения типа суммы и его последующая печать не даст такого же результата из-за того, как типы присоединяются к выражениям:

  //   > val five: +[_2, _3] = +[_2, _3]
  //   > println(five)
  //   TypeTag[_2 + _3]

  ///////////////////////////////////////////////////////////////////
  // https://blog.rockthejvm.com/type-level-programming-part-3/
  
}
