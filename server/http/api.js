const express = require("express");
const bodyParser = require("body-parser");
const utils = require("../utils");
const article = require("../services/article");
const user = require("../services/user");
const comment = require("../services/comment");

const router = express.Router();
const jsonParser = bodyParser.json({ limit: "50mb" });
const urlencodedParser = bodyParser.urlencoded({ extended: false });

// user apis
// fetch user info
router.get("/user", urlencodedParser, async (req, res) => {
  utils.log("/user", "GET", req.query);
  await user.getUserInfo(req, res);
});

// add user
router.post("/user", jsonParser, async (req, res) => {
  utils.log("/user", "POST", req.body);
  await user.addUser(req, res);
});

// list all user
router.get("/users", urlencodedParser, async (req, res) => {
  utils.log("/users", "GET", req.query);
  await user.listUsers(req, res);
});

// update user info
router.put("/user", jsonParser, async (req, res) => {
  utils.log("/user", "PUT", req.body);
  await user.updateUser(req, res);
});

// article apis
// fetch article list
router.get("/articles", urlencodedParser, async (req, res) => {
  utils.log("/articles", "GET", req.query);
  await article.listArticles(req, res);
});

router.get("/article", urlencodedParser, async (req, res) => {
  utils.log("/article", "GET", req.query);
  await article.getArticle(req, res);
});

router.post("/article", jsonParser, async (req, res) => {
  utils.log("/article", "POST", req.body);
  await article.postArticle(req, res);
});

router.put("/article", jsonParser, async (req, res) => {
  utils.log("/article", "PUT", req.body);
  await article.editArticle(req, res);
})

// comments apis
router.get("/comment", urlencodedParser, async (req, res) => {
  utils.log("/comment", "GET", req.query);
  await comment.getCommentList(req, res);
})

router.post("/comment", jsonParser, async (req, res) => {
  utils.log("/comment", "POST", req.body);
  await comment.addComment(req, res);
})

module.exports = router;
