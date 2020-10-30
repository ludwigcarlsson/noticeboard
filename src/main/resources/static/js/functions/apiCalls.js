import * as httpRequest from './httpRequest.js'

export default class Api {

  static async parse(response) {
    if(response.ok) {
      return JSON.parse(await response.text())
    }
    return null
  }
  

   //---------------//
  //--  Account  --//

  static async createAccount(_userName, _password, _email) {
    const result = await httpRequest.post('/account/create', {
      userName: _userName,
      password: _password,
      email: _email
    })
    return result
  }

  static async login (_userName, _password) {
    const result = await httpRequest.post('/account/login', {
      userName: _userName,
      password: _password
    })
    return result
  }

  static async logout() {
    httpRequest.get('/account/logout')
  }

  static async getLoginStatus() {
    const response = await httpRequest.get('/account/loginStatus')
    return Api.parse(response)
  }


  //---------------//
  //--  Notices  --//

  static getAllNotices() {
    return httpRequest.get('/notices/')
  }

  static getNotice(id) {
    return httpRequest.get('/notices/' + id)
  }

  static async newNotice(_title, _content) {
    return httpRequest.post('/notices', {
      title: _title,
      content: _content
    })
  }

  static updateNotice(id, _title = null, _content = null) {
    return httpRequest.patch('/notices/' + id, {
      title: _title,
      content: _content
    })
  }

  static deleteNotice(id) {
    return httpRequest.del('/notices/' + id)
  }


  //---------------//
  //--  Comments  --//
  
  static newComment(noticeId, _content) {
    return httpRequest.post('/notices/' + noticeId + '/comments', {
      content: _content
    })
  }

  static updateComment(noticeId, commentId, _content) {
    return httpRequest.patch('/notices/' + noticeId + '/comments/' + commentId, {
      content: _content
    })
  }

  static deleteComment(noticeId, commentId) {
    return httpRequest.del('/notices/' + noticeId + '/comments/' + commentId)
  }
}