CREATE DATABASE mybatis
  DEFAULT CHARACTER SET utf8;

USE mybatis;
DROP TABLE IF EXISTS USER;
CREATE TABLE USER (
  id       INTEGER AUTO_INCREMENT NOT NULL,
  username VARCHAR(64)            NOT NULL,
  sex      VARCHAR(64)            NOT NULL,
  birthday TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  address  VARCHAR(255),
  PRIMARY KEY (id)
);
DROP TABLE IF EXISTS ADMIN;
CREATE TABLE ADMIN (
  adminId       INTEGER AUTO_INCREMENT NOT NULL,
  adminAccount  VARCHAR(64)            NOT NULL,
  adminPassword VARCHAR(64)            NOT NULL,
  address       VARCHAR(255),
  PRIMARY KEY (adminId)
);

#订单信息表
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  id         INTEGER AUTO_INCREMENT NOT NULL,
  user_id    INTEGER                NOT NULL,
  number     VARCHAR(32)            NOT NULL,
  createtime DATETIME               NOT NULL,
  note       VARCHAR(100),
  PRIMARY KEY (id)
);

#订单详细表
DROP TABLE IF EXISTS orderdetail;
CREATE TABLE orderdetail (
  id        INTEGER AUTO_INCREMENT NOT NULL,
  orders_id INTEGER                NOT NULL,
  items_id  INTEGER                NOT NULL,
  items_num INTEGER,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS items;
CREATE TABLE items(
  id        INTEGER AUTO_INCREMENT NOT NULL,
  name VARCHAR(32) NOT NULL COMMENT '商品名称',
  price FLOAT(10,1) NOT NULL COMMENT '定价',
  detail TEXT COMMENT '商品描述',
  pic VARCHAR(512) COMMENT '商品图片',
  createtiem DATETIME COMMENT '生产日期',
  PRIMARY KEY (id)
);

INSERT INTO ADMIN (adminAccount, adminPassword, address
) VALUES ('张怀', '123456', '上海'
);
INSERT INTO ADMIN (adminAccount, adminPassword, address) VALUES ('王宇', '123456', '上海');

INSERT INTO USER (username, sex, address) VALUES ('张怀', '男', '上海');
INSERT INTO USER (username, sex, address) VALUES ('玉婷', '女', '上海');
INSERT INTO USER (username, sex, address) VALUES ('刘飞', '男', '北京');
INSERT INTO USER (username, sex, address) VALUES ('逸飞', '男', '南京');
INSERT INTO USER (username, sex, address) VALUES ('黄小明', '男', '南京');
INSERT INTO USER (username, sex, address) VALUES ('张小明', '男', '南京');
INSERT INTO USER (username, sex, address) VALUES ('路小明', '男', '南京');
INSERT INTO USER (username, sex, address) VALUES ('晏小明', '男', '南京');