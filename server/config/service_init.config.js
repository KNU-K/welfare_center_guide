const SeniorFacilitiesService = require("../services/senior_facilities.service");
const UserService = require("../services/user.service");

const seniorFacilitiesService = new SeniorFacilitiesService();
console.log("senior facility service initialize succeed");

const userService = new UserService();
console.log("user service initialize succeed");

module.exports = { seniorFacilitiesService, userService };
