import Api from "/js/functions/apiCalls.js"
import * as paginate from "/js/functions/pagination.js"

const contentContainer = document.querySelector('#contentContainer')
const paginationControls = paginate.controls();

export async function render(page) {
    const notices = await Api.parse(await Api.getAllNotices())

    let paginatedNotices = paginate.paginateItems(notices, paginationControls, 5, page)

    let elements = ''
    for (let i = 0; i < paginatedNotices.length; i++) {
        const card = document.createElement('template')
        card.innerHTML = `
          <div id="notices" class="notice-container">
            <div class="notice-item">
             <div><h3 class="notice-title"><a href="/#/notice/${paginatedNotices[i].id}">${paginatedNotices[i].title}</a></h3>
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

    const isLoggedIn = await Api.getLoginStatus()
    if(isLoggedIn) {
      const link = document.createElement('a')
      link.href = "/#/notice/create"
      link.innerText = 'create new notice'
      contentContainer.appendChild(link)
    }

    contentContainer.appendChild(paginationControls)
}



