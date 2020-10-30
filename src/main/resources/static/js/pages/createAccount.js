import Api from '../functions/apiCalls.js'
import addMessage from '../functions/messages.js'

export function render() {
    const contentContainer = document.querySelector('#contentContainer')
    contentContainer.innerHTML = `
  <h2 class="text-center">Create new account</h2>

  <form id="createAccountForm" class="fl">
    <table>
    <div class="form-group">
      <tr>
        <td>User name</td>
        <td>
          <input type="text" id="userName" autocomplete="off" required>
        </td>
      </tr>
      </div>
      <div class="form-group">
      <tr>
        <td>Password</td>
        <td>
          <input type="password" id="password" required>
        </td>
      </tr>
      </div>
      <div class="form-group">
      <tr>
        <td>Email</td>
        <td>
          <input type="email" id="email" autocomplete="off" required>
        </td>
      </tr>
     </div>
    </table>
    <div class="form-group">
    <button class="btn btn-primary btn-lg float-right" type="submit">Submit</button>
    </div>
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
        if (result.ok) {
            await Api.login(userName, password)
            location.hash = '/'
        }

        addMessage(result.ok ? 'account created' : (result.status === 409 ? 'username and/or email is taken' : 'error: ' + result.status))
    })
}