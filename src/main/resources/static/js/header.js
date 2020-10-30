export async function render(isLoggedIn) {
    const header = document.getElementById('header')
    header.innerHTML = `
    <header>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark padding-nav"> 
            <div class="center-title"> 
                <a class="navbar-brand margin-nav nav-title-size" href="#">Noticeboard</a> 
            </div> 
    
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation"> 
                <span class="navbar-toggler-icon"></span> 
            </button> 
            <div class="collapse navbar-collapse" id="navbarNavAltMarkup"> 
                <div class="navbar-nav">
                    <a class="nav-item nav-link active" href="/#/">Home <span class="sr-only">(current)</span></a>
                    <a class="nav-item nav-link loggedout" href="/#/account/create">Sign up</a>
                    <a class="nav-item nav-link loggedout" href="/#/login">Sign in</a>
                    <a class="nav-item nav-link loggedin" href="/#/logout">Logout</a>
                </div>
            </div> 
        </nav> 
    </header>
    `

    if(isLoggedIn) {
        header.querySelectorAll('.loggedout').forEach(button => button.remove())
    } else {
        header.querySelectorAll('.loggedin').forEach(button => button.remove())
    }
}
