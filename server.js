const express = require("express");
const apiRouter = require("./api/index");
const errorMiddleware = require("./middlewares/error.middleware");
const app = express();
const port = process.env.SERVER_PORT || 8080;
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use("/api", apiRouter);

app.use(errorMiddleware);
app.listen(port, async () => {
  try {
    console.log("server open ..!");
  } catch (err) {
    console.log("server close");
  }
});
