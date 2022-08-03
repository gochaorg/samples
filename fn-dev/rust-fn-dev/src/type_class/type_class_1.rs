trait BoolOp<T:Sized> {
  fn true_value (&self)  -> T;
  fn false_value(&self) -> T;

  fn not(&self, arg:T) -> T;
  fn and(&self, a:T, b:T) -> T;
  fn or (&self, a:T, b:T) -> T;
}

#[allow(dead_code)]
fn my_fun1<T:Sized, B:BoolOp<T>>( op:&B, a:T, b:T, c:T ) -> T {
  op.or( op.and(a, b), op.not(c) )
}

impl BoolOp<bool> for bool {
  fn true_value (&self)  -> bool { true }
  fn false_value(&self) -> bool { false }
  fn not(&self, arg:bool) -> bool { !arg }
  fn and(&self, a:bool, b:bool) -> bool { a && b }
  fn or (&self, a:bool, b:bool) -> bool { a || b }
}

#[test]
fn tc_for_bool() {
  let b = true;
  b.true_value();

  let t = b.true_value();
  let f = b.false_value();
  let res = my_fun1(&b, t, t, f);

  println!("{:?}", res);
}