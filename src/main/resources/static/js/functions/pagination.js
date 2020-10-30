import {render} from "/js/pages/home.js";

export function controls() { // append this to parent
    const paginationControls = document.createElement('div')
    paginationControls.setAttribute("id", "pagination-controls")
    paginationControls.classList.add("pagination-controls")
    return paginationControls
}

export function paginateItems(items, controls, rows_per_page, page) { // when calling, enter object, controls, how many rows to show and current page

    let current_page = page - 1
    let rows = 5

    let start = rows * current_page
    let end = start + rows

    setup_pagination(items.length, controls, rows, page)

    return items.slice(start, end)
}

export function setup_pagination(items, wrapper, rows_per_page, page) { // enter objects length, the wrapper for buttons and current page

    wrapper.innerHTML = ""
    let page_count = Math.ceil(items / rows_per_page)

    for (let i = 1; i < page_count + 1; i++) {
        let btn = pagination_button(i, page)
        wrapper.appendChild(btn)
    }
}

function pagination_button(page, current_page) { // enter page and the currently active page
    let button = document.createElement('button')
    button.classList.add('btn-dark')
    button.innerText = page

    if (current_page == page) {
        button.classList.add('active')
        button.classList.add('btn-dark')
    }

    button.addEventListener('click', function () {
        current_page = page
        render(current_page)
    })

    return button
}