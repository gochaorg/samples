use std::env;

fn main() {
  for (idx,arg) in env::args().enumerate() {
    println!("args [{idx:2}] \"{arg}\"")
  }
  for (env_var, env_val) in env::vars() {
    println!("env {env_var} = \"{env_val}\"")
  }
}
