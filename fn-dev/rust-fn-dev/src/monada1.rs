const SEPARATOR: &str = ";";
const SEPARATOR_BYTES: usize = SEPARATOR.len();

/// Парсинг CSV строки
/// # Аргументы
/// * `csv_live` - строка со значениями разделенных точ.запятой, пример: `value_a;value_b;value_c`
/// # Результат
/// `( значение, остаток )` - пример: `("value_a", "value_b;value_c")`
#[allow(unused)]
fn parse_field( csv_line : &str ) -> (&str, &str) {
    match csv_line.find(SEPARATOR) {
        Some(sep_at) => (
            &csv_line[..sep_at],
            &csv_line[(sep_at+SEPARATOR_BYTES)..]
        ),
        _ => (csv_line,"")
    }
}

#[test]
fn parse_field_test() {
    let r = parse_field("value_a;value_b;value_c");
    println!("{:?}", r);
    assert!(r.0 == "value_a");
    assert!(r.1 == "value_b;value_c");
}

/// Склейка двух монад в последовательность.
/// Вычисления первой монады передаются во вторую
/// # Аргументы
/// * `monada` - первая монада
/// * `mapper` - функция вычисления второй монады, после получения результата первой
/// # Результат
/// Новая монада
#[allow(unused)]
fn flat_map<Monada,MonadaNext,Src,Res,MappedRes>
    ( monada:Monada, mapper:impl Fn(Res)->MonadaNext ) -> impl Fn(Src)->(MappedRes,Src) 
where
    Monada: Fn(Src) -> (Res,Src),
    MonadaNext: Fn(Src) -> (MappedRes,Src),
{
    move | src | {
        let (res, next_src ) = monada(src);
        (mapper(res))(next_src)
    }
}

#[test]
fn flat_map_test1() {
    let parser1 = flat_map(
        parse_field,
        move |_r1| flat_map(
            parse_field,
            move |_r2| {
                move |src| ((_r1,_r2),src)
            }
        )
    );

    let src = "value_a;value_b;value_c";
    let res = parser1(src);
    println!("{:?}", res);
}

/// Ковертация результатов вычисления монады
/// # Аргументы
/// * `monada` - первая монада
/// * `mapper` - конвертация итогового результата
/// # Результат
/// Новая монада
#[allow(unused)]
fn result_map<Monada,Src,Res,MappedRes>
    ( monada:Monada, mapper:impl Fn(Res)->MappedRes ) -> impl Fn(Src)->(MappedRes,Src)
where
    Monada: Fn(Src) -> (Res,Src),    
{
    move | src | {
        let (res,next_src) = monada(src);
        let res = mapper(res);
        (res,next_src)
    }
}

trait ToInt {
    fn to_int(self) -> i32;
}

impl ToInt for &str {
    fn to_int(self) -> i32 {
        fn digit_of( c:char )->Option<i32> {
            match c {
                '0' => Some(0i32), '1' => Some(1i32), '2' => Some(2i32),
                '3' => Some(3i32), '4' => Some(4i32), '5' => Some(5i32),
                '6' => Some(6i32), '7' => Some(7i32), '8' => Some(8i32),
                '9' => Some(9i32), 
                _ => None
            }
        }
        let chrs_co = self.chars().count();
        let chrs = self.char_indices().into_iter().map(
            |(idx,c)| (idx as i32,digit_of(c).unwrap())
        );
        let nums = chrs.map(
            |(idx,digt)| 
                i32::pow(10, ((chrs_co as i32) - idx - 1) as u32 ) * digt
        );
        
        nums.fold(0i32, |a,b| a+b)
    }
}

#[test]
fn to_int_test() {
    println!("{}", "123".to_int());
}

trait ToBool {
    fn to_bool(self) -> bool;
}

impl ToBool for &str {
    fn to_bool(self) -> bool {
        self.to_lowercase() == "true" 
    }
}

#[test]
fn flat_map_test2() {    
    let parser1 = flat_map(
        result_map(parse_field, move |r| r.to_int()),
        move |_r1| result_map(
            parse_field,
            move |_r2| (_r1,_r2)
        )
    );

    let src = "123;true;value_c";
    let res = parser1(src);
    println!("{:?}", res);
}


#[test]
fn flat_map_test3() {    
    let int_parser = &result_map(parse_field, move |r| r.to_int());
    let bool_parser = &result_map(parse_field, move |r| r.to_bool());

    let parser1 = flat_map(
        int_parser,
        move |int_fld| flat_map( 
            bool_parser, 
            move |bool_fld| result_map(
                parse_field,
                move |str_fld| (int_fld,bool_fld,str_fld)
            )
        )
    );

    let src = "123;true;value_c";
    let res = parser1(src);
    println!("{:?}", res);
}
