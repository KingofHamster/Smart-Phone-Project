const utils = require("../utils");

function getUserInfo(req, res) {
  const userEmail = req.query.email;
  utils.log('article.getUserInfo', 'GET', req.query.email);

  if (req.method.toLowerCase() === 'get') {
    const results = {
      demo: 1,
    };
    // todo: await get from db
  }

  return utils.response(res, JSON.stringify(results));
}

module.exports = {
  getUserInfo,

};