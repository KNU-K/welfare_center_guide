const { Naver } = require("../config/dotenv.config");
const { userService } = require("../config/service_init.config");
const TokenService = require("../services/token.service");

const NaverStrategy = require("passport-naver").Strategy;
const LocalStrategy = require("passport-local").Strategy;
//원래는 바로 done 하는게 아니라 db 내에 저장해준다. 서비스에 대한 가입
module.exports = {
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
