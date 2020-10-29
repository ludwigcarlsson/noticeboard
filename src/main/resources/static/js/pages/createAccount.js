import Api from '../functions/apiCalls.js'
import addMessage from '../functions/messages.js'

export function render() {
  const contentContainer = document.querySelector('#contentContainer')
  contentContainer.innerHTML = `
  <h2>Create new account</h2>

  <form id="createAccountForm">
    <table>
      <tr>
        <td>User name</td>
        <td>
          <input type="text" id="userName" autocomplete="off">
        </td>
      </tr>
      <tr>
        <td>Password</td>
        <td>
          <input type="password" id="password">
        </td>
      </tr>
      <tr>
        <td>Email</td>
        <td>
          <input type="text" id="email" autocomplete="off">
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <button type="submit">Submit</button>
      </tr>
    </table>
  </form>
  `

  contentContainer.querySelector('#userName').focus()
  const form = contentContainer.querySelector('#createAccountForm')

  form.addEventListener('submit', async e => {
    e.preventDefault()
    
    const userName = form.querySelector('#userName').value
    const password = form.querySelector('#password').value
    const email = form.querySelector('#email').value

    const result = await Api.createAccount(userName, password, email)
    if(result.ok) {
      await Api.login(userName, password)
      location.hash = '/'
    }

    addMessage(result.ok ? 'account created' : (result.status === 409 ? 'username and/or email is taken' : 'error: ' + result.status))
  })
}