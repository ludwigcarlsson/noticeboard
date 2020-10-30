import Api from '../functions/apiCalls.js'
import addMessage from '../functions/messages.js'
import * as router from '/js/router.js'

export function render() {
  document.querySelector('#contentContainer').innerHTML = `
  <h2>Create new notice</h2>

  <form>
    <div>Title</div>
    
    <div>
      <input type="text" id="title" autocomplete="off">
    </div>

    <div>Content</div>
    
    <textarea id="content"></textarea>
    <button type="submit">Submit</button>
  </form>
  `

  contentContainer.querySelector('#title').focus()

  contentContainer.addEventListener('submit', async e => {
    e.preventDefault()
    const form = contentContainer.querySelector('form')

    const title = form.querySelector('#title').value
    const content = form.querySelector('#content').value

    const response = await Api.newNotice(title, content)

    if(response.ok) {
      const newNotice = await Api.parse(response)
      addMessage('notice created')
      location.hash = '/notice/' + newNotice.id
      // router.navigate('notice/' + notice.id)
    } else if(response.status === 400) {
      addMessage('could not create notice. required fields are missing')
    } else if(response.status === 401) {
      addMessage('you are not logged in')
    } else {
      addMessage('error: ' + response.status)
    }
  })
}