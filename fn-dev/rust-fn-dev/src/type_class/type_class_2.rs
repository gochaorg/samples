pub trait BoolOp<T:Sized> {
  fn not(arg:T   ) -> T;
  fn and(a:T, b:T) -> T;
  fn or (a:T, b:T) -> T;
}

#[allow(dead_code)]
pub fn my_fun1<T:Sized, B:BoolOp<T>>( a:T, b:T, c:T ) -> T {
  B::or( B::and(a, b), B::not(c) )
}

impl BoolOp<bool> for bool {
  fn not(arg:bool) -> bool { !arg }
  fn and(a:bool, b:bool) -> bool { a && b }
  fn or (a:bool, b:bool) -> bool { a || b }
}

pub struct B;
impl BoolOp<bool> for B {
  fn not(arg:bool) -> bool { !arg }
  fn and(a:bool, b:bool) -> bool { a && b }
  fn or (a:bool, b:bool) -> bool { a || b }
}

impl BoolOp<u8> for B {
  fn not(arg:u8) -> u8 { 
    match arg==0 {
      true => 1,
      false => 0
    }
  }
  fn and(a:u8, b:u8) -> u8 {  
    match (a!=0, b!=0) {
      (true,true) => 1,
      (_,_) => 0
    }
  }
  fn or (a:u8, b:u8) -> u8 { 
    match (a!=0, b!=0) {
      (true,_) => 1,
      (_,true) => 1,
      (_,_) => 0
    }
  }
}

#[test]
fn tc_for_bool() {
  let t = true;
  let f = false;
  let res = my_fun1::<bool,B>( t, t, f );

  println!("{:?}", res);
}

#[test]
fn tc_for_u8() {
  let t = 1u8;
  let f = 0u8;
  let res = my_fun1::<u8,B>( t, t, f );

  println!("{:?}", res);
}

