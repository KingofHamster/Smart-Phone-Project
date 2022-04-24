const crypto = require("crypto");
const hash = crypto.createHash('md5');
const moment = require("moment");

hash.update('e1230023790323908kdjfaklsjfklmslkmasklj', 'utf8');

hex = hash.digest('hex')
console.log(typeof(hex));