import Api from "/js/functions/apiCalls.js"

const contentContainer = document.querySelector('#contentContainer')

export async function render() {
  
  const notices = await Api.parse(await Api.getAllNotices())
  
  let elements = ''
  for(const notice of notices) {
    const card = document.createElement('template')
    card.innerHTML = `
    <div style="padding: 10px; border: 1px solid black; font-size: 16px">
      <div style="font-size: 24px">${notice.title}</div>
      <div>${notice.content}</div>
    </div>
    `
    elements += card.innerHTML
  }
  
  contentContainer.innerHTML = `
    <h1>Home<h1>
    ${elements}
  `
}
