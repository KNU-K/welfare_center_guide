const { connection } = require("../config/db_conn.config");

let users = [];
let init_sql = "select * from user";
class UserService {
  constructor() {
    connection.query(init_sql, (err, result) => {
      if (err) throw err;
      users = result;
    });
  }
  async existUser(u_email) {
    const user = await users.find((user) => user.u_email == u_email);
    if (!user) return null;
    return user;
  }
  async registerUser(user) {
    connection.query(
      "INSERT INTO user (u_name, u_email, u_provider, u_provider_id) VALUES (?, ?, ?, ?)",
      [user.u_name, user.u_email, user.u_provider, user.u_provider_id],
      (err, result) => {
        if (err) throw err;
        users.push({ u_id: users.length + 1, ...user });
        console.log(users);
        return true;
      }
    );
  }
}

module.exports = UserService;
