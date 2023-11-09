const { connection } = require("../config/db_conn.config");

class TokenService {
  saveRefreshToken(u_id, refreshToken) {
    connection.query(
      "INSERT INTO token (u_id,refresh_token) VALUES (?, ?)",
      [u_id, refreshToken],
      (err, result) => {
        if (err) throw err;
        if (result) {
          return true;
        } else {
          return false;
        }
      }
    );
  }
  updateRefreshToken(u_id, refreshToken) {
    // TODO: update Query ÀÛ¼º
    connection.query(
      `update token set refresh_token = '${refreshToken}' where u_id = ${u_id}`,
      [u_id, refreshToken],
      (err, result) => {
        if (err) throw err;
        if (result) {
          return true;
        } else {
          return false;
        }
      }
    );
  }
}

module.exports = TokenService;
