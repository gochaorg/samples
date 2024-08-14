Языки рассматриваются на нескольких уровнях:

1. **Прагматика** \- в контексте языков программирования \- это, то как программист использует язык. Ну например я на 1С буду писать драйвера.  
2. **Семантика** \- это, о том, какие сущности есть в программе, и как они между собой взаимодействуют. Например для какого нибудь бухгалтера это будет такие типы данных как Кредит, Дебит, Отчетный период, и тд.  
3. **Синтаксис \-** Это о том, по каким правилам описывается текст программы.Синтаксис, ~~Грамматика~~, Лексика- входят в один и тот же уровень.

Грамматика - более широкое понятие которое по сути включает все перечисленное и не только.

В данном тексте постараюсь не использовать слово Грамматика.

5. **Морфология,Фонетика** \- эти области о корнях слов (морфемы) и звучании (фонетика)- в контексте языков программирования, эти понятия практически не рассматриваются

# Синтаксис и Интерпретация

## Задача \-(Интро) Мат. выражения

Возьмем задачу калькулятора, оставим только два уровня: семантику и синтаксис.

Задача (глобально) может звучать так: Надо вычислять числовые значения которые заданы строкой, где текст содержит математические выражения.

Условно необходимо реализовать функцию следующей сигнатуры:

    fn eval (code: String) → Number

Вот, так условно должна работать функция:

    assert eval("1+2×3/2-2") == 2

_Оффтоп: задача по настоящему поставлена не полностью, но об этом позже_

## Подходы к решению

Необходимо определиться с терминами, с семантикой задачи и с синтаксисом.
Эта часть будет раскрываться по мере решения.

Начнем с семантики, такие примерно должны возникнуть вопросы у программиста:

(Привет шиза, или действия в двух лицах, если вы четко помните все правила арифметики то, пропускайте)

> Что за математические выражения?

обычные операции над числами: сложение, вычитание, умножение, деление

> Примеры операций?

- 5 \+ 2 \= 7  
- 1 \* 0 \= 0  
- 3 \- 4 \= \-1  
- 10/4 \= 2.5

> Что из представленного является числом, а что операцией?

- Числом являются символы: 5,2,7,1,0,4,3, 10, \-1, 2.5  
- Операциями называется символы: \+,-,\*, /

> Что обозначает символ "="?

- Выражает результат вычисления выражения стоящего перед этим символом, а результат после этого символа  
- Правильно назвать это операцией редукции → иначе говоря вычисление в смысле школьной арифметики

>Перечислены все операции?

- Нет, есть еще унарные операции

> Что за унарные операции, какого вида бывают еще операции?

- Операции могут быть двух видов:  
- унарные, которые принимают один аргумент  
  - Например: "- 1""-" \- операция, унарный минус"1"-аргумент операции  
  - Операция унарного минуса равнозначна вычитанию из нуля аргумента: **\-X** \= **0-Х**  
  - Например: "+2". унарный плюс  
- Бинарные операции \- операции с двумя аргументами

> Числа являются рациональными? Достаточно ли использовать числа с плавающей запятой, 64 битные согласно стандарту IEEE 754?

- Да

> Бинарные и Унарные операции работают только с числами?

- С числами и результатом вычислений  
- Результат вычислений \- это тот случай когда задано несколько операций, то результат вычисления одной операции используется как аргумент другой операции:  
  - 1×2×**(3+4)**  
  - Так, сначала должно быть вычислено значение в скобках, результат вычисления должен быть подставлен вместо скобок включительно:  
  - 1×2×7  
  - А затем вычисления в порядке правил арифметики

> Скобки определяют порядок вычислений? Скобки это особая операция?

- В некотором смысле скобки \- это унарная операция, в качестве аргумента используется математическое выражение  
- Скобки определяют порядок вычислений.  
- Запись 1×2×(3+4) является сокращенной, в том смысле что ряд скобок опущено, полная запись должна выглядеть так: ((1\*2) × (3+4))  
- Вычисления начинаются с внутренних скобок  
- Операции умножения и деления являются более приоритетными

> Необходимо пояснение о приоритетности операций

- Если встречается запись вида 1 **?** 2 **?** 3, где ?- некая операция, тогда возможно следующие варианты расстановки скобок  
- ((1 ? 2\) ? 3\)  
- (1 ? (2 ? 3))  
- В случае если первая операция \+, вторая ×, тогда операция ✗ должна быть вычислена первой, т. е. возможно только одна интерпретация  
  - (1+(2*3))  
