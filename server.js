const express = require("express");
const app = express();
const port = process.env.SERVER_PORT || 8080;
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.listen(port, () => {
  console.log("server open ..!");
});
