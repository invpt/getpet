let animal;

const euthanize = intakeNumber => {
    console.info('Sending euthanization request for animal with intake number', intakeNumber);

    fetch(`/persistence/animal/${intakeNumber}`, { method: 'DELETE' })
        .catch(e => console.error("error euthanizing animal:", e));
}

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
        document.getElementById('optionVaccinated').checked = true;
        document.getElementById('optionSpayNeuter').checked = true;
    }
}

const onSubmit = ev => {
    ev.preventDefault();

    let updateRequest = readForm(document.getElementById('detailsForm'));

    apiCall({
        endpoint: `/animal/${updateRequest.intakeNumber}`,
        method: 'POST',
        body: updateRequest,
    })
    .then(resp => console.info('Got response from server after update', resp))
    .catch(e => displayErrorPage(-1, 'Internal error - failed to update animal', e));
}

// Enable onSubmit
document.getElementById('detailsForm').addEventListener('submit', onSubmit);

requirePrivilegeLevel('any');

if (hasPrivilegeLevel('director'))
    document.getElementById('buttonEuthanize').onclick = () => {
        if (confirm(`Are you sure you want to mark ${animal.name} as euthanized?`))
            euthanize(animal.intakeNumber);
    };
else
    document.getElementById('buttonEuthanize').style.display = 'none';

const intakeNumber = parseInt(new URLSearchParams(window.location.search).get('intakeNumber'));
if (!searchParams.has('intakeNumber'))
    displayErrorPage(-1, 'Internal error - invalid or nonexistent intake number');
else
    fetch(`/persistence/animal/${intakeNumber}`)
        .then(fillDetails)
        .catch(e => console.log("ERROR: " + e));