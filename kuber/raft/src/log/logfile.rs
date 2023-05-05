use super::block::*;
use super::super::bbuff::absbuff::*;
use std::sync::{Arc, RwLock, PoisonError, RwLockReadGuard};
use std::fmt;

#[cfg_attr(doc, aquamarine::aquamarine)]
#[derive(Clone)]
/// Лог файл
/// 
/// Содержит блоки 
/// 
/// ```mermaid
/// flowchart RL
/// 
/// a[#0]
/// b[#1]
/// c[#2]
/// d[#3]
/// e[#4]
/// f[#5]
/// g[#6]
/// h[#7]
/// 
/// subgraph a[#0]
///   head
/// end
/// 
/// subgraph b[#1]
///   bp0[ref]
/// end
/// 
/// bp0 --> |-1| a
/// 
/// subgraph c[#2]
///   direction LR
///   cp0[ref]
///   cp1[ref]
/// end
/// 
/// cp0 --> |-1| b
/// cp1 --> |-2| a
/// 
/// subgraph d[#3]
///   direction LR
///   dp0[ref]
/// end
/// 
/// dp0 --> c
/// 
/// subgraph e[#4]
///   direction LR
///   ep0[ref]
///   ep1[ref]
///   ep2[ref]
/// end
/// 
/// ep0 --> |-1| d
/// ep1 --> |-2| c
/// ep2 --> |-4| a
/// 
/// subgraph f[#5]
///   direction LR
///   fp0[ref]
/// end
/// 
/// fp0 --> e
/// 
/// subgraph g[#6]
///   direction LR
///   gp0[ref]
///   gp1[ref]
/// end
/// 
/// gp0 --> |-1| f
/// gp1 --> |-2| e
/// 
/// subgraph h[#7]
///   direction LR
///   hp0[ref]
/// end
/// 
/// hp0 --> |-1| g
/// 
/// subgraph i[#8]
///   direction LR
///   ip0[ref]
///   ip1[ref]
///   ip2[ref]
///   ip3[ref]
/// end
/// 
/// ip0 --> |-1| h
/// ip1 --> |-2| g
/// ip2 --> |-4| e
/// ip3 --> |-8| a
/// 
/// ```
pub struct LogFile<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  buff: FlatBuff,
  last_blocks: Box<Vec<BlockHeadRead>>,
}

impl<A> fmt::Display for LogFile<A> 
where A: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    let log_size = self.buff.bytes_count();
    let mut msg = "".to_string();

    msg.push_str(
      &(match log_size {
        Ok(log_size) => { format!("log file size {log_size} bytes") },
        Err(err) => { format!("log file size err:{:?}", err) }
      })
    );

    for (idx, bh) in self.last_blocks.iter().enumerate() {
      msg.push_str(&format!(
        "\nlast block [{idx}] #{b_id} off={off} block_size={block_size} {data_size:?}", 
        b_id=bh.head.block_id,
        off=bh.position,
        data_size=bh.data_size,
        block_size=bh.block_size()
      ));
    }

    write!(f, "{}", msg )
  }
}

#[allow(dead_code)]
#[derive(Clone, Debug)]
pub enum LogErr {
  Generic(String),
  FlatBuff(ABuffError),
  Block(BlockErr),
  LogIsEmpty
}

impl From<ABuffError> for LogErr {
  fn from(value: ABuffError) -> Self {
    LogErr::FlatBuff(value.clone())
  }
}

impl From<BlockErr> for LogErr {
  fn from(value: BlockErr) -> Self {
    LogErr::Block(value.clone())
  }
}

impl<A> From<PoisonError<RwLockReadGuard<'_, A>>> for LogErr {
  fn from(value: PoisonError<RwLockReadGuard<'_, A>>) -> Self {
    LogErr::Generic(format!("can't lock at {}", value.to_string()))
  }
}

