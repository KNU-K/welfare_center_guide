const SeniorFacilitiesService = require("../services/senior_facilities.service");
const UserService = require("../services/user.service");

const seniorFacilitiesService = new SeniorFacilitiesService();
const userService = new UserService();

module.exports = { seniorFacilitiesService, userService };
