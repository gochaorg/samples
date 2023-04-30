use std::sync::{Arc, RwLock};

use super::block;
use super::super::bbuff::absbuff::*;

#[derive(Clone)]
pub struct LogFile<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  buff: FlatBuff,
  count: Option<u32>,
}

impl<FlatBuff> LogFile<FlatBuff> 
where FlatBuff: ReadBytesFrom+WriteBytesTo+BytesCount+ResizeBytes+Clone
{
  fn new( buff:FlatBuff ) -> Self {
    LogFile {
      buff: buff,
      count: None
    }
  }

  fn get_count() -> u32 {
    todo!()
  }
}

struct LogPointer {
  
}