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
                <a id="home" class="nav-item nav-link active" href="/#/home">Home <span class="sr-only">(current)</span></a> 
                <a id="signup" class="nav-item nav-link" href="/#/createAccount">Sign up</a> 
                <a id="login" class="nav-item nav-link" href="/#/login">Sign in</a> 
            </div> 
        </div> 
    </nav> 
</header>
`