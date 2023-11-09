const { Naver } = require("../config/dotenv.config");
const { userService } = require("../config/service_init.config");
const TokenService = require("../services/token.service");

const NaverStrategy = require("passport-naver").Strategy;
const LocalStrategy = require("passport-local").Strategy;
//원래는 바로 done 하는게 아니라 db 내에 저장해준다. 서비스에 대한 가입
module.exports = {
  local: new LocalStrategy(
    {
      passReqToCallback: true,
    },
    async (req, username, password, done) => {
      try {
        /**
         * username and password aren't use
         * req를 통해 가입 정보를 받고, 그 정보를 토대로
         * 가입 및 가입이 완료되었다면 로그인을한다.
         *
         */
        const { refreshToken, u_name, u_email, u_provider, u_provider_id } =
          req.body;

        //TODO: valid한 refresh token인지 확인해야하는 작업 추가 되어야함.

        const user = await userService.existUser(u_email);
        //refreshToken 은 항시
        if (user) {
          TokenService.updateRefreshToken(user.u_id, refreshToken);
          return done(null, user);
        }
        if (
          await userService.registerUser({
            u_name: u_name,
            u_email: u_email,
            u_provider: u_provider,
            u_provider_id: u_provider_id,
          })
        ) {
          const user = await userService.existUser(u_email);
          TokenService.createRefreshToken(user.u_id, refreshToken);
          return done(null, user);
        }
      } catch (err) {
        return done(err, null);
      }
    }
  ),
  naver: new NaverStrategy(
    {
      clientID: Naver.NaverClientID,
      clientSecret: Naver.NaverClientSecret,
      callbackURL: "http://localhost:8080/api/auth/naver-login/callback",
    },
    async (accessToken, refreshToken, profile, done) => {
      try {
        // 추가 필요
        //존재하나?
        //존재하면 --> 로그인
        //존재안하면 등록 후 로그인
        const user = await userService.existUser(profile._json.email);
        if (user) return done(null, user);
        if (
          await userService.registerUser({
            u_name: profile.displayName,
            u_email: profile._json.email,
            u_provider: profile.provider,
            u_provider_id: profile.id,
          })
        )
          console.log("a");
        return done(null, {
          u_name: profile.displayName,
          u_email: profile._json.email,
          u_provider: profile.provider,
          u_provider_id: profile.id,
        });
      } catch (err) {
        return done(err, null);
      }
    }
  ),
};
