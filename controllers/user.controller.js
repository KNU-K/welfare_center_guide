const find_all_user = async (req, res, next) => {
  try {
  } catch (err) {
    next(err);
  }
};
const find_detail_of_user = async (req, res, next) => {
  try {
    const { userId } = req.params;
  } catch (err) {
    next(err);
  }
};
const find_all_bookmark_of_user = async (req, res, next) => {
  try {
    const { userId } = req.params;
  } catch (err) {
    next(err);
  }
};
const find_bookmark_of_user = async (req, res, next) => {
  try {
    const { userId, bookmarkId } = req.params;
  } catch (err) {
    next(err);
  }
};
const create_bookmark_of_user = async (req, res, next) => {
  try {
    const { userId } = req.params;
  } catch (err) {
    next(err);
  }
};
const update_bookmark_of_user = async (req, res, next) => {
  try {
    const { userId, bookmarkId } = req.params;
  } catch (err) {
    next(err);
  }
};
const delete_bookmark_of_user = async (req, res, next) => {
  try {
    const { userId, bookmarkId } = req.params;
  } catch (err) {
    next(err);
  }
};
module.exports = {
  find_all_user: find_all_user,
  find_detail_of_user: find_detail_of_user,
  find_all_bookmark_of_user: find_all_bookmark_of_user,
  find_bookmark_of_user: find_bookmark_of_user,
  create_bookmark_of_user: create_bookmark_of_user,
  update_bookmark_of_user: update_bookmark_of_user,
  delete_bookmark_of_user: delete_bookmark_of_user,
};
