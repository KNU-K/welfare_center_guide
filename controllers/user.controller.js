const { userService } = require("../config/service_init.config");

const find_all_user = async (req, res, next) => {
  try {
    const users = userService.findAllUser();
    res.send(
      users.map((user) => ({
        u_email: user.u_email,
        u_id: user.u_id,
        u_name: user.u_name,
      }))
    );
  } catch (err) {
    next(err);
  }
};
//권한 필요
const find_detail_of_user = async (req, res, next) => {
  try {
    const { id } = req.params;
    const user = await userService.findUserById(id);
    res.send(user);
  } catch (err) {
    next(err);
  }
};

const delete_user = async (req, res, next) => {
  try {
  } catch (err) {}
};
//관계형으로 찾기 inner join
const find_all_bookmark_of_user = async (req, res, next) => {
  try {
    const { id } = req.params;
    console.log("hi");
    const bookmarks = await userService.findAllBookmarkById(id);
    res.send(bookmarks);
  } catch (err) {
    next(err);
  }
};
const find_bookmark_of_user = async (req, res, next) => {
  try {
    const { id, bookmarkId } = req.params;
    const bookmark = await userService.findBookmarkById(id, bookmarkId);
    res.send(bookmark);
  } catch (err) {
    next(err);
  }
};

const create_bookmark_of_user = async (req, res, next) => {
  try {
    const { id } = req.params;
    const { sf_id } = req.body;
    if (await userService.createBookmark(id, sf_id)) {
      res.send({ msg: "bookmark good" });
    } else {
      res.send({ msg: "bookmark fail" });
    }
  } catch (err) {
    res.send({
      msg: "already exist sequence combination ",
    });
  }
};
const delete_bookmark_of_user = async (req, res, next) => {
  try {
    const { id, bookmarkId } = req.params;
    if (await userService.deleteBookmarkByIdAndBookmarkId(id, bookmarkId)) {
      res.send({
        msg: "succeed",
      });
    } else {
      res.send(
        res.send({
          msg: "fail",
        })
      );
    }
  } catch (err) {
    console.log(err);
    next(err);
  }
};
module.exports = {
  find_all_user: find_all_user,
  find_detail_of_user: find_detail_of_user,
  delete_user: delete_user,
  find_all_bookmark_of_user: find_all_bookmark_of_user,
  find_bookmark_of_user: find_bookmark_of_user,
  create_bookmark_of_user: create_bookmark_of_user,
  delete_bookmark_of_user: delete_bookmark_of_user,
};
