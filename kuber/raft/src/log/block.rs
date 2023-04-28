//! Представляет из себя блок лог файла
//! 
//! Размер блока может быть разным
//! 
//! Структура блока
//! 
//! | Поле | Тип | Описание |
//! |------|-----|----------|
//! | head | Head | Заголовок блока |
//! | data | Data | Данные блока |
//! | tail | Tail | Хвост блока - хранит маркер конца блока, который указывает на заголовок |
//! 
//! Структура заголовка
//! 
//! | Поле                 | Тип/размер   | Описание |
//! |----------------------|--------------|-----------|
//! | head                 | head         | Заголовок |
//! | head.head_size       | u32          | Размер заголовка |
//! | head.data_size       | u32          | Размер данных |
//! | head.tail_size       | u16          | Размер хвоста |
//! | head.block_id        | BlockId(u32) | Идентификатор блока |
//! | head.data_type_id    | DataId       | Тип данных |
//! | head.back_refs_count | u32          | Кол-во обратных ссылок (head.back_ref) |
//! | head.back_ref.b_id   | u32          | Идентификатор блока |
//! | head.back_ref.b_off  | u64          | Смещение блока |
//! | head.block_options   | BlockOptions | Опции блока    |
use crate::bbuff::{streambuff::{ByteBuff, ByteReader, ByteWriter}, absbuff::ABuffError};
use crate::bbuff::absbuff::{ ReadBytesFrom, WriteBytesTo };

/// Блок лога
#[allow(dead_code)]
pub struct Block {
  /// Заголовок
  pub head: BlockHead,

  /// Данные
  pub data: Box<Vec<u8>>
}

