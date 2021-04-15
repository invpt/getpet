requirePrivilegeLevel('any');

const onSubmit = ev => {
    ev.preventDefault();

    let intakeRequest = { animal: readForm(document.getElementById('intakeForm')) };

    intakeRequest.animal.intakeNumber = -1;

    apiCall({
        endpoint: `/animal/new`,
        method: 'POST',
        body: intakeRequest,
    })
    .then(resp => console.info('Got response from server after intake', resp))
    .catch(e => displayErrorPage('Internal error - failed to intake animal', e));
}

// Enable onSubmit
document.getElementById('intakeForm').addEventListener('submit', onSubmit);