// Entrance
// Created by Wang Tianyang on 2022/04/06

const db = require('./redis');
(async function main() {
    await db.init();
    const http_server = require('./http/server');
    const _ = http_server.start();
}());

// todo node_modules local
