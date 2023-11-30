const { seniorFacilitiesService } = require("../config/service_init.config");
const find_all_facilities = (req, res, next) => {
  try {
    const result = seniorFacilitiesService.findAll();
    res.send(result);
  } catch (err) {
    next(err);
  }
};
const find_facility_of_user = async (req, res, next) => {
  try {
    const { id } = req.params;

    const result = await seniorFacilitiesService.findFacilityOfUser(id);
    res.send(result);
  } catch (err) {
    next(err);
  }
};

const find_non_selected_facility_of_user = async (req, res, next) => {
  try {
    const { id } = req.params;
    const result = await seniorFacilitiesService.findNonSelectedFacilityOfUser(
      id
    );
    res.send(result);
  } catch (err) {
    next(err);
  }
};
module.exports = {
  find_non_selected_facility_of_user: find_non_selected_facility_of_user,
  find_facility_of_user: find_facility_of_user,
  find_facility: find_all_facilities,
};
