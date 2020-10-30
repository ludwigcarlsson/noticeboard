import Api from '../functions/apiCalls.js'
import addMessage from '../functions/messages.js'

const contentContainer = document.querySelector('#contentContainer')

export async function render() {
    contentContainer.innerHTML = `
    <form class="login-container" id="loginForm">
    
    <table>
        <div class="form-group">
        <tr>
        
            <td class="">Username:</td>
            <td>
                <input id="userName" class="form-control input-margin" type="text" autocomplete="off"
                        placeholder="Enter username" required>
            </td>
        </tr>
        </div>
        <div class="form-group">
        <tr>
            <td>Password:</td>
            <td>
                <input id="password" class="form-control input-margin " type="password"
                        placeholder="Enter password" required>
            </td>
        </tr>
        </div>
    </table>
     <div class="form-group">     
                <button class="btn btn-primary btn-lg mb-2 btn-placement">Login</button>
        </div>
    </form>
    `

    contentContainer.querySelector('#userName').focus()

    // const isLoggedIn = await Api.getLoginStatus()

    // if (true || !isLoggedIn) {
    const form = document.querySelector('#loginForm')

    form.addEventListener('submit', async e => {
        e.preventDefault();

        const userName = form.querySelector('#userName').value
        const password = form.querySelector('#password').value

        const response = await Api.login(userName, password)
        if (response.ok) {
            // TODO navigate
            addMessage('log in successful')
            location.hash = '/'
        } else {
            addMessage('wrong account credentials')
        }
    })
    // } else {
    // TODO navigate to home?
    // }
}