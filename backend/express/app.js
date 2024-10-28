const express = require('express');
const fileUpload = require('express-fileupload');
const path = require('path');
const fs = require('fs');
const { v4: uuidv4 } = require('uuid');

const app = express();
const uploadDir = path.join(__dirname, 'static/upload/images');

// CORS 설정
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', 'http://localhost:3000');
  res.header(
    'Access-Control-Allow-Headers',
    'Origin, X-Requested-With, Content-Type, Accept'
  );
  res.header('Access-Control-Allow-Credentials', 'true');
  next();
});

// 파일 업로드를 위한 미들웨어 설정
app.use(fileUpload());

// 정적 파일 제공을 위한 미들웨어 설정
app.use('/static', express.static(path.join(__dirname, 'static')));

// 디렉토리가 존재하지 않으면 생성
fs.mkdirSync(uploadDir, { recursive: true });

app.post('/upload/image', (req, res) => {
  if (!req.files || !req.files.upload) {
    return res.status(400).json({ error: 'No file uploaded' });
  }

  const uploadFile = req.files.upload;
  const uploadId = `${uuidv4()}.${path.extname(uploadFile.name)}`;

  uploadFile.mv(path.join(uploadDir, uploadId), (err) => {
    if (err) {
      console.error('Error saving the file:', err);
      return res.status(500).json({ error: 'Internal Server Error' });
    }

    const response = {
      url: `/static/upload/images/${uploadId}`,
      ...req.body,
    };

    return res.json({ url: `http://localhost:8070${response.url}` });
  });
});

const port = 8070;
app.listen(port, () => {
    console.log(path);
  console.log(`Server is listening on port ${port}`);
});
