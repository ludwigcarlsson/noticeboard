const msgTimeout = 5000
export default function addMessage(msg) {
  const messageContainer = document.querySelector('#messages')

  messageContainer.textContent = msg
  setTimeout(() => {
    messageContainer.textContent = ''
  }, msgTimeout)
}