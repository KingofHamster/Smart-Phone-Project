const express = require("express");
const api = require("./api");

const app = express();
const host = "localhost";
const port = 8000;

app.set("host", host);
app.set("port", port);

app.use("/api", api);
app.use((_, res) => {
  res.status(404);
  res.json({ code: 404, message: "Not Found" });
});

const start = function () {
  const server = app.listen(app.get("port"), app.get("host"), function () {
    console.log(`Express server listening on ${host}:${port}`);
    //schedule.scheduleJobs();
  });
  return server;
};

module.exports = {
  start: start,
};
