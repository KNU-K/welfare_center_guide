const { connection } = require("../config/db_conn");

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
}

module.exports = SeniorFacilitiesService;
