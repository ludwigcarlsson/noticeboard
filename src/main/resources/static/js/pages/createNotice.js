import Api from '../api/apiCalls.js'

document.querySelector('#contentContainer').innerHTML = `
<h2>Create new notice</h2>

<form>
  <div>Title</div>
  
  <div>
    <input type="text" id="title" autocomplete="off" autofocus>
  </div>

  <div>Content</div>
  
  <textarea id="content"></textarea>
  <button type="submit">Submit</button>
</form>
`

contentContainer.addEventListener('submit', async e => {
  e.preventDefault()
  const form = contentContainer.querySelector('form')

  const title = form.querySelector('#title').value
  const content = form.querySelector('#content').value

  const response = await Api.newNotice(title, content)

  if(response.ok) {
    form.reset()
    console.log('notice created')
  } else if(response.status === 400) {
    console.log('could not create notice. required fields are missing')
  } else if(response.status === 401) {
    console.log('you are not logged in')
  } else {
    console.log('error: ' + response.status)
  }
})
