import Api from '/js/functions/apiCalls.js'
import addMessage from '/js/functions/messages.js'
import * as router from '/js/router.js'

const actions = { edit: 1, delete: 2 }

export async function render(noticeId) {

  const getNoticeResponse = await Api.getNotice(noticeId)
  if (getNoticeResponse.ok) {
    const notice = await Api.parse(getNoticeResponse)
    
    const accountResponse = await Api.getAccountId()
    let accountId
    if (accountResponse.status === 200) {
      accountId = await Api.parse(accountResponse)
    }
    const isLoggedIn = accountId != null

    const elements = document.createElement('div')

    const noticeCard = document.createElement('div')
    noticeCard.innerHTML = `
    <div class="post">
      <div id="title">${notice.noticeTitle}</div>
      <div id="content">${notice.noticeContent}</div>
      <div id="bottombar">
        <div id="buttons">
          <a id="editNotice">edit</a>
          <a id="deleteNotice">delete</a>
        </div>
        <div id="author">
          <div>${notice.noticeUserName}</div>
          <div>${'posted ' + notice.noticeTimestamp}</div>
          <div>${notice.editedTimestamp ? 'edited ' + notice.editedTimestamp : ''}</div>
        </div>
      </div>
    </div>
    <hr>
    `

    if (accountId !== notice.accountId) {
      noticeCard.querySelector('#buttons').remove()
    }
    elements.appendChild(noticeCard)

    for (const comment of notice.comments) {
      const card = document.createElement('div')
      card.innerHTML = `
      <div class="post" id="${comment.id}">
        <div id="content">${comment.content}</div>
        <div id="bottombar">
          <div id="buttons">
            <a id="editComment">edit</a>
            <a id="deleteComment">delete</a>
          </div>
          <div id="author">
            <div>${comment.account}</div>
            <div>${'posted ' + comment.timestamp}</div>
            <div>${comment.editedTimestamp ? 'edited ' + comment.editedTimestamp : ''}</div>
          </div>
        </div>
      </div>
      <hr>
      `
      if (accountId !== comment.accountId) {
        card.querySelector('#buttons').remove()
      }
      elements.appendChild(card)
    }

    const contentContainer = document.querySelector('#contentContainer')
    contentContainer.innerHTML = ''
    contentContainer.appendChild(elements)

    if (isLoggedIn) {
      const addCommentForm = document.createElement('form')
      addCommentForm.innerHTML = `      
      <textarea placeholder="Write something..."></textarea>
      <button id="addBtn" type="submit">Add comment</button>
      `
      contentContainer.appendChild(addCommentForm)

      addCommentForm.addEventListener('submit', async e => {
        e.preventDefault()
        const content = addCommentForm.querySelector('textarea').value
        const response = await Api.newComment(noticeId, content)

        if (response.ok) {
          addMessage('comment posted')
        } else {
          let msg
          switch (response.status) {
            case 400:
              msg = 'enter content'
              break;
            case 401:
              msg = 'you are not logged in'
              break;
            case 404:
              msg = 'notice not found'
              break;
            default:
              msg = reponse.status
          }
          addMessage('error ' + msg)
        }
        render(noticeId)
      })
    }

    let response, isNoticeAction
    elements.addEventListener('click', async e => {
      if (e.target.id === 'editNotice' || e.target.id === 'editComment') {
        // Show edit controls
        isNoticeAction = e.target.id === 'editNotice'
        const post = getPost(e.target)
        const commentId = post.id
        const contentDiv = post.querySelector('#content')
        const buttons = post.querySelector('#buttons')
        buttons.classList.add('gone')

        const editControllers = document.createElement('div')
        let titleDiv
        if (isNoticeAction) {
          titleDiv = post.querySelector('#title')
          titleDiv.remove()
          editControllers.innerHTML = `
          <input type="text" value="${titleDiv.innerText}">
          `
        }
        editControllers.innerHTML += `
        <textarea>${contentDiv.innerText}</textarea>
        <button id="editBtn" type="submit">Edit</button>
        <button id="cancelBtn" type="reset">Cancel</button>
        `

        contentDiv.replaceWith(editControllers)
        editControllers.addEventListener('click', async e2 => {
          e2.preventDefault()

          if (e2.target.id === 'editBtn') {
            const newContent = editControllers.querySelector('textarea').value
            if (isNoticeAction) {
              // Update notice
              const newTitle = editControllers.querySelector('input').value
              response = await Api.updateNotice(noticeId, newTitle, newContent)
            } else {
              // Update comment
              response = await Api.updateComment(noticeId, commentId, newContent)
            }
            displayFeedbackMsg(response, isNoticeAction, actions.edit)
            render(noticeId)
          } else if (e2.target.id === 'cancelBtn') {
            // Cancel edit
            if (isNoticeAction) {
              editControllers.replaceWith(titleDiv, contentDiv)
            } else {
              editControllers.replaceWith(contentDiv)
            }
            buttons.classList.remove('gone')
          }
        })


      } else if (e.target.id === 'deleteNotice') {
        // Delete notice
        isNoticeAction = true
        response = await Api.deleteNotice(noticeId)
        displayFeedbackMsg(response, isNoticeAction, actions.delete)
        router.navigate('')

      } else if (e.target.id === 'deleteComment') {
        // Delete comment
        isNoticeAction = false
        const commentId = getPost(e.target).id
        response = await Api.deleteComment(noticeId, commentId)
        displayFeedbackMsg(response, isNoticeAction, actions.delete)
        render(noticeId)
      }
    })

    function getPost(target) {
      let parent = target.parentNode
      while (!parent.classList.contains('post')) {
        parent = parent.parentNode
      }
      return parent
    }
  }

  function displayFeedbackMsg(response, isNoticeAction, action) {
    if (response.ok) {
      addMessage((isNoticeAction ? 'notice' : 'comment') + ' ' + (action === actions.edit ? 'edited' : 'deleted'))
    } else {
      let reason
      switch (response.status) {
        case 404:
          reason = isNoticeAction ? 'notice' : 'comment'
          break;
        case 401:
          reason = 'you are not logged in'
          break;
        case 400:
          reason = 'all fields needs to be filled in'
        default:
          reason = response.status
      }
      addMessage('error ' + (action === actions.edit ? 'editing ' : 'deleting ') + (isNoticeAction ? 'notice' : 'comment') + ': ' + reason)
    }
  }

}
