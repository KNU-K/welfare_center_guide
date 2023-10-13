const {
  naver_login_callback,
  kakao_login,
  kakao_login_callback,
  naver_login,
  logout,
} = require("../controllers/auth.controller");
const {
  find_all_facilities,
  find_facility,
} = require("../controllers/senior_facilities.controller");
const {
  find_all_user,
  find_detail_of_user,
  find_bookmark_of_user,
  find_all_bookmark_of_user,
  create_bookmark_of_user,
  update_bookmark_of_user,
  delete_bookmark_of_user,
} = require("../controllers/user.controller");

const router = require("express").Router();

/** authorization sector */
router.get("/auth/naver-login/callback", naver_login_callback);
router.get("/auth/kakao-login/callback", kakao_login_callback);
router.post("/auth/naver-login", naver_login);
router.post("/auth/kakao-login", kakao_login);
router.post("/auth/logout", logout);

/** detail of a user */
router.get("/user", find_all_user);
router.get("/user/:userId", find_detail_of_user);
router.get("/user/:userId/bookmark", find_all_bookmark_of_user);
router.get("/user/:userId/bookmark/:bookmarkId", find_bookmark_of_user);
router.post("/user/:userId/bookmark", create_bookmark_of_user);
router.put("/user/:userId/bookmark/:bookmarkId", update_bookmark_of_user);
router.delete("/user/:userId/bookmark/:bookmarkId", delete_bookmark_of_user);

/** detail of facility information */
router.get("/senior-facilities", find_all_facilities);
router.get("/senior-facilities/:facilityId", find_facility);

module.exports = router;
