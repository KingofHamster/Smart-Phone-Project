const utils = require("../utils");
const db = require("../database/redis")

const moment = require("moment");
const crypto = require("crypto");
const hash = crypto.createHash('md5');
const keyPrefix = 'ARTI_';

async function listArticles(req, res) {
  let results = {};
  utils.log('article.listArticles', '', '');

  if (req.method.toLowerCase() === 'get') {
    results = await db.listByPrefix(keyPrefix);
    console.log(results);
  }

  return utils.response(res, JSON.stringify(results));
}

async function getArticle(req, res) {
  utils.log('article.getArticle', '', req.query);

  let results = {};
  if (req.query.hasOwnProperty('id')) {
    key = keyPrefix + req.query.id;
    results = await db.get(key);
  } else {
    if (req.query.hasOwnProperty('author') || req.query.hasOwnProperty('title')) {
      results = await db.listByPrefix(keyPrefix);
      console.log(results);
      for (const article of Object.values(results)) {
        if (req.query.hasOwnProperty('author')) {
          if (article.author !== req.query.author) {
            delete results[article.ID];
            continue;
          }
        }
        if (req.query.hasOwnProperty('title')) {
          if (article.title !== req.query.title) {
            delete results[article.ID];
          }
        }
      }
    } else {
      // missing query params
      return utils.response(res, '{ "code": -1, "message": "Error format" }');
    }
  }

  return utils.response(res, JSON.stringify(results));
}

async function postArticle(req, res) {
  const body = req.body;
  utils.log('article.postArticle', '', body);

  if (!body.hasOwnProperty('author') ||
      !body.hasOwnProperty('text') ||
      !body.hasOwnProperty('title')) {
    return utils.response(res, '{ "code": -1, "message": "Error format" }');
  }

  const createTime = moment().format( "YYYY-MM-DD HH:mm:ss");
  hash.update(body.author + body.title + createTime, 'utf8');
  const ID = hash.copy().digest('hex');

  const article = {
    id: ID,
    createTime: createTime,
    lastEditTime: createTime,
    title: body.title,
    author: body.author,
    text: body.text,
    anonymous: body.anonymous
  };

  const key = keyPrefix + ID;
  await db.set(key, article);

  return utils.response(res, '{ "code": 0, "message": "ok" }');
}

async function editArticle(req, res) {
  const body = req.body;
  utils.log('article.editArticle', '', body);

  if (!body.hasOwnProperty('id') || !body.hasOwnProperty('text')) {
    return utils.response(res, '{ "code": -1, "message": "Error parameters" }');
  }

  const key = keyPrefix + body.id;
  let article = await db.get(key);

  if (article) {
    article.lastEditTime = moment().format( "YYYY-MM-DD HH:mm:ss");
    article.text = body.text;
    await db.set(key, article);
  }

  return utils.response(res, '{ "code": 0, "message": "ok" }');
}

module.exports = {
  listArticles,
  getArticle, 
  postArticle,
  editArticle,
};
