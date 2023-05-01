use super::block::*;
use super::super::bbuff::absbuff::*;
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
  last_block: Option<BlockHeadRead>,
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

    match &self.last_block {
      None => {
        msg.push_str(" last block is none");
      },
      Some(last_block) => {
        msg.push_str(&format!(" last block id={b_id} pos={pos}", pos=last_block.position, b_id=last_block.head.block_id));
      }
    }

    write!(f, "{}", msg )
  }
}

#[allow(dead_code)]
#[derive(Clone, Debug)]
pub enum LogErr {
  Generic(String),
  FlatBuff(ABuffError),
  Block(BlockErr)
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
          last_block: None,
        }
      )
    }

    let block_head_read 
      = Tail::try_read_head_at( buff_size as u64, &buff )?;

    Ok(
      LogFile {
        buff: buff,
        last_block: Some(block_head_read.clone()),
      }
    )
  }

  /// Добавление блока в лог файл
  /// 
  /// # Аргументы
  /// - `block` - добавляемый блок
  #[allow(dead_code)]
  fn append_block( &mut self, block: &Block ) -> Result<(), LogErr> {
    match &self.last_block {
      None => {
        self.append_first_block(block)
      },
      Some(last_block) => {
        self.append_next_block(
          last_block.position.value() + last_block.block_size(), 
          block
        )
      }
    }
  }

  /// Добавление первого блока
  fn append_first_block( &mut self, block:&Block ) -> Result<(), LogErr> {
    self.append_next_block(0, block)
  }

  /// Добавление второго и последующих блоков
  fn append_next_block( &mut self, position:u64, block:&Block ) -> Result<(), LogErr> {
    let writed_block = block.write_to(position, &mut self.buff)?;
    self.last_block = Some( writed_block );
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
  fn read_head_at( &self, position:u64 ) -> Result<BlockHeadRead,LogErr> {    
    let res = BlockHead::read_form(position as usize, &self.buff)?;
    Ok(res)
  }

  fn read_block_at( &self, position:u64 ) -> Result<(Block,u64), LogErr> {
    let res = Block::read_from(position, &self.buff)?;
    Ok(res)
  }

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

  let r0 = log.read_head_at(0).unwrap();
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