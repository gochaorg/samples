/// Поиск серверов в сети
mod lookup;

/// Лог файл
mod log;

/// Работа с байтовым буфером
mod bbuff;

fn main() {
    println!("Hello, world!");
}

#[test]
fn test_dns() {
  //use trust_dns_proto::DnsStreamHandle;
  use trust_dns_client::client::{Client, SyncClient};
  use trust_dns_client::udp::UdpClientConnection;

  let address = "127.0.0.53:53".parse().unwrap();
  let conn = UdpClientConnection::new(address).unwrap();
  let client = SyncClient::new(conn);

  use std::str::FromStr;
  use trust_dns_client::op::DnsResponse;
  use trust_dns_client::rr::{DNSClass, Name, Record, RecordType};

  let name = Name::from_str("cl.dev.local.").unwrap();

  let response: DnsResponse = client.query(&name, DNSClass::IN, RecordType::A).unwrap();
  let answers: &[Record] = response.answers();

  for a in answers {
    println!("{:?}", a)
  }
}

#[test]
fn find_sys_nameserver() {
  let contents = std::fs::read_to_string("/etc/resolv.conf").expect("Failed to open resolv.conf");
  let config = resolv_conf::Config::parse(&contents).unwrap();
  println!("{:?}", config.nameservers);
}
