const { seniorFacilitiesService } = require("../config/service_init.config");
const find_all_facilities = () => {
  try {
    const result = seniorFacilitiesService.findAll();
    return result;
  } catch (err) {
    throw err;
  }
};

module.exports = {
  find_facility: find_all_facilities,
};
