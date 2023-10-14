const { CustomError } = require("../utils/error");

module.exports = (err, req, res, next) => {
  /** simply processing , # BUG this module is need refactoring */
  res.send({ msg: err.msg });
  if (err instanceof CustomError) {
  } else {
  }
};
