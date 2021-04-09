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

const intakeNumber = parseInt(new URLSearchParams(window.location.search).get('intakeNumber'));
if (!searchParams.has('intakeNumber'))
    displayErrorPage(-1, 'Internal error - invalid or nonexistent intake number');
else
    fetch(`/persistence/animal/${intakeNumber}`)
        .then(fillDetails)
        .catch(e => displayErrorPage(-1, null, e));