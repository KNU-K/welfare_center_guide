/**
 *
 * to check correctness of user,
 * make guard middleware
 *
 * this middleware is default option that is comparable user
 *
 */
module.exports = (req, res, next) => {
  if (req.isAuthenticated()) {
    next();
  } else {
    return res.send({
      msg: "you should login, when access this api",
    });
  }
};
