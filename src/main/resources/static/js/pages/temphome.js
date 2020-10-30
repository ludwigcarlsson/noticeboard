import Api from "/js/functions/apiCalls.js"

const contentContainer = document.querySelector('#contentContainer')
const paginationControls = document.createElement('div')
paginationControls.setAttribute("id", "pagination-controls")
paginationControls.classList.add("pagination-controls")

export async function render(page) {
  
  const notices = await Api.parse(await Api.getAllNotices())

  let current_page = page - 1
  let rows = 5

  let start = rows * current_page
  let end = start + rows
  let paginatedNotices = notices.slice(start, end)
  
  let elements = ''
  let pagination = ''
  for(let i = 0; i < paginatedNotices.length; i++) {
    const card = document.createElement('template')
    card.innerHTML = `
    <div style="padding: 10px; border: 1px solid black; font-size: 16px" id="notices">
      <div style="font-size: 20px">${paginatedNotices[i].title}</div>
      <div>${paginatedNotices[i].content}</div>
    </div>
    `
    elements += card.innerHTML
  }

  setup_pagination(notices.length, paginationControls, rows, page)

  contentContainer.innerHTML = `
    <h1>Home</h1>
    ${elements}
  `
  contentContainer.appendChild(paginationControls)
}

function setup_pagination (items, wrapper, rows_per_page, page) {

  wrapper.innerHTML = ""
  let page_count = Math.ceil(items / rows_per_page)

  for (let i = 1; i < page_count + 1; i++) {
      let btn = pagination_button(i, page)
      wrapper.appendChild(btn)
  }
}

function pagination_button(page, current_page) {
  let button = document.createElement('button')
  button.innerText = page

  if (current_page == page) { button.classList.add('active') }

  button.addEventListener('click', function () {
    current_page = page
    render(current_page)
  })

  return button
}


