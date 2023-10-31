const express = require("express");
const apiRouter = require("./api/index");
const errorMiddleware = require("./middlewares/error.middleware");
const { connection } = require("./config/db_conn");
const app = express();
const port = process.env.SERVER_PORT || 8080;
const cors = require("cors");
const session = require("express-session");
app.use(
  cors({
    sameSite: "none",
    origin: true,
    credentials: true,
  })
);
app.use(
  session({
    secret: "your-secret-key",
    resave: false,
    saveUninitialized: false,
  })
);
app.use(passport.initialize());
app.use(passport.session());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use("/api", apiRouter);

app.use(errorMiddleware);

app.listen(port, async () => {
  try {
    await connection.connect();
    console.log("server open ..!");
  } catch (err) {
    console.log("server close, ", err);
  }
});
