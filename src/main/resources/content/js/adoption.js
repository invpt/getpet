requirePrivilegeLevel('any');

let animal;

const fillDetails = response => {
    animal = response.animal;

    if (animal) {
        document.getElementById('animalName').innerText = animal.name;
        document.getElementById('animalIntakeNumber').innerText = animal.intakeNumber;
    }
}

const submit = ev => {
    ev.preventDefault();

    console.log('Sending animal delete request for animal', intakeNumber);

    apiCall({
        endpoint: `/animal/${intakeNumber}`,
        method: 'DELETE'
    })
        .catch(e => displayErrorPage(-1, 'Internal error - failed to adopt animal', e));


    return false;
}

document.getElementById('submitButton').addEventListener('click', submit);

const intakeNumber = parseInt(new URLSearchParams(window.location.search).get('intakeNumber'));
if (!searchParams.has('intakeNumber'))
    displayErrorPage(-1, 'Internal error - invalid or nonexistent intake number');
else
    apiCall({ endpoint: `/animal/${intakeNumber}` })
        .then(resp => resp.json())
        .then(fillDetails)
        .catch(e => displayErrorPage(-1, null, e));