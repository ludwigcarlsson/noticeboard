import Api from '../api/apiCalls.js'

const contentContainer = document.querySelector('#contentContainer')
contentContainer.innerHTML = `
<form class="login-container" id="loginForm">
  <table>
      <tr>
          <td class="">Username:</td>
          <td>
              <input id="userName" class="form-control input-margin" type="text" autofocus autocomplete="off"
                    placeholder="Enter username" required>
          </td>
      </tr>
      <tr>
          <td>Password:</td>
          <td>
              <input id="password" class="form-control input-margin" type="password"
                    placeholder="Enter password" required>
          </td>
      </tr>
      <tr>
          <td>
              <button class="btn btn-primary btn-placement">Login</button>
          </td>
      </tr>
  </table>
</form>
`

init()

async function init() {
    const isLoggedIn = await Api.getLoginStatus()
    console.log(isLoggedIn)

    if (true || !isLoggedIn) {
        const form = document.querySelector('#loginForm')

        form.addEventListener('submit', async e => {
            e.preventDefault();
            
            const userName = form.querySelector('#userName').value
            const password = form.querySelector('#password').value

            const response = await Api.login(userName, password)
            if (response.ok) {
                // TODO navigate
                console.log('log in successful')
            } else {
                console.log('log in failed')
            }
        })

    } else {
        // TODO navigate to home?
    }
}
