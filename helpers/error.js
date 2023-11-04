class CustomError extends Error {
  constructor(statusCode, msg) {
    super(msg || null);
    this.statusCode = statusCode;
  }
}

module.exports = { CustomError };
