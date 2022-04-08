const utils = require("../utils");

async function getUserInfo(req, res) {
  let results = {};
  const userEmail = req.query.email;
  utils.log('user.getUserInfo', '', userEmail);

  if (req.method.toLowerCase() === 'get') {
    results = { 
      email: userEmail,
      name: 'test_name',
      password: 'test_pwd',
      articles: {
        articleID1: {
          title: 'title1',
        },
        articleID2: {
          title: 'title2',
        },
      }
    };
    // todo: await get from db
  }

  return utils.response(res, JSON.stringify(results));
}

// todo refactor: api + impl
async function signUpUser(req, res) {
  const message = req.body.message;
  utils.log('user.signUpUser', '', message)
  // todo await set

  return utils.response(res, '{ "code": 0, "message": "ok" }');
}

async function updateUserInfo(req, res) {
  const message = req.body.message;
  utils.log('user.updateUserInfo', '', message);

  // get, err handle
  // parse msg, check valid
  // generate new info
  // set, err handle
}

module.exports = {
  getUserInfo,
  signUpUser,
};