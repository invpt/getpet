const users = {
    'director': 'iAmDirector!!...',
    'assistant': 'AssistantToTheDirector.....',
};

const loginAs = username => {
    window.sessionStorage.setItem('role', username);
    window.location = window.location.origin + '/';
};

const displayLoginFailed = () => {
    const errorMessageElement = document.getElementById('errorMessage');
    errorMessageElement.style.visibility = 'visible';
    errorMessageElement.style.maxWidth = 'none';
    errorMessageElement.style.minWidth = 'none';
};

const onSubmit = ev => {
    ev.preventDefault();

    const loginDetails = readForm(document.getElementById('credentials'));

    console.info('login occurred with details', loginDetails);

    if (users[loginDetails.username] && users[loginDetails.username] === loginDetails.password)
        loginAs(loginDetails.username);
    else
        displayLoginFailed();

    return false;
}

document.getElementById('credentials').addEventListener('submit', onSubmit);