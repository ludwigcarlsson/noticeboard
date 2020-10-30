const msgTimeout = 5000
let timeout

export default function addMessage(msg) {
  const messageContainer = document.querySelector('#messages')
  clearTimeout(timeout)

  messageContainer.textContent = msg
  timeout = setTimeout(() => {
    messageContainer.textContent = ''
  }, msgTimeout)
}