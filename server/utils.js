const moment = require("moment");

function response(res, result) {
  if (typeof result === 'string') {
    res.send(result);
  }
  res.end();
}

function log(fun, method, args) {
  console.log(
    `[${moment().format(
      "YYYY-MM-DD HH:mm:ss"
    )}] ${method} ${fun}, args: [${JSON.stringify(args)}]`
  );
}

module.exports = {
  response,
  log,
};
