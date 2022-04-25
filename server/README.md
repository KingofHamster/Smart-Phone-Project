# BackEnd of HKU Tree Hole

## 1. Setup
a. Redis (for data storage)
for macOS:
```
brew install redis
```

Then, run redis on the foreground
```
redis-server
```
Or run on the background
```
// start
brew services start redis
// stop
brew services stop redis
```

for other OS, please refer to: https://redis.io/docs/getting-started/installation/install-redis-on-mac-os/

b. Server (for connection with app client, handle requests, ...)
```
// start server
cd Smart-Phone-Project/server
forever start -o out.log index.js

// stop server
forever stopall
```

## 2. Structure Design
![Backend Design Graph](images/Structure.png)

## 3. API Document
### Article
Please refer to: https://documenter.getpostman.com/view/9647531/UyrBjbvQ
### User
Please refer to: https://documenter.getpostman.com/view/9647531/UyrBjGDL
### Comment
Please refer to: https://documenter.getpostman.com/view/9647531/UyrBjbvP
