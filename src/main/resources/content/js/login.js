const users = {
    'director': 'iAmDirector!!...',
    'assistant': 'AssistantToTheDirector.....',
};

const doLogin = username => {
    window.sessionStorage.setItem('role', username);
    window.location.href = '/search.html';
};

const displayLoginFailed = () => {
    elements.errorMessage.style.visibility = 'visible';
    elements.errorMessage.style.maxWidth = 'none';
    elements.errorMessage.style.minWidth = 'none';
};

elements.credentials.addEventListener('submit', ev => {
    ev.preventDefault();

    const loginDetails = readForm(elements.credentials);

    if (users[loginDetails.username] && users[loginDetails.username] === loginDetails.password)
        window.location.href = '/search.html';
    else
        displayLoginFailed();

    return false;
});