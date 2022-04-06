const moment = require("moment");
const express = require("express");
const bodyParser = require("body-parser");

const router = express.Router();
const jsonParser = bodyParser.json({ limit: "50mb" });
const urlencodedParser = bodyParser.urlencoded({ extended: false });

// fetch user info
router.get("/user", urlencodedParser, async (req, res) => {
  log("/user", "GET", req.query);
  // TODO: await wxrobot.WXWorkRobotCallback(req, res);
});

// upload user info
router.post("/user", jsonParser, async (req, res) => {
  log("/user", "POST", req.body);
  // await wxrobot.WXWorkRobotCallback(req, res);
});

// fetch article list
router.get("/articles", urlencodedParser, async (req, res) => {
  log("/articles", "GET", req.query);
  // TODO
});

router.get("/article", urlencodedParser, async (req, res) => {
  log("/article", "GET", req.query);
  // TODO: await wxrobot.WXWorkRobotCallback(req, res);
});

router.post("/article", jsonParser, async (req, res) => {
  log("/article", "POST", req.body);
  //await feedback.FeedbackRobotCallback(req, res);
});

function log(fun, method, args) {
  console.log(
    `[${moment().format(
      "YYYY-MM-DD HH:mm:ss"
    )}] ${method} ${fun}, args: [${JSON.stringify(args)}]`
  );
}

module.exports = router;
