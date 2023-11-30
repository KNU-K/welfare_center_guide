const { connection } = require("../config/db_conn.config");

let facilities = [];
let init_sql = "select * from senior_facility";

class SeniorFacilitiesService {
  constructor() {
    connection.query(init_sql, (err, result) => {
      if (err) throw err;
      facilities = result;
    });
  }
  findAll() {
    return facilities;
  }
  findFacilityOfUser(id) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select * from bookmark,senior_facility where bookmark.sf_id = senior_facility.sf_id and bookmark.u_id = ${id}`,
        (err, result) => {
          if (err) reject(err);

          resolve(result);
        }
      );
    });
  }
  findNonSelectedFacilityOfUser(id) {
    return new Promise((resolve, reject) => {
      connection.query(
        `select * from senior_facility where senior_facility.sf_id not in(select bookmark.sf_id from bookmark,senior_facility where bookmark.sf_id = senior_facility.sf_id and bookmark.u_id = ${id})`,
        (err, result) => {
          if (err) reject(err);

          resolve(result);
        }
      );
    });
  }

  //반경범위 return
}

module.exports = SeniorFacilitiesService;
