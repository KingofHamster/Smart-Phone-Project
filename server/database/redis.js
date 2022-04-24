let client;
let initialized = false;

async function init () {
  const redis = require('redis');
  client = redis.createClient(); // localhost:6379
  console.log('Connecting Redis on port 6379');
  client.on('error', (err) => console.log('Redis Client Error', err));
  await client.connect();
  initialized = true;
}

async function get(key) {
  if(!initialized) {
    await init();
  }

  val = await client.get(key);
  return JSON.parse(val);
}

async function set(key, value) {
  if(!initialized) {
    await init();
  }

  await client.set(key, JSON.stringify(value));
}

async function listByPrefix(prefix) {
  if(!initialized) {
    await init();
  }

  const results = {};
  for await (const key of client.scanIterator()) {
    if (key.startsWith(prefix)) {
      const actualKey = key.substring(prefix.length);
      results[actualKey] = await get(key);
    }
  }

  return results;
}

module.exports = {
  get,
  set,
  listByPrefix,
}
