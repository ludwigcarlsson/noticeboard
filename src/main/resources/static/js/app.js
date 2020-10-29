const host = location.protocol + '//' + location.hostname + ':' + location.port

const apiBasePath = '/api/v1'

const path = host + apiBasePath

const contentContainer = document.querySelector('#contentContainer')

init()

async function init() {

    const isLoggedIn = await (await fetch(path + '/account/loginStatus')).json()

    if (!isLoggedIn) {

        const loginForm = document.querySelector('#loginTemplate').content.cloneNode(true)

        contentContainer.appendChild(loginForm)

        const form = document.querySelector('#loginForm')

        form.addEventListener('submit', async e => {

            e.preventDefault();

            const result = await fetch(path + '/account/login', {

                method: 'POST',

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify({

                    userName: form.querySelector('#userName').value,

                    password: form.querySelector('#password').value

                })

            })

            if (result.ok) {

                init()

                form.remove()

            }

            document.querySelector('#messages').textContent = result.ok ? "login successful" : "login unsuccessful. code: " + result.status

        })

    } else {

        const button = document.createElement('button')

        button.innerText = 'log out'

        contentContainer.appendChild(button)

        button.addEventListener('click', async e => {

            await fetch(path + '/account/logout')

            button.remove()

            init()

        })

    }

}
