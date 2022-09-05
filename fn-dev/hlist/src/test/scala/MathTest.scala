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
  
  // 10. Lists as types
  // 10. Списки как типы
  // --------------------

  // As is now pretty standard for the series, we’ll represent everything as a type. 
  // In our case, we’ll represent lists of number-types as types again:

  // Сейчас это довольно стандартно для серии, мы будем представлять все как тип. 
  // В нашем случае мы снова будем представлять списки числовых типов как типы:
  
  trait HList
  class HNil extends HList
  class ::[H <: Nat, T <: HList] extends HList

  // You may have seen similar definitions for heterogeneous lists (hence the HList name) in Shapeless and other libraries. 
  // In our case, our HList types are only restricted to Nat types.

  // Вы могли видеть подобные определения для разнородных списков (отсюда и название HList) в Shapeless и других библиотеках. 
  // В нашем случае наши типы HList ограничены только типами Nat.

  // We’re going to sort these HList types into other HList types, all inferred by the compiler. 
  // For that, we’re going to use a merge-sort algorithm. You must know it already. We need to
  // 
  //     split the lists in half
  //     sort the halves
  //     then merge the halves back in a sorted order
  // 
  // Every operation will be encoded as a type, and all results will be computed by the compiler via implicits.

  // Мы собираемся отсортировать эти типы HList по другим типам HList, которые выводятся компилятором. 
  // Для этого воспользуемся алгоритмом сортировки слиянием. Вы должны знать это уже. Нам надо
  // 
  //      разделить списки пополам
  //      рассортировать половинки
  //      затем объедините половинки обратно в отсортированном порядке
  // 
  // Каждая операция будет закодирована как тип, и все результаты будут вычислены компилятором с помощью неявных выражений.

  // 11. Operation 1: The Split
  // 11. Операция 1: Раскол
  // ---------------------------------------------------------

  // We need to be able to split a list in exactly half or at most one element difference 
  // (if the list has an odd number of elements). 
  // I bet you’re used to encoding operations as types - this is 
  // what the second part’s arithmetic was about - so we’ll encode this as a type as well:

  // Нам нужно иметь возможность разделить список ровно пополам или не более чем на одну разницу элементов 
  // (если список имеет нечетное количество элементов). 
  // Бьюсь об заклад, вы привыкли кодировать операции как типы — именно 
  // об этом была арифметика во второй части — так что мы также закодируем это как тип:

  trait Split[HL <: HList, L <: HList, R <: HList]

  // In this case, HL is the original list, and L and R are the “left” and “right” halves of the list, respectively. 
  // We’re going to make the compiler compute instances of this Split type, and thus prove that a list can be halved. 
  // As before, we’re going to do everything in a companion of Split, 
  // starting with the basic case: an empty list is halved into two empty lists:
  
  // В данном случае HL — исходный список, а L и R — «левая» и «правая» половины списка соответственно. 
  // Мы заставим компилятор вычислять экземпляры этого типа Split и таким образом докажем, что список можно разделить пополам. 
  // Как и прежде, мы будем делать все в компаньоне Split, 
  // начиная с базового случая: пустой список делится пополам на два пустых списка:

  object Split {
    implicit val basic: Split[HNil, HNil, HNil] = new Split[HNil, HNil, HNil] {}
  }

  // This is one of the starting points. 
  // Another basic case is 
  // that any one-element list can also be split into itself on the left, and empty on the right:

  // Это одна из отправных точек. 
  // Другой базовый случай заключается в том, 
  // что любой одноэлементный список также может быть разбит на себя слева и пустым справа:

  implicit def basic2[N <: Nat]: Split[N :: HNil, N :: HNil, HNil] =
    new Split[N :: HNil, N :: HNil, HNil] {}

  // In the above, we’re using the infix :: to make the types easier to read. 
  // So for any type N which is a “number”, 
  // the compiler can automatically create an instance of Split[N :: HNil, N :: HNil, HNil], 
  // which is proof of the existence of a split of a list of one element into itself and the empty type.

  // В приведенном выше примере мы используем инфикс :: для облегчения чтения типов. 
  // Таким образом, для любого типа N, который является «числом», 
  // компилятор может автоматически создать экземпляр Split[N::HNil, N::HNil, HNil], 
  // что является доказательством существования разделения списка из одного элемента. в себя и пустой тип.


  // The general inductive case, is when you have a list with at least two elements. 
  // That will have the type N1 :: N2 :: T, where T is some other list type (the tail of the list).

  // Общий индуктивный случай — это когда у вас есть список как минимум из двух элементов. 
  // Он будет иметь тип N1::N2::T, где T — какой-то другой тип списка (хвост списка).

  implicit def inductive[H <: Nat, HH <: Nat, T <: HList, L <: HList, R <: HList]
    (implicit split: Split[T, L, R])
    : Split[H :: HH :: T, H :: L, HH :: R]
    = new Split[H :: HH :: T, H :: L, HH :: R] {}

  // In other words, if the tail T can be split into L and R - as detected 
  // by the compiler in the presence of an implicit 
  // Split[T,L,R] then N1 :: N2 :: T 
  // can be split into N1 :: L and N2 :: R. 
  // One number goes to the left, the other to the right.

  // Другими словами, если хвост T можно разбить на L и R — как 
  // это обнаружено компилятором при наличии неявного 
  // Split[T,L,R], то N1 :: N2 :: T 
  // можно разбить на N1 :: L и N2::R. 
  // Одно число идет влево, другое вправо.

  // We can now add an apply method in Split:
  // Теперь мы можем добавить метод применения в Split:

  def apply[HL <: HList, L <: HList, R <: HList](implicit split: Split[HL, L, R]) = split

  // and then test it out:
  // а затем протестируйте его:

  // val validSplit: Split[_1 :: _2 :: _3 :: HNil, _1 :: _3 :: HNil, _2 :: HNil] = Split.apply

  // This works, because the compiler does the following:
  // 
  //  it requires an implicit Split[_1 :: _2 :: _3 :: HNil, _1 :: _3 :: HNil, _2 :: HNil]
  //  it can build that implicit by running inductive, but it needs an implicit Split[_3 :: HNil, _3 :: HNil, HNil]
  //  it can build that implicit by running basic2[_3]
  //  it will then build the dependent implicits as required

  // Это работает, потому что компилятор делает следующее:
  // 
  //    для этого требуется неявный Split[_1 :: _2 :: _3 :: HNil, _1 :: _3 :: HNil, _2 :: HNil]
  //    он может построить это неявное, запустив индуктивный, но ему нужен неявный Split[_3 :: HNil, _3 :: HNil, HNil]
  //    это можно построить, запустив basic2[_3]
  //    затем он построит зависимые имплициты по мере необходимости

  // Conversely, the compiler will not compile your code if the split is invalid:
  // И наоборот, компилятор не будет компилировать ваш код, если разбиение неверно:

  // val invalidSplit: Split[_1 :: _2 :: _3 :: HNil, _1 :: HNil, _2 :: _3 :: HNil] = Split.apply

  // Though technically viable, the compiler needs to have a single 
  // proof for a split, so we chose the approach of 
  // “one number to the left, one to the right” and consider everything else invalid.

  // Хотя технически это возможно, компилятору необходимо иметь единственное 
  // доказательство для разделения, поэтому мы выбрали подход 
  // «одно число слева, одно справа» и считаем все остальное недействительным.

  // 12. Operation 2: The Merge
  // 12. Операция 2: Слияние
  // ----------------------------------

  // You know the drill - we’ll create a new type which will have the meaning of a sorted merge of two lists:
  // Вы знаете упражнение — мы создадим новый тип, который будет иметь значение отсортированного слияния двух списков:

  trait Merge[LA <: HList, LB <: HList, L <: HList]

  // This means list LA merges with list LB and results in the final list L. 
  // We have two basic axioms we need to start with, 
  // and that is any list merged with HNil results in that list:

  // Это означает, что список LA объединяется со списком LB и приводит к окончательному списку L. 
  // У нас есть две основные аксиомы, с которых нам нужно начать, 
  // и это любой список, объединенный с HNil, приводит к этому списку:

  object Merge {
    implicit def basicLeft[L <: HList]: Merge[HNil, L, L] =
      new Merge[HNil, L, L] {}
    implicit def basicRight[L <: HList]: Merge[L, HNil, L] =
      new Merge[L, HNil, L] {}
  }

  //  This time we need two basic axioms because the types Merge[HNil, L, L] and 
  //  Merge[L, HNil, L] are different to the compiler.

  //  The inductive implicits are interesting. 
  // Considering two lists with at least an element each, 
  // say HA :: TA and HB :: TB, we need to compare their heads HA and HB:

  //      if HA <= HB, then HA must stay first in the result
  //      if HB < HA, then HB must stay first in the result

  //  The question is, what’s the result?

  //      if HA <= HB, then the compiler must find a merge between TA and 
  //         the other list HB :: TB, so it’ll need an implicit instance of 
  //         Merge[TA, HB :: TB, O], where O is some HList, and the final result will be HA :: O
  //      if HB < HA, it’s the other way around - the compiler needs to find 
  //         an implicit of Merge[HA :: TA, TB, O] and then the final result will be HB :: O

  //  So we need to embed those rules as implicits:

  // Это означает, что список LA объединяется со списком LB и приводит к окончательному списку L. 
  // У нас есть две основные аксиомы, с которых нам нужно начать, и это любой список, объединенный с HNil, 
  // приводит к этому списку: На этот раз нам нужны две основные аксиомы, 
  // потому что типы Merge[HNil, L, L] и Merge[L, HNil, L] отличаются от компилятора.

  // Индуктивные имплициты интересны. Рассмотрим два списка, в каждом из которых есть хотя бы один элемент, 
  // скажем, HA::TA и HB::TB, нам нужно сравнить их заголовки HA и HB:

  //     если HA <= HB, то HA должен остаться первым в результате
  //     если HB < HA, то HB должен остаться первым в результате

  // Вопрос в том, каков результат?

  //     если HA <= HB, то компилятор должен найти слияние между TA и другим списком HB::TB, 
  //       поэтому ему потребуется неявный экземпляр Merge[TA, HB::TB, O], 
  //       где O — некоторый HList , и окончательный результат будет HA::O
  //     если HB < HA, то наоборот — компилятору нужно найти имплицит Merge[HA::TA, TB, O] 
  //       и тогда окончательный результат будет HB::O

  // Поэтому нам нужно внедрить эти правила как неявные:

  // implicit def inductiveLTE[HA <: Nat, TA <: HList, HB <: Nat, TB <: HList, O <: HList]
  //   (implicit merged: Merge[TA, HB :: TB, O], lte: HA <= HB)
  //   : Merge[HA :: TA, HB :: TB, HA :: O]
  //   = new Merge[HA :: TA, HB :: TB, HA :: O] {}
  // 
  // implicit def inductiveGT[HA <: Nat, TA <: HList, HB <: Nat, TB <: HList, O <: HList]
  //   (implicit merged: Merge[HA :: TA, TB, O], g: HB < HA)
  //   : Merge[HA :: TA, HB :: TB, HB :: O]
  //   = new Merge[HA :: TA, HB :: TB, HB :: O] {}

  // Let’s take the first case and read it: if the compiler can find an implicit Merge[TA, HB :: TB, O] and 
  // an implicit instance of HA <= HB (based on part 1), 
  // then the compiler will be able to automatically create an instance of Merge[HA :: TA, HB :: TB, HA :: O], 
  // which means that HA stays at the front of the result.

  // Возьмем первый случай и прочитаем его: если компилятор может найти неявный Merge[TA, HB::TB, O] и 
  // неявный экземпляр HA <= HB (на основе части 1), 
  // то компилятор сможет автоматически создать экземпляр Merge[HA::TA, HB::TB, HA::O], 
  // что означает, что HA остается впереди результата.

  // The other case reads in the exact same way except the conditions are the opposite: HB < HA, 
  // so HB needs to stay at the front of the result.

  // Другой случай читается точно так же, за исключением того, что условия противоположны: HB < HA, 
  // поэтому HB должен оставаться впереди результата.

  // Finally, if we add an apply method to Merge:
  // Наконец, если мы добавим метод применения к Merge:

  // def apply[LA <: HList, LB <: HList, O <: HList](implicit merged: Merge[LA, LB, O]) = merged

  // then we should be able to test it:
  // то мы должны быть в состоянии проверить это:

  // val validMerge: Merge[_1 :: _3 :: HNil, _2 :: HNil, _1 :: _2 :: _3 :: HNil] = Merge.apply

  // This works, because the compiler
  //   
  //     requires an implicit Merge[_1 :: _3 :: HNil, _2 :: HNil, _1 :: _2 :: _3 :: HNil]
  //     will run the inductiveLTE, requiring an implicit Merge[_3 :: HNil, _2 :: HNil, _2 :: _3 :: HNil] 
  //       and an implicit _1 < _2, which we’ll assume true by virtue of Part 1
  //     will run inductiveGT, requiring an implicit Merge[_3 :: HNil, HNil, _3 :: HNil]
  //     will run basicLeft, creating an implicit Merge[_3 :: HNil, HNil, _3 :: HNil]
  //     will create all the dependent implicits in reverse order
  // 
  // Conversely, if you try an invalid merge, the compiler won’t compile your code 
  // because it can’t find the appropriate implicits.

  // Это работает, потому что компилятор
  // 
  //      требует неявного Merge[_1 :: _3 :: HNil, _2 :: HNil, _1 :: _2 :: _3 :: HNil]
  //      запустит inductiveLTE, требуя неявного Merge[_3::HNil, _2::HNil, _2::_3::HNil] 
  //        и неявного _1 < _2, которое мы будем считать истинным в силу Части 1.
  //      запустит inductiveGT, требуя неявного Merge[_3 :: HNil, HNil, _3 :: HNil]
  //      запустит basicLeft, создав неявное Merge[_3::HNil, HNil, _3::HNil]
  //      создаст все зависимые имплициты в обратном порядке
  // 
  // И наоборот, если вы попробуете недопустимое слияние, компилятор не скомпилирует ваш код, 
  // потому что не сможет найти подходящие имплициты.

  // 13. Operation 3: The Sort
  // 13. Операция 3: Сортировка
  // ----------------------------

  // By now you should be ahead of my writing - encode the sort operation as a type:
  // К настоящему времени вы должны опередить мое письмо — закодируйте операцию сортировки как тип:

  trait Sort[L <: HList, O <: HList]

  // where L is the input list and O is the output list. 
  // Let’s now think of the sorting axioms, again in the companion object of Sorted.

  // где L — входной список, а O — выходной список. 
  // Давайте теперь подумаем об аксиомах сортировки, опять же, в объекте-компаньоне Sorted.

  // The first basic axiom is that an empty list should stay unchanged:
  // Первая базовая аксиома заключается в том, что пустой список должен оставаться неизменным:

  // implicit val basicNil: Sorted[HNil, HNil] = new Sorted[HNil, HNil] {}

  // Same for a list of one element:
  // То же самое для списка из одного элемента:

  // implicit def basicOne[H <: Nat]: Sorted[H :: HNil, H :: HNil] =
  //   new Sorted[H :: HNil, H :: HNil] {}

  // Now the inductive axiom is the killer one, 
  // as it will require all our previous work. 
  // We’ll need the compiler to split the list, 
  // sort the halves and merge them back. 
  // Here’s how we can encode that:

  // Теперь индуктивная аксиома является убийственной, 
  // так как потребует всей нашей предыдущей работы. 
  // Нам понадобится компилятор, чтобы разделить список, 
  // отсортировать половинки и объединить их обратно. 
  // Вот как мы можем это закодировать:

  // implicit def inductive[I <: HList, L <: HList, R <: HList, SL <: HList, SR <: HList, O <: HList]
  //   (implicit
  //    split: Split[I, L, R],
  //    sl: Sort[L, SL],
  //    sr: Sort[R, SR],
  //    merged: Merge[SL, SR, O])
  //   : Sort[I, O]
  //   = new Sort[I, O] {}

  // This reads as:
  // 
  //     given a Split of the input list into left (L) and right (R)
  //     given an existing instance of Sort[L, SL]
  //     given an existing instance of Sort[R, SR]
  //     given a Merge of SL and SR (which are sorted) into O

  // Это читается как:
  // 
  //      учитывая разделение входного списка на левый (L) и правый (R)
  //      учитывая существующий экземпляр Sort[L, SL]
  //      учитывая существующий экземпляр Sort[R, SR]
  //      учитывая слияние SL и SR (которые отсортированы) в O

  // then the compiler can automatically build an instance of Sort[I, O]. 
  // With a little bit of practice, this reads like natural language, doesn’t it? 
  // Split, sort left, sort right, merge.

  // тогда компилятор может автоматически построить экземпляр Sort[I, O]. 
  // С небольшой практикой это читается как естественный язык, не так ли? 
  // Разделить, отсортировать слева, отсортировать справа, объединить.

  // Let’s stick an apply method there:
  // Прикрепим туда метод apply:  

  // def apply[L <: HList, O <: HList](implicit sorted: Sorted[L, O]) = sorted

  // And we should be free to test!
  // И мы должны быть свободны для тестирования!

  //    val validSort: Sort[
  //      _4 :: _3 :: _5 :: _1 :: _2 :: HNil,
  //      _1 :: _2 :: _3 :: _4 :: _5 :: HNil
  //      ] = Sort.apply

  // This compiles, because the compiler will bend over backwards 
  // to try to find an implicit for every operation we need: 
  // the split of _4 :: _3 :: _5 :: _1 :: _2 :: HNil, 
  // the sorting of the smaller lists - which involve another
  // splitting - and the merge of _2 :: _4 :: _5 :: HNil with _1 :: _3 :: HNil into the final result.
  
  // Это компилируется, потому что компилятор будет из кожи вон лезть, 
  // чтобы попытаться найти неявное для каждой операции, которая нам нужна: 
  // разделение _4::_3::_5::_1::_2::HNil, 
  // сортировка меньших списков — которые влекут за собой еще одно
  // расщепление - и слияние _2::_4::_5::HNil с _1::_3::HNil в окончательный результат.

  // Just plain amazing. The Scala compiler is awesome.
  // Просто потрясающе. Компилятор Scala великолепен.

}
