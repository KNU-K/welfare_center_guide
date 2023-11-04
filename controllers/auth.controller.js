const passport = require("passport");
const { naver } = require("../middlewares/passport.middleware");

passport.use("naver", naver);
passport.serializeUser((user, done) => {
  console.log("serialize User", user);
  done(null, user);
});

passport.deserializeUser((user, done) => {
  console.log("deserialize User");
  done(null, user);
});

const naver_login = passport.authenticate("naver");

const naver_login_callback = async (req, res, next) => {
  try {
    res.send({ msg: "login good" });
  } catch (err) {
    next(err);
  }
};
const kakao_login = async (req, res, next) => {
  try {
  } catch (err) {
    next(err);
  }
};
const kakao_login_callback = async (req, res, next) => {
  try {
  } catch (err) {
    next(err);
  }
};
const logout = async (req, res, next) => {
  try {
    req.logOut((err) => {
      if (err) throw err;
      else res.send({});
    });
  } catch (err) {
    next(err);
  }
};
module.exports = {
  naver_login: naver_login,
  naver_login_callback: naver_login_callback,
  kakao_login: kakao_login,
  kakao_login_callback: kakao_login_callback,
  logout: logout,
};
