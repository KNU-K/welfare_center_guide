const passport = require("passport");
const {
  naver_login_callback,
  kakao_login,
  kakao_login_callback,
  naver_login,
  logout,
} = require("../controllers/auth.controller");
const {
  find_facility,
} = require("../controllers/senior_facilities.controller");
const {
  find_all_user,
  find_detail_of_user,
  find_bookmark_of_user,
  find_all_bookmark_of_user,
  create_bookmark_of_user,
  delete_bookmark_of_user,
  delete_user,
} = require("../controllers/user.controller");
const guardMiddleware = require("../middlewares/guard.middleware");

const router = require("express").Router();

/** authorization sector */
router.get(
  "/auth/naver-login/callback",
  passport.authenticate("naver"),
  naver_login_callback
);
router.get("/auth/kakao-login/callback", kakao_login_callback);
router.get("/auth/naver-login", naver_login);
router.post("/auth/kakao-login", kakao_login);
router.post("/auth/logout", logout);

router.get("/test", (req, res) => {
  res.send(req.user);
});
/** detail of a user */
router.get("/user", find_all_user);
router.get("/user/:id", find_detail_of_user);
router.delete("/user/:id", delete_user);
router.get("/user/:id/bookmark", find_all_bookmark_of_user);
router.get("/user/:id/bookmark/:bookmarkId", find_bookmark_of_user);
router.post("/user/:id/bookmark", create_bookmark_of_user);
router.delete("/user/:id/bookmark/:bookmarkId", delete_bookmark_of_user);

/** detail of facility information */
router.get("/senior-facilities", find_facility);

module.exports = router;
