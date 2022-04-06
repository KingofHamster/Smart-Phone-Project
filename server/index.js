const db = require('./redis');
// const server = require('./http/http');
const server = require('./http/server');

async function main() {
    await db.init();
    server.start();

    // todo node_modules local
}

main();
