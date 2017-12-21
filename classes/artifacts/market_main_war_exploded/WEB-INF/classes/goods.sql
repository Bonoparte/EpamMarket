CREATE TABLE goods
(
  goods_id      SERIAL        NOT NULL
    CONSTRAINT goods_pkey
    PRIMARY KEY,
  name VARCHAR(256)  NOT NULL,
  price        REAL          NOT NULL,
  amount       INTEGER       NOT NULL,
  description  VARCHAR(1024) NOT NULL
);

CREATE UNIQUE INDEX goods_id_uindex
  ON goods (goods_id);
CREATE UNIQUE INDEX goods_name_uindex ON goods (name);

