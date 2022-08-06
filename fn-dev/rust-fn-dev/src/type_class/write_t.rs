use super::type_class_2;

fn my_fun0<T,F1,F2,F2b>( and:F2, or:F2b, not:F1, a:T, b:T, c:T ) -> T 
where
  T: Clone + Sized,
  F1: Fn(T)->T,
  F2: Fn(T,T)->T,
  F2b: Fn(T,T)->T,
{
  or(and(a,b),not(c))
}

#[test]
fn bool_test() {
  let and = |a,b| a&&b;
  let or = |a,b| a||b;
  let not = |a:bool| !a;
  let res = my_fun0( and , or , not, true, true, true);
  println!("res = {res}");
}

trait Combine 
{
  fn combine( a:&Self, b:&Self ) -> Self;
}

impl Combine for String {
    fn combine( a:&Self, b:&Self ) -> Self {
        let mut s = String::new();
        s.push_str(a);
        s.push_str(b);
        s
    }
}

fn init<A,B>( value:A, name:B ) -> (A,B) 
where
  A: Sized + Clone,
  B: Sized + Clone,
{
  (value.clone(), name.clone())
}

fn named1<A,B:Combine,F1>( f1:F1, name:B ) -> impl Fn((A,B))->(A,B)
where
  F1: Fn(A)->A,
  B: Clone+Sized,
{
  move |a:(A,B)| (f1(a.0), Combine::combine(&a.1, &name) )
}

fn named2<A,B:Combine,F2>( f1:F2, name:B ) -> impl Fn((A,B),(A,B))->(A,B)
where
  F2: Fn(A,A)->A,
  B: Clone+Sized,
{
  move |a:(A,B), b:(A,B)| (f1(a.0, b.0), Combine::combine( &Combine::combine(&a.1, &b.1), &name) )
}

#[test]
fn named_test() {
  let and = |a,b| a&&b;
  let named_and = named2(and, String::from("and"));

  let or = |a,b| a||b;
  let named_or = named2(or, String::from("or"));
  
  let not = |a:bool| !a;
  let named_not = named1(not, String::from("or"));

  let named_true = init(true, String::from("true"));
  let named_false = init(true, String::from("false"));

  let res = my_fun0( named_and , named_or , named_not, named_true.clone(), named_true.clone(), named_false);
  println!("res = {:?}", res);
}

