const { seniorFacilitiesService } = require("../config/service_init.config");
const find_all_facilities = () => {
  try {
    const result = seniorFacilitiesService.findAll();
    return result;
  } catch (err) {
    throw err;
  }
};
const find_facility = async (req, res, next) => {
  try {
    const queryString = req.query;
    console.log(queryString.key());
    if (!queryString.keys().length) res.send(find_all_facilities());
    else console.log(queryString);
  } catch (err) {
    next(err);
  }
};
module.exports = {
  find_facility: find_facility,
};
