requirePrivilegeLevel('any');

const onSubmit = ev => {
    ev.preventDefault();

    let intakeRequest = readForm(document.getElementById('intakeForm'));

    intakeRequest.intakeNumber = -1;

    apiCall({
        endpoint: `/animal/new`,
        method: 'POST',
        body: intakeRequest,
    })
    .then(resp => console.info('Got response from server after intake', resp))
    .catch(e => displayErrorPage(-1, 'Internal error - failed to intake animal', e));
}

// Enable onSubmit
document.getElementById('intakeForm').addEventListener('submit', onSubmit);