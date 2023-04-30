use std::sync::{Arc, RwLock};

use super::block;
use super::super::bbuff::absbuff::*;

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

  // fn get_count() -> u32 {
  //   todo!()
  // }
}
