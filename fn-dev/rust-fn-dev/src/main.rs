use std::marker::PhantomData;

fn main() {
    println!("Hello, world!");
}

trait Parse<FROM: Sized> {
    type TARGET;
    fn parse(&self,from:FROM) -> Self::TARGET;
    fn flatMap<U: Sized, P: Parse<FROM,TARGET = U>>( &self, f: impl Fn(Self::TARGET)->P );
    fn map<U: Sized,P: Parse<FROM,TARGET = U>>( &self, f: impl Fn(Self::TARGET)->U );
}

struct Parser<P,FROM,TARGET> 
where
    P: Fn(FROM) -> (TARGET,FROM)
{
    pub parser : P,
    _p : PhantomData<(FROM,TARGET)>
}

impl<P,FROM,TARGET> Parser<P,FROM,TARGET> 
where
    P: Fn(FROM) -> (TARGET,FROM)
{
    fn new( f:P ) -> Self {
        Self {
            _p : PhantomData.clone(),
            parser: f
        }
    }
}

#[test]
fn monada_test1() {
    println!("monada test 1");
    let sample = "100;blabla;true";
    println!("{:?}",sample.find(";"));

    let parser1 = Parser::new( |x:u8| (2u8, x) );
    //println!("{:?}",sample.;
}