import * as createAccountPage from '/js/pages/createAccount.js'
import * as createNoticePage from '/js/pages/createNotice.js'
import * as loginPage from '/js/pages/login.js'
import * as viewAllNotices from '/js/pages/temphome.js'
import * as header from '/js/header.js'
import Api from '/js/functions/apiCalls.js'
import addMessage from '/js/functions/messages.js'

const routes = {
  home: '',
  error: 'error',
  createAccount: 'account/create',
  loginPage: 'login',
  logout: 'logout',
  createNotice: 'notice/create',
}

const requiresAuth = new Set([
  routes.createNotice
])

const redirects = new Set([
  routes.loginPage,
  routes.createAccount
])

setPage()

window.addEventListener('hashchange', () => {
  setPage()
})

async function setPage() {
  const isLoggedIn = await Api.getLoginStatus()

  header.render(isLoggedIn)

  let page = location.hash.substring(2)
  const pagePath = page.split('/')
  page = pagePath[0]

  if(!isLoggedIn && requiresAuth.has(page)) {
    addMessage('you must be logged in to view this page')
    location.hash = '/'
    return
  } else if(isLoggedIn && redirects.has(page)) {
    location.hash = '/'
    return
  }

  switch(page) {
    case routes.logout:
      await Api.logout()
      addMessage('you are now logged out')
      location.hash = '/'
      break;
    case routes.createAccount:
      createAccountPage.render()
      break;
    case routes.loginPage:
      loginPage.render()
      break;
    case routes.createNotice:
      createNoticePage.render()
      break;
    case routes.home:
      viewAllNotices.render()
      break;
    case routes.error:
    default:
      addMessage('404 site not found')
      // TODO 404 page
  }
}
