const express = require("express");
const bodyParser = require("body-parser");
const utils = require("../utils");
const article = require("../services/article");
const user = require("../services/user");

const router = express.Router();
const jsonParser = bodyParser.json({ limit: "50mb" });
const urlencodedParser = bodyParser.urlencoded({ extended: false });

// fetch user info
//{ip_address}:8000/api/user
router.get("/user", urlencodedParser, async (req, res) => {
  utils.log("/user", "GET", req.query);
  await user.getUserInfo(req, res);
});

// upload user info
router.post("/user", jsonParser, async (req, res) => {
  utils.log("/user", "POST", req.body);
  // await wxrobot.WXWorkRobotCallback(req, res);
});

// fetch article list
router.get("/articles", urlencodedParser, async (req, res) => {
  utils.log("/articles", "GET", req.query);
  // TODO
});

router.get("/article", urlencodedParser, async (req, res) => {
  utils.log("/article", "GET", req.query);
  // TODO: await wxrobot.WXWorkRobotCallback(req, res);
});

router.post("/article", jsonParser, async (req, res) => {
  utils.log("/article", "POST", req.body);
  //await feedback.FeedbackRobotCallback(req, res);
});

module.exports = router;
