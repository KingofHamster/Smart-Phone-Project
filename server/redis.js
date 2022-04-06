const redis = require('redis');

// (async () => {
//   const client = redis.createClient(); // localhost:6379

//   client.on('error', (err) => console.log('Redis Client Error', err));

//   await client.connect();

//   await client.set('key', '13value');
//   const value = await client.get('key');
//   console.log(value);
// })();

async function init() {
    const client = redis.createClient(); // localhost:6379
    client.on('error', (err) => console.log('Redis Client Error', err));
    await client.connect();
    console.log('Redis running at http://127.0.0.1:6379/');
}

module.exports = {
    init,
};
