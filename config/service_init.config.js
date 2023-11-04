const SeniorFacilitiesService = require("../services/senior_facilities.service");
const UserService = require("../services/user.service");

const seniorFacilitiesService = new SeniorFacilitiesService();
const userService = new UserService();

console.log("service initialize succeed");
module.exports = { seniorFacilitiesService, userService };
