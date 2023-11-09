const passport = require("passport");
const { naver, local } = require("../middlewares/passport.middleware");

passport.use("naver", naver);
passport.use("local", local);
passport.serializeUser((user, done) => {
  console.log("serialize User", user);
  done(null, user);
});

passport.deserializeUser((user, done) => {
  console.log("deserialize User");
  done(null, user);
});

//local login
const local_login = async (req, res, next) => {
  passport.authenticate("local", async (err, user) => {
    try {
      if (err) throw err;
      res.send({ msg: "login good", u_id: user.u_id }); // this doesn't access throughout session
      //only access throughout token method
    } catch (err) {
      next(err);
    }
  })(req, res, next);
};

/**
 * this sector is login method for FE development
 * that isn't work yet
 *
 */
const naver_login = passport.authenticate("naver");
const naver_login_callback = async (req, res, next) => {
  try {
    req.logIn(req.user, (err) => {
      if (err) throw err;
      res.send({ msg: "login good" });
    });
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
  local_login: local_login,
  naver_login: naver_login,
  naver_login_callback: naver_login_callback,
  kakao_login: kakao_login,
  kakao_login_callback: kakao_login_callback,
  logout: logout,
};
