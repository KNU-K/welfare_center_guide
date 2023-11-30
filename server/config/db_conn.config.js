const mysql = require("mysql2");
const { MySql } = require("./dotenv.config");
const config = {
  host: MySql.DB_HOST,
  port: MySql.DB_PORT,
  user: MySql.DB_USER,
  password: MySql.DB_PASSWORD,
  database: MySql.DB_NAME,
};

//default
module.exports = {
  connection: mysql.createConnection(config),
};
