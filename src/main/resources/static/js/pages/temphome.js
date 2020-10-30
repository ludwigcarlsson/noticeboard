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
    <div id="notices" class="notice-container">
       <div class="notice-item">
      <div><h3 class="notice-title">${paginatedNotices[i].title}</h3>
      <button class="btn-sm btn-outline-dark float-right">Edit</button>
      </div>
      <div class="lead notice-description">${paginatedNotices[i].content}</div>
      <div class="float-right blockquote-footer notice-signature">${paginatedNotices[i].account} ${paginatedNotices[i].timestamp}</div>
      </div>
    </div>
    `
    elements += card.innerHTML
  }

  contentContainer.innerHTML = `
    <h1 class="text-center">Home</h1>
    ${elements}
  `
  contentContainer.appendChild(paginationControls)
}