/// Реализация 
/// - создания лог файла
/// - Добавление блока в лог файл
impl<FlatBuff> LogFile<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  #[allow(dead_code)]
  pub fn new( buff:FlatBuff ) -> Result<Self, LogErr> {
    let buff_size = buff.bytes_count()?;
    if buff_size == 0 {
      return Ok(
        LogFile {
          buff: buff,
          last_blocks: Box::new(Vec::<BlockHeadRead>::new()),
        }
      )
    }

    let block_head_read 
      = Tail::try_read_head_at( buff_size as u64, &buff )?;

    let mut last_blocks = Box::new(Vec::<BlockHeadRead>::new());
    last_blocks.push(block_head_read.clone());

    Ok(
      LogFile {
        buff: buff,
        last_blocks: last_blocks,
      }
    )
  }

  /// Добавление блока в лог файл
  /// 
  /// # Аргументы
  /// - `block` - добавляемый блок
  #[allow(dead_code)]
  fn append_block( &mut self, block: &Block ) -> Result<(), LogErr> {
    if self.last_blocks.is_empty() {
      self.append_first_block(block)
    }else{
      let last_block = &self.last_blocks[0];
      self.append_next_block( 
        last_block.position.value() + last_block.block_size(),
        block
      )
    }
  }

  /// Добавление первого блока
  fn append_first_block( &mut self, block:&Block ) -> Result<(), LogErr> {
    self.append_next_block(0, block)
  }

  /// Добавление второго и последующих блоков
  /// 
  /// Обновляет/вставляет ссылку на записанный блок в last_blocks[0]
  fn append_next_block( &mut self, position:u64, block:&Block ) -> Result<(), LogErr> {
    let writed_block = block.write_to(position, &mut self.buff)?;

    if self.last_blocks.is_empty() {
      self.last_blocks.push(writed_block)
    }else{
      self.last_blocks[0] = writed_block;
    }

    Ok(())
  }
}

#[test]
fn test_empty_create() {
  let bb = ByteBuff::new_empty_unlimited();
  let log = LogFile::new(bb);
  assert!( log.is_ok() )
}

#[test]
fn test_raw_append_block() {
  let bb = ByteBuff::new_empty_unlimited();

  println!("create log from empty buff");
  let mut log = LogFile::new(bb.clone()).unwrap();

  let b0 = Block {
    head: BlockHead { block_id: BlockId::new(0), data_type_id: DataId::new(0), back_refs: BackRefs::default(), block_options: BlockOptions::default() },
    data: Box::new(vec![0u8, 1, 2])
  };

  let b1 = Block {
    head: BlockHead { block_id: BlockId::new(1), data_type_id: DataId::new(0), back_refs: BackRefs::default(), block_options: BlockOptions::default() },
    data: Box::new(vec![10u8, 11, 12])
  };

  let b2 = Block {
    head: BlockHead { block_id: BlockId::new(2), data_type_id: DataId::new(0), back_refs: BackRefs::default(), block_options: BlockOptions::default() },
    data: Box::new(vec![20u8, 21, 22])
  };

  log.append_block(&b0).unwrap();
  log.append_block(&b1).unwrap();
  log.append_block(&b2).unwrap();

  println!("data len {}", bb.bytes_count().unwrap());
  println!("log {}", log);

  println!("create log from buff with data");
  let log = LogFile::new(bb.clone()).unwrap();
  println!("log {}", log);
}

#[allow(dead_code)]
impl<FlatBuff> LogFile<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  /// Чтение заголовка в указанной позиции
  fn read_head_at<P:Into<FileOffset>>( &self, position:P ) -> Result<BlockHeadRead,LogErr> {    
    let res = BlockHead::read_form(
      position.into(), 
      &self.buff
    )?;
    Ok(res)
  }

  /// Чтение блока в указанной позиции
  /// 
  /// # Аргументы
  /// - `position` - позиция
  /// 
  /// # Результат
  /// ( Блок, позиция следующего блока )
  fn read_block_at<P:Into<FileOffset>>( &self, position:P ) -> Result<(Block,u64), LogErr> {
    let res = Block::read_from(
      position.into().value(), 
      &self.buff
    )?;
    Ok(res)
  }

  /// Получение предшедствующего заголовка перед указанным
  fn read_previous_head( &self, current_block: &BlockHeadRead ) -> Result<Option<BlockHeadRead>, LogErr> {
    let res = Tail::try_read_head_at(current_block.position.value(), &self.buff);
    match res {
      Ok(res) => Ok(Some(res)),
      Err(err) => match err {
        BlockErr::PositionToSmall { min_position: _, actual: _ } => Ok(None),
        BlockErr::TailPointerOuside { pointer: _ } => Ok(None),
        _ => Err(LogErr::from(err))
      }
    }
  }

  /// Получение заголовка следующего блока за указанным
  fn read_next_head( &self, current_block: &BlockHeadRead ) -> Result<Option<BlockHeadRead>, LogErr> {
    let next_ptr = current_block.block_size() + current_block.position.value();
    let buff_size = self.buff.bytes_count()?;
    if next_ptr >= buff_size { return Ok(None) }

    self.read_head_at(next_ptr).map(|r| Some(r))
  }
}

