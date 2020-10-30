import Api from "/js/functions/apiCalls.js"
import * as paginate from "/js/functions/pagination.js"

const contentContainer = document.querySelector('#contentContainer')
const paginationControls = paginate.controls();

export async function render(page) {
  
  const notices = await Api.parse(await Api.getAllNotices())

  let paginatedNotices = paginate.paginateItems(notices, paginationControls, 5, page)
  
  let elements = ''
  for(let i = 0; i < paginatedNotices.length; i++) {
    const card = document.createElement('template')
    card.innerHTML = `
    <a href="${'/#/notice/' + paginatedNotices[i].id}" style="padding: 10px; border: 1px solid black; font-size: 16px; display: block" id="notices">
      <div style="font-size: 20px">${paginatedNotices[i].title}</div>
      <div>${paginatedNotices[i].content}</div>
    </a>
    `
    elements += card.innerHTML
  }

  contentContainer.innerHTML = `
    <h1>Home</h1>
    ${elements}
  `
  contentContainer.appendChild(paginationControls)
}



