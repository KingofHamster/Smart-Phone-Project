const db = require('./redis');
const server = require('./http');

async function main() {
    await db.init();
    server.init();

    // todo node_modules local
    // todo express, Api design, ...
}

main();
