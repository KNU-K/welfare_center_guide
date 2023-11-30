const { connection } = require("../config/db_conn.config");

class TokenService {
  static saveRefreshToken(u_id, refreshToken) {
    return new Promise((resolve, reject) => {
      connection.query(
        "INSERT INTO token (u_id,refresh_token) VALUES (?, ?)",
        [u_id, refreshToken],
        (err, result) => {
          console.log(err);
          if (err) reject(err);
          console.log(result);
          if (result) {
            resolve(true);
          } else {
            reject(false);
          }
        }
      );
    });
  }
  static updateRefreshToken(u_id, refreshToken) {
    return new Promise((resolve, reject) => {
      // TODO: update Query ÀÛ¼º
      connection.query(
        `update token set refresh_token = '${refreshToken}' where u_id = ${u_id}`,
        [u_id, refreshToken],
        (err, result) => {
          if (err) reject(err);
          if (result) {
            resolve(true);
          } else {
            resolve(false);
          }
        }
      );
    });
  }
}

module.exports = TokenService;
