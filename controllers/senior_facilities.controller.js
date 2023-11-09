const { seniorFacilitiesService } = require("../config/service_init.config");
const find_all_facilities = (req, res, next) => {
  try {
    const result = seniorFacilitiesService.findAll();
    res.send(result);
  } catch (err) {
    throw err;
  }
};

module.exports = {
  find_facility: find_all_facilities,
};
