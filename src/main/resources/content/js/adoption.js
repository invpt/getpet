requirePrivilegeLevel('any');

let animal;

const fillDetails = response => {
    animal = response.animal;

    if (animal) {
        document.getElementById('animalName').innerText = animal.name;
        document.getElementById('animalIntakeNumber').innerText = animal.intakeNumber;
        document.getElementById('animalCageNumber').innerText = animal.cageNumber;
    }
}

const submit = ev => {
    ev.preventDefault();

    apiCall({
        endpoint: `/animal/${intakeNumber}`,
        method: 'DELETE'
    })
        .then(_ => window.history.back())
        .catch(e => displayError('failed to adopt animal', e));

    return false;
}

document.getElementById('submitButton').addEventListener('click', submit);

const intakeNumber = parseInt(new URLSearchParams(window.location.search).get('intakeNumber'));
if (!intakeNumber)
    displayError('Invalid or nonexistent intake number');
else
    apiCall({ endpoint: `/animal/${intakeNumber}` })
        .then(fillDetails)
        .catch(e => displayError(null, e));