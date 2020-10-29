import Api from './api/apiCalls.js'

const accountTemplate = document.createElement('template')
accountTemplate.innerHTML = `
<div id="accountFunctions">
  <fieldset>
    <legend>Account functions</legend>
    <button id="createAccount">create account</button>
    <button id="login">login</button>
    <button id="quickLogin">quick login</button>
    <button id="logout">logout</button>
    <button id="loginStatus">check login status</button>
  </fieldset>
<div>
`

const noticeTemplate = document.createElement('template')
noticeTemplate.innerHTML = `
<div id="noticeFunctions">
  <fieldset>
    <legend>Notice functions</legend>
    <button id="getAllNotices">get all notices</button>
    <button id="getNotice">get specific notice</button>
    <button id="newNotice" name="created">new notice</button>
    <button id="updateNotice">update notice</button>
    <button id="deleteNotice">delete notice</button>
  </fieldset>
<div>
`

const commentTemplate = document.createElement('template')
commentTemplate.innerHTML = `
<div id="commentFunctions">
  <fieldset>
    <legend>Comment functions</legend>
    <button id="newComment">new comment</button>
    <button id="updateComment">update comment</button>
    <button id="deleteComment">delete comment</button>
  </fieldset>
<div>
`

document.body.appendChild(accountTemplate.content)
document.body.appendChild(noticeTemplate.content)
document.body.appendChild(commentTemplate.content)

const testInputs = document.createElement('template')
testInputs.innerHTML = `
<form>
<fieldset>
  <legend>Test inputs</legend>
  <input type="text" id="testInput1"> Account name / Notice title
  <br>
  <input type="text" id="testInput2"> Account password / Notice/Comment content
  <br>
  <input type="text" id="testInput3"> Account email
  <br>
  <input type="number" id="testInputId1"> Account id / Notice Id
  <br>
  <input type="number" id="testInputId2"> Comment Id
  <br>
  <button type="reset">Clear</button>
</fieldset>
</form>
`
document.body.appendChild(testInputs.content)

function getTestInput(id) {
  const inputValue = document.querySelector('#testInput' + id).value
  return inputValue.trim().length == 0 ? null : inputValue
}

document.querySelector('#accountFunctions').addEventListener('click', async e => {
  const in1 = getTestInput(1)
  const in2 = getTestInput(2)
  const in3 = getTestInput(3)

  if (e.target.id === 'createAccount') {
    const result = await Api.createAccount(in1, in2, in3)
    console.log(result.ok ? 'account created' : (result.status === 409 ? 'username and/or email is taken' : 'error: ' + result.status))
  } else if (e.target.id === 'login') {
    const result = await Api.login(in1, in2)
    console.log(result.ok ? 'logged in' : 'wrong account credentials')
  } else if (e.target.id === 'quickLogin') {
    const response = await Api.login('bumpfel', 'test')
    console.log(response.ok ? 'logged in' : 'wrong account credentials')
  } else if (e.target.id === 'logout') {
    await Api.logout()
    console.log('logged out')
  } else if (e.target.id === 'loginStatus') {
    const response = await Api.getLoginStatus()
    console.log(await Api.parse(response))
  }
})

document.querySelector('#noticeFunctions').addEventListener('click', async e => {
  const title = getTestInput(1)
  const content = getTestInput(2)
  const noticeId = getTestInput('Id1')

  let response, result, action, required

  if (e.target.id === 'getAllNotices') {
    response = await Api.getAllNotices()
    result = await Api.parse(response)

  } else if (e.target.id === 'getNotice') {
    required = 'noticeId'
    action = 'get'
    response = await Api.getNotice(noticeId)
    result = await Api.parse(response)
    
  } else if (e.target.id === 'newNotice') {
    action = 'create'
    required = 'title and content' 
    response = await Api.newNotice(title, content)
    result = 'notice created'
    
  } else if (e.target.id === 'updateNotice') {
    action = 'update'
    required = 'noticeId, title, and content'
    response = await Api.updateNotice(noticeId, title, content)
    result = 'notice updated'
    
  } else if (e.target.id === 'deleteNotice') {
    action = 'delete'
    response = await Api.deleteNotice(noticeId)
    result = 'notice deleted'
  }

  if(response.ok) {
    console.log(result)
  } else if(response.status === 400) {
    console.log('error on ' + action + ' notice. ' + required + ' is required')
  } else if(response.status === 401) {
    console.log('error: ' + action + ' notice requires you to be logged in')
  } else if(response.status === 404) {
    console.log('error: notice not found')
  } else {
    console.log('error on ' + action + ': ' + response.status)
  }
})

document.querySelector('#commentFunctions').addEventListener('click', async e => {
  const noticeId = getTestInput('Id1')
  const commentId = getTestInput('Id2')
  const content = getTestInput(2)

  let response, required, action

  if (e.target.id === 'newComment') {
    required = 'noticeId and content'
    action = 'create'
    response = await Api.newComment(noticeId, content)

  } else if (e.target.id === 'updateComment') {
    action = 'update'
    required = 'noticeId, commentId, and content'
    response = await Api.updateComment(noticeId, commentId, content)
    if(content === null) {
      console.log('no content changed')
    }
    
  } else if (e.target.id === 'deleteComment') {
    action = 'delete'
    required = 'noticeId and commentId'
    response = await Api.deleteComment(noticeId, commentId)
  }

  if(response.ok) {
    console.log('comment ' + action + 'd')
  } else if(response.status === 401) {
    console.log('error: ' + action + ' comment requires you to be logged in')
  } else if(response.status === 400) {
    console.log('error on ' + action + ' comment. ' + required + ' is required')
  } else {
    console.log('error on ' + action + ': ' + response.status)
  }
})

