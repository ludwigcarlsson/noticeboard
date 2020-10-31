# Noticeboard
The notice board is a public listing of notices which the user can interact with through comments. A user, if registered can post, edit and delete comments and notices. Furthermore, if an user is inactive for more than 10 minutes, they will be automatically logged out. 

The web application uses: Hibernate, Postgres and Spring.

Uploaded to Heroku at: https://experis-notice-board.herokuapp.com/

## Installation
1. Clone the repo
git clone https://github.com/ludwigcarlsson/noticeboard.git

2. Open the project in your preferred IDE. We recommend Intellij.

3. To run the project/server, navigate to the "main" class, right click and "run main".

## Usage 

### Endpoints

Endpoints      | Type |   Description
------------ | ------------| ------------ 
"https://experis-notice-board.herokuapp.com/#/account/create" |(POST)| Adds an account/user
"https://experis-notice-board.herokuapp.com/#/login" |(POST)| Login with a registered account
"https://experis-notice-board.herokuapp.com/#/" |(GET)| Gets all the notices and displays them on the mainpage
"https://experis-notice-board.herokuapp.com/#/notice/{id}" |(GET) | Gets a specific notice and showcases all the comments on the issue
"https://experis-notice-board.herokuapp.com/#/notice/{id}" |(PATCH) | Edits a specific notice posted by the logged in account
"https://experis-notice-board.herokuapp.com/#/notice/{id}" | (DELETE)| Deletes a specific notice posted by the logged in account
"https://experis-notice-board.herokuapp.com/#/notice/create" |(POST)| Creates a notice
"https://experis-notice-board.herokuapp.com/#/notices/{id}/comments" | (POST) |  Creates a comment to a specific notice
"https://experis-notice-board.herokuapp.com/#/notices/{id}/comments" | (PATCH) | Edits a comment to a specific notice
"https://experis-notice-board.herokuapp.com/#/notices/1/comments" |(DELETE) | Deletes a comment posted by the logged in account to a specific notice


## License
[MIT](https://mit-license.org/)

#### Authors
[Eric Enoksson](https://github.com/Bumpfel), [Oscar Dahlquist](https://github.com/Vattenkruka) and [Ludwig Carlsson](https://github.com/ludwigcarlsson)