#[test]
fn test_navigation() {
  let bb = ByteBuff::new_empty_unlimited();

  println!("create log from empty buff");
  let mut log = LogFile::new(bb.clone()).unwrap();

  let b0 = Block {
    head: BlockHead { block_id: BlockId::new(0), data_type_id: DataId::new(0), back_refs: BackRefs::default(), block_options: BlockOptions::default() },
    data: Box::new(vec![0u8, 1, 2])
  };

  let b1 = Block {
    head: BlockHead { block_id: BlockId::new(1), data_type_id: DataId::new(0), back_refs: BackRefs::default(), block_options: BlockOptions::default() },
    data: Box::new(vec![10u8, 11, 12])
  };

  let b2 = Block {
    head: BlockHead { block_id: BlockId::new(2), data_type_id: DataId::new(0), back_refs: BackRefs::default(), block_options: BlockOptions::default() },
    data: Box::new(vec![20u8, 21, 22])
  };

  log.append_block(&b0).unwrap();
  log.append_block(&b1).unwrap();
  log.append_block(&b2).unwrap();

  let r0 = log.read_head_at(0u64).unwrap();
  assert!(r0.head.block_id == b0.head.block_id);

  let r1 = log.read_next_head(&r0).unwrap();
  let r1 = r1.unwrap();
  assert!(b1.head.block_id == r1.head.block_id);

  let r2 = log.read_next_head(&r1).unwrap();
  let r2 = r2.unwrap();
  assert!(b2.head.block_id == r2.head.block_id);

  let r3 = log.read_next_head(&r2).unwrap();
  assert!(r3.is_none());

  let rr1 = log.read_previous_head(&r2).unwrap();
  let rr1 = rr1.unwrap();
  assert!( rr1.head.block_id == r1.head.block_id );

  let rm0 = log.read_previous_head(&r0).unwrap();
  assert!(rm0.is_none());

}

impl<FlatBuff> LogFile<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  fn build_next_block(  &mut self, data_id: DataId, data: &[u8] ) -> Block {
    // build BlockData
    let mut block_data = Vec::<u8>::new();
    block_data.resize( data.len(), 0 );
    for i in 0..data.len() { block_data[i] = data[i] }
    let block_data = Box::new(block_data);

    // build BlockOptions
    let block_opt = BlockOptions::default();
    
    if self.last_blocks.is_empty() {
      return Block {
        head: BlockHead { 
          block_id: BlockId::new(0), 
          data_type_id: data_id, 
          back_refs: BackRefs::default(), 
          block_options: block_opt 
        },
        data: block_data
      }
    }

    let last_block = &self.last_blocks[0];
    let block_id = BlockId::new( last_block.head.block_id.value() + 1 );

    let mut update_ref = |ref_idx:usize| {
      if ref_idx >= self.last_blocks.len() {
        while ref_idx >= self.last_blocks.len() {
          match self.last_blocks.last() {
            Some(last) => {
              self.last_blocks.push(last.clone())
            },
            None => {}
          }
        }
      } else {
        self.last_blocks[ref_idx] = self.last_blocks[ref_idx-1].clone()
      }
    };

    for i in 1..32 {
      let level = 32 - i;
      let n = u32::pow(2, level);
      let idx = level;
      if block_id.value() % n == 0 { 
        update_ref(idx as usize) 
      }
    }

    let back_refs:Vec<(BlockId,FileOffset)> = self.last_blocks.iter()
      .map( |bhr| 
        (bhr.head.block_id.clone(), bhr.position.clone()) )
      .collect()
      ;

    let back_refs = Box::new(back_refs);

    Block {
      head: BlockHead { 
        block_id: block_id, 
        data_type_id: data_id, 
        back_refs: BackRefs { refs: back_refs }, 
        block_options: block_opt 
      },
      data: block_data
    }
  }

  /// Добавление данных в лог
  fn append_data( &mut self, data_id: DataId, data: &[u8] ) -> Result<(), LogErr> {
    let block = self.build_next_block(data_id, data);
    self.append_block( &block )
  }
}

trait GetPointer<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  /// Создания указателя на последний добавленый блок
  fn pointer_to_end( self ) -> Result<LogPointer<FlatBuff>, LogErr>;
}

impl<FlatBuff> GetPointer<FlatBuff> for Arc<RwLock<LogFile<FlatBuff>>> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  fn pointer_to_end( self ) -> Result<LogPointer<FlatBuff>, LogErr> {
    let lock = self.read()?;
    if lock.last_blocks.is_empty() {
      return Err(LogErr::LogIsEmpty)
    }

    let last_block = &lock.last_blocks[0];
    Ok(LogPointer { log_file: self.clone(), current_block: last_block.clone() })
  }
}

/// Указатель на блок
#[derive(Clone)]
pub struct LogPointer<FlatBuff>
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  log_file: Arc<RwLock<LogFile<FlatBuff>>>,
  current_block: BlockHeadRead
}

