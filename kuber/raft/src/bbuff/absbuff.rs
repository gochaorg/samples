use std::{
  sync::{Arc, RwLock, PoisonError, RwLockWriteGuard, RwLockReadGuard}, 
  fs::File,
  io::{
    SeekFrom,
    prelude::*
  },
};

/// Ошибка чтения/записи
#[derive(Debug,Clone)]
pub struct ABuffError( pub String );

impl From<PoisonError<RwLockWriteGuard<'_, std::fs::File>>> for ABuffError {
  fn from(value: PoisonError<RwLockWriteGuard<'_, std::fs::File>>) -> Self {
    Self(
      format!("can't write lock file field at {value}")
    )
  }
}

impl From<std::io::Error> for ABuffError {
  fn from(value: std::io::Error) -> Self {
    Self(
      format!("io error: {value}")
    )
  }
}

impl From<PoisonError<RwLockReadGuard<'_, std::fs::File>>> for ABuffError {
  fn from(value: PoisonError<RwLockReadGuard<'_, std::fs::File>>) -> Self {
    Self(
      format!("can't read lock file field at {value}")
    )
  }
}

/// Чтение байтов по абсолютной позиции
pub trait ReadBytesFrom {
  /// Чтение массива байтов
  /// 
  /// # Возвращает
  /// кол-во прочитанных байтов
  /// 
  /// может быть возврещено меньше чем запрагиваемое значение
  /// 
  /// 0 - если нет данных, например выход за пределы массива или файла
  fn read_from( &self, pos:usize, data_consumer:&mut [u8] ) -> Result<usize,ABuffError>;
}

/// Запись байтов в указанную позицию
pub trait WriteBytesTo {
  /// Запись байтов в определенную позицию
  fn write_to( &mut self, pos:usize, data_provider: &[u8] ) -> Result<(),ABuffError>;
}

/// Возвращает кол-во байтов
pub trait BytesCount {
  /// Возвращает кол-во байтов
  fn bytes_count( &self ) -> Result<usize,ABuffError>;
}

/// Изменяет размер массива
pub trait ResizeBytes {
  /// Изменяет размер массива  
  fn resize_bytes( &mut self, new_size:usize ) -> Result<(),ABuffError>;
}

/// Байтовый массив в памяти
#[derive(Debug,Clone)]
pub struct ByteBuff{ 
  pub data: Box<Vec<u8>>, 
  pub resizeable: bool,
  pub max_size: Option<usize>
}

impl ReadBytesFrom for ByteBuff {
  fn read_from( &self, pos:usize, data_consumer:&mut [u8] ) -> Result<usize, ABuffError> {
    if pos > self.data.len() {
      Ok( 0 )
    } else {
      let available = self.data.len() - pos;
      let read_size = data_consumer.len().min( available );
      for i in 0..read_size {
        data_consumer[i] = self.data[i + pos]
      }
      Ok(read_size)
    }
  }
}

impl WriteBytesTo for ByteBuff {
  fn write_to( &mut self, pos:usize, data_provider: &[u8] ) -> Result<(),ABuffError> {
    let min_size = pos + data_provider.len();
    if self.data.len() < min_size {
      if self.resizeable {
        match self.max_size {
          Some(max_size) => {
            if min_size > max_size {
              return Err(ABuffError(format!("can't write, limit by max size {max_size}, target size {min_size}")))
            } else {
              self.data.resize(min_size, 0);
            }
          },
          None => {
            self.data.resize(min_size, 0);
          }
        }
      }
    }

    for i in 0..data_provider.len() {
      self.data[pos + i] = data_provider[i];
    }

    Ok(())
  }
}

impl BytesCount for ByteBuff {
  fn bytes_count( &self ) -> Result<usize,ABuffError> {
    Ok(self.data.len())
  }
}

impl ResizeBytes for ByteBuff {
  fn resize_bytes( &mut self, new_size:usize ) -> Result<(),ABuffError> {
    self.data.resize(new_size, 0);
    Ok(())
  }
}

/// Файловый буффер
#[derive(Debug,Clone)]
pub struct FileBuff {
  pub file: Arc<RwLock<File>>
}

impl WriteBytesTo for FileBuff {
  fn write_to( &mut self, pos:usize, data_provider: &[u8] ) -> Result<(),ABuffError> {
    let mut file = self.file.write()?;
    let file_len = file.metadata()?.len();

    let min_size = pos + data_provider.len();
    if min_size > (u64::MAX as usize) {
      return Err(ABuffError("can't write to file pos more than u64::MAX".to_string()))
    }

    if (file_len as usize) < min_size {
      file.set_len( min_size as u64 )?
    }

    file.seek(SeekFrom::Start(pos as u64))?;

    let writed = file.write(data_provider)?;
    if writed < data_provider.len() {
      let expect = data_provider.len();
      return Err(ABuffError(format!("partial writed, writed {writed} bytes, expect {expect}")));
    }

    file.flush()?;
    
    Ok(())
  }
}

impl ReadBytesFrom for FileBuff {
  fn read_from( &self, pos:usize, data_consumer:&mut [u8] ) -> Result<usize,ABuffError> {
    let mut file = self.file.write()?;
    let file_len = file.metadata()?.len();
    if pos > (file_len as usize) { return Ok(0); }

    file.seek(SeekFrom::Start(pos as u64))?;

    let reads = file.read(data_consumer)?;
    Ok(reads)
  }
}

impl BytesCount for FileBuff {
  fn bytes_count( &self ) -> Result<usize,ABuffError> {
    let file = self.file.read()?;
    let file_len = file.metadata()?.len();
    Ok(file_len as usize)
  }
}

impl ResizeBytes for FileBuff {
  fn resize_bytes( &mut self, new_size:usize ) -> Result<(),ABuffError> {
    if new_size > (u64::MAX as usize) { return Err(ABuffError(format!("can't resize file more than u64"))); }

    let file = self.file.write()?;
    file.set_len(new_size as u64)?;
    Ok(())
  }
}