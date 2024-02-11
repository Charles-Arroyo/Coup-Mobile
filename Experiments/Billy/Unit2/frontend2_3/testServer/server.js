
const express = require('express')
const app = express()
const port = 8080

app.post('/', (req, res) => {
  console.log(req.body);
  res.send('{ "user": "sim", "email":"a@a", "addr":"103A"}')
})

app.get('/', (req, res) => {
  console.log(req.body);
  res.send('{ "user": "sim", "addr":"103A"}')
})

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`)
})
