const utils = require("../utils");
const db = require("../database/redis");
const { disabled } = require("express/lib/application");

const keyPrefix = 'USER_';

async function getUserInfo(req, res) {
  let results = {};
  const userEmail = req.query.email;
  utils.log('user.getUserInfo', '', userEmail);

  if (req.method.toLowerCase() === 'get') {
    const key = keyPrefix + userEmail;
    results = await db.get(key);
    console.log(results);
  }

  return utils.response(res, JSON.stringify(results));
}

async function addUser(req, res) {
  const body = req.body;
  // todo check valid
  if (!body.hasOwnProperty('email') || !body.hasOwnProperty('name')) {
    return utils.response(res, '{ "code": -1, "message": "Error parameters" }');
  }
  utils.log('user.addUser', '', body)

  const key = keyPrefix + body.email;
  await db.set(key, body);

  return utils.response(res, '{ "code": 0, "message": "ok" }');
}

async function updateUser(req, res) {
  const body = req.body;
  utils.log('user.updateUserInfo', '', body);

  // check valid
  if (!body.hasOwnProperty('email') || !body.hasOwnProperty('name')) {
    return utils.response(res, '{ "code": -1, "message": "Error parameters" }');
  }

  const key = keyPrefix + body.email;
  const result = db.get(key);
  if (result) {
    let newUserInfo = { ...result };
    newUserInfo = Object.assign({}, req.body);
    await db.set(key, newUserInfo);

    return utils.response(res, '{ "code": 0, "message": "ok" }');
  }

  return utils.response(res, '{ "code": -1, "message": User Not Found }');
}

// list all users
async function listUsers(req, res) {
  let results = {};
  utils.log('user.listUsers', '', '');

  if (req.method.toLowerCase() === 'get') {
    results = await db.listByPrefix(keyPrefix);
    console.log(results);
  }

  return utils.response(res, JSON.stringify(results));
}

module.exports = {
  getUserInfo,
  addUser,
  listUsers,
  updateUser
};