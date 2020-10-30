import Api from '../functions/apiCalls.js'
import addMessage from '../functions/messages.js'

const contentContainer = document.querySelector('#contentContainer')

export async function render() {
    contentContainer.innerHTML = `
    <form class="login-container" id="loginForm">
    <table>
        <tr>
            <td class="">Username:</td>
            <td>
                <input id="userName" class="form-control input-margin" type="text" autocomplete="off"
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