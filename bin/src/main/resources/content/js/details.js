let animal;

// Fills in the form fields with the animal's preexisting details
const fillDetails = response => {
    animal = response.animal;

    const setRadio = (elementId, name) => {
        const element = document.getElementById(elementId);
        element.querySelector(`input[value="${name}"]`).checked = true;
    };
    const setCheckboxes = (elementId, names) => {
        const element = document.getElementById(elementId);

        for (const name of names)
            element.querySelector(`input[value="${name}"]`).checked = true;
    };

    if (animal) {
        document.getElementById('optionName').value = animal.name;
        document.getElementById('optionBreed').value = animal.breed;
        document.getElementById('optionWeight').value = animal.weight;
        document.getElementById('optionIntakeNumber').value = animal.intakeNumber;
        document.getElementById('optionCageNumber').value = animal.cageNumber;
        setRadio('optionSize', animal.size);
        setRadio('optionGender', animal.gender);
        setRadio('optionSpecies', animal.species);
        setCheckboxes('optionColor', animal.colors);
        document.getElementById('optionVaccinated').checked = animal.vaccinated;
        document.getElementById('optionSpayNeuter').checked = animal.spayNeuter;
    }
}

const onSubmit = ev => {
    ev.preventDefault();

    let updateRequest = { animal: readForm(document.getElementById('detailsForm')) };
    
    let intakeNumber = updateRequest.animal.intakeNumber;
    delete updateRequest.animal.intakeNumber;

    apiCall({
        endpoint: `/animal/${intakeNumber}`,
        method: 'PUT',
        body: updateRequest,
    })
    .then(resp => console.info('Got response from server after update', resp))
    .catch(e => displayError('failed to update animal', e));
}

// Enable onSubmit
document.getElementById('detailsForm').addEventListener('submit', onSubmit);

requirePrivilegeLevel('any');

if (hasPrivilegeLevel('director'))
    document.getElementById('buttonEuthanize').onclick = () => {
        if (confirm(`Are you sure you want to mark ${animal.name} as euthanized?`))
        apiCall({
            endpoint: `/animal/${intakeNumber}`,
            method: 'DELETE'
        })
            .then(_ => window.history.back())
            .catch(e => displayError('failed to mark animal as euthanized', e));
    };
else
    document.getElementById('buttonEuthanize').style.display = 'none';

const intakeNumber = parseInt(new URLSearchParams(window.location.search).get('intakeNumber'));
if (!intakeNumber)
    displayError('invalid intake number');
else
    apiCall({ endpoint: `/animal/${intakeNumber}` })
        .then(fillDetails)
        .catch(e => displayError('That animal does not exist', e));