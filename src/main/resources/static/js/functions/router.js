import * as createAccountPage from '../pages/createAccount.js'
import * as createNoticePage from '../pages/createNotice.js'
import * as loginPage from '../pages/login.js'
import * as viewAllNotices from '../pages/temphome.js'

const routes = { 
  createAccount: 'createAccount',
  loginPage: 'login',
  createNotice: 'createNotice'
}

setPage()

window.addEventListener('hashchange', () => {
  setPage()
})

function setPage() {
  const page = location.hash.substring(2)
  
  switch(page) {
    case routes.createAccount:
      createAccountPage.render()
      break;
    case routes.loginPage:
      loginPage.render()
      break;
    case routes.createNotice:
      createNoticePage.render()
      break;
    default:
      viewAllNotices.render()
  }
}
