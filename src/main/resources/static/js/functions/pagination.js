import {render} from "/js/pages/temphome.js";

export function controls() {
    const paginationControls = document.createElement('div')
    paginationControls.setAttribute("id", "pagination-controls")
    paginationControls.classList.add("pagination-controls")
    return paginationControls
}

export function paginateItems(notices, controls, rows_per_page, page) {

    let current_page = page - 1
    let rows = 5

    let start = rows * current_page
    let end = start + rows

    setup_pagination(notices.length, controls, rows, page)

    return notices.slice(start, end)
}

export function setup_pagination (items, wrapper, rows_per_page, page) {

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