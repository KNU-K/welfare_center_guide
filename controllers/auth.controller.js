const passport = require("passport");
const { naver } = require("../middlewares/passport.middleware");
const { userService } = require("../config/service_init.config");
const TokenService = require("../services/token.service");
passport.use("naver", naver);
passport.serializeUser((user, done) => {
  console.log("serialize User", user);
  done(null, user);
});

passport.deserializeUser((user, done) => {
  console.log("deserialize User");
  done(null, user);
});
const local_login = async (req, res, next) => {
  try {
    const { refreshToken, u_name, u_email, u_provider, u_provider_id } =
      req.body;

    //TODO: valid한 refresh token인지 확인해야하는 작업 추가 되어야함.

    const user = await userService.existUser(u_email);
    //refreshToken 은 항시
    if (user) {
      TokenService.updateRefreshToken(user.u_id, refreshToken);
      return res.send({ msg: "auth succeed", u_id: user.u_id });
    }
    const registerUserResult = await userService.registerUser({
      u_name: u_name,
      u_email: u_email,
      u_provider: u_provider,
      u_provider_id: u_provider_id,
    });

    console.log(registerUserResult);
    if (registerUserResult) {
      const user = await userService.existUser(u_email);
      console.log("user=", user);

      await TokenService.saveRefreshToken(user.u_id, refreshToken);

      return res.send({ msg: "auth succeed", u_id: user.u_id });
    }
  } catch (err) {
    next(err);
  }
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
