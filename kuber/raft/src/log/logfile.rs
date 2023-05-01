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
  last_block_id: Option<BlockId>,
  last_block_begin: Option<FileOffset>,
  last_block_size: Option<u64>,
}

impl<A> fmt::Display for LogFile<A> 
where A: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    write!(f, 
      "last_block_id: {last_block_id:?} last_block_begin: {last_block_begin:?} last_block_size: {last_block_size:?}",
      last_block_id = self.last_block_id,
      last_block_begin = self.last_block_begin,
      last_block_size = self.last_block_size
    )
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
#[allow(dead_code)]
impl<FlatBuff> LogFile<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  pub fn new( buff:FlatBuff ) -> Result<Self, LogErr> {
    let buff_size = buff.bytes_count()?;
    if buff_size == 0 {
      return Ok(
        LogFile {
          buff: buff,
          last_block_id: None,
          last_block_begin: None,
          last_block_size: None,
        }
      )
    }

    if buff_size > (u64::MAX as usize) {
      return Err(
        LogErr::Generic(format!("buff to big"))
      );
    }

    let block_head_raw 
      = Tail::try_read_head_at( buff_size as u64, &buff )?;

    let block_size = block_head_raw.clone().block_size();

    let (head, _, _, _) = block_head_raw;

    Ok(
      LogFile {
        buff: buff,
        last_block_id: Some(head.block_id),
        last_block_begin: Some(FileOffset::new( (buff_size as u64) - block_size) ),
        last_block_size: Some(block_size),
      }
    )
  }

  /// Добавление блока в лог файл
  /// 
  /// # Аргументы
  /// - `block` - добавляемый блок
  fn append_block( &mut self, block: &Block ) -> Result<(), LogErr> {
    match self.last_block_id {
      None => {
        self.append_first_block(block)
      },
      Some(_last_block_id) => {
        match self.last_block_begin {
          None => { return Err(LogErr::Generic(format!("internal state error, at {}:{}", file!(), line!() ))) },
          Some( last_block_offset ) => {
            match self.last_block_size {
              None => { return Err(LogErr::Generic(format!("internal state error, at {}:{}", file!(), line!() ))) },
              Some( last_block_size ) => {
                self.append_next_block(last_block_offset.value() + last_block_size, block)
              }
            }
          }
        }
      }
    }
  }

  /// Добавление первого блока
  fn append_first_block( &mut self, block:&Block ) -> Result<(), LogErr> {
    self.append_next_block(0u64, block)
  }

  /// Добавление второго и последующих блоков
  fn append_next_block( &mut self, position:u64, block:&Block ) -> Result<(), LogErr> {
    let block_size = block.write_to(position, &mut self.buff)?;
    self.last_block_id = Some(block.head.block_id);
    self.last_block_begin = Some(FileOffset::new(position));
    self.last_block_size = Some(block_size as u64);

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
  fn read_block_head_at( &self, position:u64 ) -> Result<(BlockHead, BlockHeadSize, BlockDataSize, BlockTailSize),LogErr> {    
    let res = BlockHead::read_form(position as usize, &self.buff)?;
    Ok(res)
  }

  fn read_block_at( &self, position:u64 ) -> Result<(Block,u64), LogErr> {
    let res = Block::read_from(position, &self.buff)?;
    Ok(res)
  }
}