const { seniorFacilitiesService } = require("../config/service_init.config");

const find_all_facilities = async (req, res, next) => {
  try {
    const result = seniorFacilitiesService.findAll();
    res.send(result);
  } catch (err) {
    next(err);
  }
};
const find_facility = async (req, res, next) => {
  try {
  } catch (err) {
    next(err);
  }
};
module.exports = {
  find_all_facilities: find_all_facilities,
  find_facility: find_facility,
};
