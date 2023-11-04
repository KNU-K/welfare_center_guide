const { connection } = require("../config/db_conn.config");

let users = [];
class UserService {
  constructor() {
    connection.query("select * from user", (err, result) => {
      if (err) throw err;
      users = result;
    });
  }
  deleteBookmarkByIdAndBookmarkId(id, bookmarkId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `delete from bookmark where u_id=${id} and bookmark_id=${bookmarkId}`,
        (err, result) => {
          if (err) reject(err);
          if (result.affectedRows) resolve(true);
          else resolve(false);
        }
      );
    });
  }
  createBookmark(id, sf_id) {
    return new Promise((resolve, reject) => {
      connection.query(
        "INSERT INTO bookmark (u_id, sf_id) VALUES (?, ?)",
        [id, sf_id],
        (err, result) => {
          if (err) reject(err);
          if (result.affectedRows) resolve(true);
          else resolve(false);
        }
      );
    });
  }
  findAllUser() {
    return users;
  }
  findUserById(id) {
    const user = users.find((user) => user.u_id === Number(id));
    return user;
  }
  findAllBookmarkById(id) {
    return new Promise((resolve, reject) => {
      connection.query(
        `
      select bookmark_id,sf_name,sf_tel, sf_addr from (select * from bookmark where u_id = ${id})as b inner join senior_facility as s on b.sf_id = s.sf_id;
      `,
        (err, result) => {
          if (err) reject(err);
          resolve(result);
        }
      );
    });
  }
  findBookmarkById(id, bookmarkId) {
    return new Promise((resolve, reject) => {
      connection.query(
        `
      select bookmark_id,sf_name,sf_tel, sf_addr from (select * from bookmark where u_id = ${id} and bookmark_id =${bookmarkId})as b inner join senior_facility as s on b.sf_id = s.sf_id;
      `,
        (err, result) => {
          if (err) reject(err);
          resolve(result);
        }
      );
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