impl<FlatBuff> LogPointer<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  /// Возвращает заголовок текущего блока
  pub fn current_head<'a>( &'a self ) -> &'a BlockHeadRead {
    &self.current_block
  }

  /// Возвращает данные текущего блока
  pub fn current_data( &self ) -> Result<Box<Vec<u8>>, LogErr> {
    let (block,_) = self.log_file.read()?.read_block_at( self.current_block.position.value() )?;
    Ok(block.data)
  }

  /// Возвращает указатель на предыдущий блок
  pub fn previous( &self ) -> Result<Self,LogErr> {
    let prev = self.log_file.read()?.read_previous_head(&self.current_block)?;
    match prev {
      Some(b) => {
        Ok( Self { log_file: self.log_file.clone(), current_block: b } )
      },
      None => {
        Err(LogErr::Generic(format!("can' move back")))
      }
    }
  }

  /// Возвращает указатель на следующий блок
  pub fn next( &self ) -> Result<Self,LogErr> {
    let next = self.log_file.read()?.read_next_head(&self.current_block)?;
    match next {
      Some(b) => {
        Ok( Self { log_file: self.log_file.clone(), current_block: b } )
      },
      None => {
        Err(LogErr::Generic(format!("can' move forward")))
      }
    }
  }

  /// Прыжок назад к определенному блоку
  fn jump_back( &self, block_id:BlockId ) -> Result<Self,LogErr> {
    // Указываем на себя ?
    if self.current_head().head.block_id.value() == block_id.value() {
      return Ok( self.clone() )
    }

    // Указываем прыжок в перед ?
    if self.current_head().head.block_id.value() < block_id.value() {
      return Err(LogErr::Generic(format!("can' jump forward")))
    }
    
    let back_refs = self.current_head().head.back_refs.refs.clone();

    // Обратных ссылок нет ?
    if back_refs.is_empty() {
      let prev = self.previous()?;
      return prev.jump_back(block_id)
    }

    let found
      = back_refs.iter().zip(back_refs.iter().skip(1))
      .filter(|((a_id,a_off),(b_id,b_off))| 
        {
          let (a_id, a_off, b_id, b_off) = 
          if b_id < a_id {
            (b_id, b_off, a_id, a_off)
          } else {
            (a_id, a_off, b_id, b_off)
          };
          *a_id < block_id && block_id <= *b_id
        }
    ).map(|((a_id,a_off),(b_id,b_off))| 
      FileOffset::new(a_off.value().max(b_off.value()))
    ).next();

    // Нашли в приемлемом диапазоне ?
    if found.is_some() {
      let block_head = self.log_file.read()?.read_head_at(found.unwrap())?;
      let ptr = Self {
        log_file: self.log_file.clone(), current_block: block_head
      };
      return ptr.jump_back(block_id);
    }

    let (b_id,b_off) = back_refs[0].clone();

    // Первый блок может указывает ?
    if block_id.value() <= b_id.value() {
      let block_head = self.log_file.read()?.read_head_at(b_off)?;
      let ptr = Self {
        log_file: self.log_file.clone(), current_block: block_head
      };
      return ptr.jump_back(block_id);
    }

    // Последняя попытка
    let prev = self.previous()?;
    return prev.jump_back(block_id)
}
}

#[test]
fn test_pointer() {
  let bb = ByteBuff::new_empty_unlimited();
  let log = Arc::new(RwLock::new(LogFile::new(bb).unwrap()));

  {
    let mut log = log.write().unwrap();

    for n in 0u8 .. 130 {
      log.append_data(DataId::new(0), &[n,n+1,n+2]).unwrap();
    }
  }

  let mut ptr = log.clone().pointer_to_end().unwrap();
  loop {
    let block_head = ptr.current_head();
    // pointer to BlockId(16) : Ok([16, 17, 18]) FileOffset(954)
    print!("pointer to #{b_id:<6} : {data:<18}", 
      b_id=block_head.head.block_id.value(), 
      data=format!("{:?}",ptr.current_data())
    );

    print!(" back ref");
    for (idx, (b_id, b_off)) in block_head.head.back_refs.refs.iter().enumerate() {
      print!(" [{idx:0>2}] #{b_id:<4} off={b_off:<6}",
        b_id  = b_id.value(),
        b_off = b_off.value()
      )
    }
    println!("");

    match ptr.previous() {
      Err(_err) => break,
      Ok(next_ptr) => {
        ptr = next_ptr
      }
    }
  }
  
  ptr = log.clone().pointer_to_end().unwrap();
  let ptr1 = ptr.jump_back(
    ptr.current_head().head.block_id
  ).unwrap();
  assert!(ptr.current_head().head.block_id == ptr1.current_head().head.block_id);

  let ptr1 = ptr.jump_back(BlockId::new(9)).unwrap();

}