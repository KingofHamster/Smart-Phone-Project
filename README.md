# Smart-Phone-Project

## Introduction
This is the group project of COMP7506 Smart phone apps development, HKU, Semester2, 2022.
We intend to build a Android App named "HKU Tree Hole", providing functions include:
- Login and verify by HKU email
- Main Page: Display list of posts, including title, author, tags, time, ...
- Content Page: Show the content of post, support functions include thumb-up, comment, ...
- Me Page: Show info of user his/herself

- Further: Using Api from https://github.com/fangzesheng/free-api to support more functions

## Division of Labor (first version for presentation)
Wang Tianyang
- Backend
  - Rest API Design
  - Database (Redis)
  - For details: https://github.com/KingofHamster/Smart-Phone-Project/tree/main/server/README.md
- MainPage
  - Search
  - Filter

Wang Jiaxing
- Main Page
  - Display list of posts author and time.
  - New post
- Content Page
  - The content of the post, comment function and reply. 


Tang Shaowei

- Login Page
  - Verification code service for HKU Email based on SMTP protocol
  - Login logic
- Me Page
  - User data representation and editing
  - Emotion Analysis
  - Logout
- Tecent Cloud as server if necessary
  - http://175.178.42.68:12345/appProject/userInfo.php for demo, more function to be added if necessary
  - http://175.178.42.68:888/phpmyadmin_18787ffdd094dd55 for MySQL database administration


LI Jingqi

- Instant Messaging based on Tencent IM API
  - deploy TUIKit, initialize IM API
  - Create new chat
  - List exist Conversation
  - Send instant message, support text message, voice messgae and images


## Timeline
- 01/04/2022 - 07/04/2022: First Stage Development
- 08/04/2022: Presentation
- ...

## Complile setting
- SDK Manager/SDK Platfroms:
  - Android API 32
  - Android 12.0 (S)
- Emulator device:
  - Hardware: Pixel2
  - System message: Q

- compileSdkVersion = 31
- buildToolsVersion = "30.0.2"
- minSdkVersion = 19
- targetSdkVersion = 31