// Макрос для NewType - генерация ByteWriter, ByteReader
macro_rules! bytes_rw_new_type {
  ( $( #[$meta:meta] )*
    $vis:vis struct $struct_name:ident 
    (
      $field_type:ty
    )
  ) => {
    $(#[$meta])*
    pub struct $struct_name ( $field_type );

    impl ByteWriter<$struct_name> for ByteBuff {
      fn write( &mut self, v:$struct_name ) {
        self.write( v.0 )
      }
    }

    impl ByteReader<$struct_name> for ByteBuff {
      fn read( &mut self, target:&mut $struct_name ) -> Result<(),String> {    
        self.read(&mut target.0)
      }
    }
  };
}

bytes_rw_new_type!(
  #[derive(Copy, Clone, Debug, Default, PartialEq)]
  struct BlockId(u32)
);
bytes_rw_new_type!(
  #[derive(Copy, Clone, Debug, Default, PartialEq, PartialOrd)]
  struct FileOffset(u64)
);
bytes_rw_new_type!(
  #[derive(Copy, Clone, Debug, Default, PartialEq)]
  struct DataId(u32)
);

/// Опции блока
#[derive(Copy, Clone, Debug)]
pub struct BlockOptions {
}

impl Default for BlockOptions {
  fn default() -> Self {
    BlockOptions {
    }
  }
}

impl ByteWriter<BlockOptions> for ByteBuff {
  fn write( &mut self, _v:BlockOptions ) {

  }
}
impl ByteReader<BlockOptions> for ByteBuff {
  fn read( &mut self, _target:&mut BlockOptions ) -> Result<(),String> {
    Ok(())
  }
}

/// Заголовок блока
#[derive(Clone,Debug, Default)]
pub struct BlockHead {
  /// Идентификатор блока
  pub block_id: BlockId, 

  /// Идентификатор типа данных 
  pub data_type_id: DataId,

  /// Ссылки на предшествующий блоки
  pub back_refs: BackRefs,

  /// Опции блока
  pub block_options: BlockOptions,
}

/// Данные блока
#[derive(Clone, Debug)]
pub struct BlockData {
  pub data: Box<Vec<u8>>
}

/// Ссылка на предыдущий блок
#[derive(Clone, Debug)]
pub struct BackRefs {
  pub refs: Box<Vec<(BlockId, FileOffset)>>
}
impl Default for BackRefs {
  fn default() -> Self {
    Self {
      refs: Box::new(vec![])
    }
  }
}

/// минимальный размер заголовка
#[allow(dead_code)]
pub const HEAD_MIN_SIZE : u32 = 22;

#[derive(Debug,Clone, Copy)]
pub struct BlockHeadSize(u32);

#[derive(Debug,Clone, Copy)]
pub struct BlockDataSize(u32);

#[derive(Debug,Clone, Copy)]
pub struct BlockTailSize(u16);

/// Чтение заголовка
#[allow(dead_code)]
fn read_block_head( data: Box<Vec<u8>> ) -> Result<(BlockHead, BlockHeadSize, BlockDataSize, BlockTailSize), String> {  

  let mut head_size: u32 = 0;
  let mut data_size: u32 = 0;
  let mut tail_size: u16 = 0;

  let mut bh = BlockHead::default();
  let mut back_refs_count: u32 = 0;

  let mut bbuf = ByteBuff::from( data );

  bbuf.read(&mut head_size)?;
  if head_size < HEAD_MIN_SIZE {
    return Err("head to small".to_string());
  }

  bbuf.read(&mut data_size)?;
  bbuf.read(&mut tail_size)?;

  bbuf.read(&mut bh.block_id)?;
  bbuf.read(&mut bh.data_type_id)?;
  bbuf.read(&mut back_refs_count)?;

  for _ in 0..back_refs_count {
    let mut b_id = BlockId::default();
    let mut b_off = FileOffset::default();
    bbuf.read(&mut b_id)?;
    bbuf.read(&mut b_off)?;
    
    bh.back_refs.refs.push( (b_id, b_off) );
  }

  let head_opt_size = (head_size as i64) - (bbuf.position as i64);
  if head_opt_size > 0 {
    bbuf.read(&mut bh.block_options)?;
  }

  Ok((bh, BlockHeadSize(head_size), BlockDataSize(data_size), BlockTailSize(tail_size)))
}

#[allow(dead_code)]
fn write_block_head( head:BlockHead, data_size:u32, tail_size:u16 ) -> Box<Vec<u8>> {
  let mut bbuf = ByteBuff::new();

  bbuf.position = 0;
  bbuf.write(0u32);
  bbuf.write(data_size);
  bbuf.write(tail_size);
  bbuf.write(head.block_id);
  bbuf.write(head.data_type_id);
  bbuf.write(head.back_refs.refs.len() as u32);
  for (b_id, b_off) in head.back_refs.refs.iter() {
    bbuf.write(*b_id);
    bbuf.write(*b_off);
  }

  bbuf.write(head.block_options);

  let size = bbuf.position;  
  bbuf.position = 0;
  bbuf.write(size as u32);

  bbuf.buff
}

#[test]
fn test_block() {
  let bh = BlockHead {
    block_id: BlockId(10),
    data_type_id: DataId(2),
    back_refs: BackRefs { refs: Box::new(vec![
      (BlockId(9), FileOffset(7)),
      (BlockId(8), FileOffset(20)),
    ])},
    block_options: BlockOptions::default()
  };

  let block_data = write_block_head(bh, 134, 0);
  println!("{:?}",read_block_head(block_data));
}

#[allow(dead_code)]
const TAIL_MARKER : &str = "TAIL";

impl Block {
  /// Формирование массива байтов представлющих блок
  #[allow(dead_code)]
  pub fn to_bytes( &self ) -> Box<Vec<u8>> {
    let mut tail = Box::new(Vec::<u8>::new());
    for i in 0..TAIL_MARKER.len() {
      tail.push(TAIL_MARKER.as_bytes()[i]);
    }

    tail.push(0);tail.push(0);tail.push(0);tail.push(0);

    let mut bytes = write_block_head(self.head.clone(), self.data.len() as u32, tail.len() as u16);

    let off = bytes.len();
    if self.data.len()>0 {      
      bytes.resize(bytes.len() + self.data.len() + tail.len(), 0)
    }

    for i in 0..(self.data.len()) {
      bytes[off + i] = self.data[i]
    }

    let total_size = (bytes.len() + tail.len()) as u32;
    let total_size = total_size.to_le_bytes();
    tail[4] = total_size[0];
    tail[5] = total_size[1];
    tail[6] = total_size[2];
    tail[7] = total_size[3];

    let blen = bytes.len();
    for i in 0..tail.len() {
      bytes[ blen - tail.len() + i ] = tail[i];
    }

    bytes
  }
}

impl BlockHead {
  /// Чтение заголовка
  #[allow(dead_code)]
  pub fn from_bytes( bytes: Box<Vec<u8>> ) -> Result<(BlockHead, BlockHeadSize, BlockDataSize, BlockTailSize), String> {
    read_block_head(bytes)
  }

  /// Формирование байтового представления
  #[allow(dead_code)]
  pub fn to_bytes( &self, data_size:u32 ) -> Box<Vec<u8>> {
    write_block_head( self.clone(), data_size, 0)
  }
}

/// Ошибка при операциях с блоком лога
#[derive(Debug,Clone)]
pub struct BlockErr( pub String );
impl From<std::io::Error> for BlockErr {
  fn from(value: std::io::Error) -> Self {
    Self(value.to_string())
  }
}
impl From<String> for BlockErr {
  fn from(value: String) -> Self {
    Self(value)
  }
}
impl From<&str> for BlockErr {
  fn from(value: &str) -> Self {
    Self(value.to_string())
  }
}
impl From<ABuffError> for BlockErr {
  fn from(value: ABuffError) -> Self {
    Self(value.0)
  }
}

#[allow(dead_code)]
const READ_BUFF_SIZE: usize = 1024*8;

impl Block {
  /// Чтение блока из массива байт
  #[allow(dead_code)]
  pub fn read_from<Source>( position: u64, file: &Source ) -> Result<(Self,u64), BlockErr> 
  where Source : ReadBytesFrom
  {
    const PREVIEW_SIZE:usize = 1024 * 64;
    let mut head_preview: [u8;PREVIEW_SIZE] = [0; PREVIEW_SIZE];

    let reads = file.read_from(position as usize, &mut head_preview)?;
    if reads < (HEAD_MIN_SIZE as usize) { return Err(BlockErr::from("readed to small header")) }

    let (bh, head_size, data_size, tail_size) = BlockHead::from_bytes(Box::new(head_preview.to_vec()))?;
    let mut buff: [u8;READ_BUFF_SIZE] = [0;READ_BUFF_SIZE];
    let mut left_bytes = data_size.0;

    let mut block_data = Vec::<u8>::new();
    block_data.resize(left_bytes as usize, 0);

    let mut block_data_ptr = 0usize;
    let mut file_pos = (position as usize) + (head_size.0 as usize);

    while left_bytes>0 {
      let readed = file.read_from( file_pos,&mut buff)?;
      if readed==0usize { return Err(BlockErr::from("data block truncated")) }

      for i in 0..(readed.min(left_bytes as usize)) {
        block_data[block_data_ptr] = buff[i];
        block_data_ptr += 1;
        file_pos += 1;
        left_bytes -= 1;
      }
    }

    Ok((Self{head: bh, data:Box::new(block_data)}, position + (head_size.0 as u64) + (data_size.0 as u64) + (tail_size.0 as u64)))
  }

  /// Запись блока в массив байтов
  #[allow(dead_code)]
  pub fn write_to<Destination>( &self, position:u64, dest:&mut Destination ) -> Result<(),BlockErr> 
  where Destination: WriteBytesTo
  {
    let bytes = self.to_bytes();
    dest.write_to( position as usize, &bytes[0 .. bytes.len()])?;
    Ok(())
  }
}

#[test]
fn test_block_rw(){
  use super::super::bbuff::absbuff::ByteBuff;

  let mut bb = ByteBuff {
    data: Box::new(Vec::<u8>::new()),
    resizeable: true,
    max_size: None
  };

  let block = Block {
    head: BlockHead { block_id: BlockId(0), data_type_id: DataId(1), back_refs: BackRefs::default(), block_options: BlockOptions::default() },
    data: Box::new( vec![1,2,3] )
  };

  block.write_to(0, &mut bb).unwrap();
  println!("{block_size}", block_size=bb.data.len() );

  let (rblock,_) = Block::read_from(0, &bb).unwrap();
  assert!( rblock.head.block_id == block.head.block_id );
  assert!( rblock.head.data_type_id == block.head.data_type_id );
}