- Аналогично, если первая ×, a вторая операция \+, тогда все равно возможна только одна интерпретация \- сначала умножение, деление, потом сложение и вычитание:  
- ((1×2)+3)

> Играют какую либо роль пробелы?

- Нет, можно игнорировать

> Как поступать в случае деления на ноль?

- Результатом должно быть специальное значение, которое бы указывало на недопустимость операции

> Как поступать в случае операций с таким значением?

- Операции с таким значением допускаются, но их результат является это же самое значение

# Семантика

Подведем из диалога итоги

Числа могут быть двух форм

- 64 битное, с плавающей запятой, стандарт IEEE 754, ему соответствуют типы: double в таких языках как Java и С/ C++  
- Специальное значение которое обозначает ошибку (деление на ноль)

Для этого можно кодировать такое число двумя переменными:

- **hasValue: boolean,** может принимать 2 значения  
  - false \- означает,что число является ошибкой  
  - true- означает, что число содержит актуальные данные/значение  
- **value: double** \- число, актуальное значение

Для удобства операций с такими числами, мы можем определить специальный тип данных:

```java
class Num {
  boolean hasValue;
  double value;
}
```

```rust
struct Num {
  hasValue: bool;
  value: f64;
}
```

Необходимо несколько базовых функций для работы с этим типом

- преобразование double → Num  
- проверка какого данные содержит Num и извлечение  
  - проверка наличия ошибки Num → boolean  
  - Num → double извлечение значения

Пояснение: функция тут указана как стрелка в право, слева типа аргументов, справа тип результата

Если посмотреть на извлечение данных, то там присутствует две функции, которые можно заменить на одну:

    Num → Optional<double>

Пояснение: **OptionаI\<double\>** - это такой тип - контейнер, для опционального значения и имеет две связанные с ним функции

- Optional\<double\> → boolean \- проверка наличия значения (double)В случае Java это метод boolean isPresent () класса Optional  
- Optional\<double\> → double для извлечения значения в случае Java это метод get() класса Optional

Суть замены в том, что многие средства контроля исходного кода проверяют этот тип данных, как он используется в коде

И запрещают код который используют **Optional\<double\> → double,** без предварительной проверки: Optional \< double \> → **boolean**

То есть вот такой код Java допустим

    Optional<double> value = ...
    if (value.isPresent()) {
        value. get()...
    }

С числами определились

## **Операции над числами**

Над числами возможно 4 операции:

1. \+ Num, Num → Num  
2. \- Num, Num → Num  
3. \* Num, Num → Num  
4. / Num, Num → Num

На языке Java это будет примерно так:

class Num {  
boolean hasValue;  
double value;

Num plus(Num а) {...}  
Num minus(Num а) {...}  
Num multiply(Num а) {...}  
Num division(Num а) {...}  
}

И еще две унарных операции

5. \+ Num → Num  
6. \- Num → Num

С учетом Optional и преобразования из double → Num, конечный класс должен выглядеть так:

```java
class Num {
    private boolean hasValue;
    private double value;

    public Num(double value) {
        hasValue = true;
        this.value = value;
    }

    public Optional<Double> value() {...}
    
    public Num plus(Num а) {...}  
    public Num minus(Num а) {...}  
    public Num multiply(Num а) {...}  
    public Num division(Num а) {...}
    
    public Num plus( ) {...}  
    public Num minus( ) {...}  
}
```

Тут надо чуть расшифровать поведение, по скольку Num может быть в двух состояниях: hasValue = true/false, то обозначим эти два состояния двумя условными типами:

- Num.t (hasValue=true)  
- Num.f (hasValue=false)

В конечном итоге семантика расписывается в такой конечный список Функции, их сигнатур/типов

- Помещение значения в Num: double → Num.t  
- извлечение значения: Num.t → double  
- общий тип: Num. t → Num  
- проверка какого типа, есть или нет значение: Num → boolean  
- сложение, вычитание, умножение, деление  
  - Num.t, Num.t → Num.t  
  - Num.t, **Num.f** → **Num.f**  
  - **Num.f,** Num.t → **Num.f**  
  - **Num.f, Num.f** → **Num.f**  
  - Num.t → Num.t (унарные операции)  
  - Num.f → Num.f (унарные операции)