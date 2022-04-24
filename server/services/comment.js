const utils = require("../utils");
const db = require("../database/redis");
const moment = require("moment");

const keyPrefix = 'COMM_';

async function getCommentList(req, res) {
  utils.log('comment.getCommentList', '', req.query);

  if (!req.query.hasOwnProperty('id')) {
    return utils.response(res, '{ "code": -1, "message": "Error Parameters" }');
  }
  const key = keyPrefix + req.query.id;
  const results = await db.get(key);

  return utils.response(res, JSON.stringify(results));
}

async function addComment(req, res) {
  const body = req.body;
  utils.log('addComment', '', body);

  // check valid

  const key = keyPrefix + body.articleID;
  const time = moment().format( "YYYY-MM-DD HH:mm:ss");
  const comment = {
    articleID: body.articleID,
    author: body.author,
    text: body.text,
    anonymous: body.anonymous,
    createTime: time
  };
  let commentList = await db.get(key) || {};
  commentList[time] = comment;

  await db.set(key, commentList);
  return utils.response(res, '{ "code": 0, "message": "ok"}');
}

module.exports = {
  getCommentList,
  addComment
};
