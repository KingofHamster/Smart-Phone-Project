const redis = require('redis');

//   await client.set('key', '13value');
//   const value = await client.get('key');

async function init() {
    const client = redis.createClient(); // localhost:6379
    client.on('error', (err) => console.log('Redis Client Error', err));
    await client.connect();
    console.log('Redis running at localhost:6379/');
}

module.exports = {
    init,
};
