requirePrivilegeLevel('any');

elements.intakeForm.addEventListener('submit', ev => {
    ev.preventDefault();

    let updateRequest = readForm(elements.intakeForm);

    updateRequest.intakeNumber = -1;

    console.log('Sending intake request with body', updateRequest);

    fetch('/persistence/addAnimal', {
        method: 'POST',
        body: JSON.stringify(updateRequest),
    })
        .then(resp => resp.json())
        .then(resp => console.log('Got response from server after intake', resp))
        .catch(e => console.log("ERROR: " + e));
});