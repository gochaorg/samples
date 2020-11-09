DROP TABLE IF EXISTS billionaires;

CREATE TABLE billionaires (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  career VARCHAR(250) DEFAULT NULL,
  int_num INT NULL,
  date_a DATE NULL
);

INSERT INTO billionaires (first_name, last_name, career, int_num, date_a) VALUES
('Aliko', 'Dangote', 'Billionaire Industrialist', 10, parsedatetime('2012-09-17', 'yyyy-MM-dd')),
('Bill', 'Gates', 'Billionaire Tech Entrepreneur', 12, parsedatetime('2016-10-16', 'yyyy-MM-dd')),
('Folrunsho', 'Alakija', 'Billionaire Oil Magnate', 24, parsedatetime('2015-11-14', 'yyyy-MM-dd')),
('Fgah', 'Fgah-112a', 'Magnate11', 34, parsedatetime('2014-09-12', 'yyyy-MM-dd')),
('Hyyt', 'hyyt 234', 'Yu ii', 45, parsedatetime('2012-01-10', 'yyyy-MM-dd')),
('Fgah', 'Fgah-112a', 'IIYL', 57, parsedatetime('2010-02-13', 'yyyy-MM-dd')),
('TTyu', 'Tui Tyu', 'UIOU UPup!',63, parsedatetime('2008-03-14', 'yyyy-MM-dd')),
('Myuaa', 'Abrico', 'Some RRTa',null, parsedatetime('2006-04-19', 'yyyy-MM-dd'))
;

----------------------------------------------

--DROP TABLE IF EXISTS aparent;
--
--CREATE TABLE aparent (
--  id INT AUTO_INCREMENT  PRIMARY KEY,
--  name VARCHAR(250) NOT NULL
--);
--
--DROP TABLE IF EXISTS achild;
--
--CREATE TABLE achild (
--  id INT AUTO_INCREMENT  PRIMARY KEY,
--  name VARCHAR(250) NOT NULL,
--  owner_id INT NULL
--);

INSERT INTO aparent (id, name) VALUES
(1, 'Vassa'),
(2, 'Ollo');

INSERT INTO achild (id, name, owner_id) VALUES
(1, 'Mika', 1),
(2, 'Nobia', 1),
(3, 'Yoyo', 2),
(4, 'Kilof', 2);