requirePrivilegeLevel('any');

const euthanize = intakeNumber => {
    const request = { intakeNumber };

    console.log('Sending euthanization request with body', request);

    fetch("/persistence/euthanize", {
        method: 'POST',
        body: JSON.stringify(request),
    })
        .catch(e => console.log("ERROR: " + e));
}

let animal;

const fakeSubmit = ev => {
    ev.preventDefault();

    const request = { intakeNumber: animal.intakeNumber };

    console.log('Sending euthanization request with body', request);

    fetch("/persistence/euthanize", {
        method: 'POST',
        body: JSON.stringify(request),
    })
        .then(_ => window.location.reload())
        .catch(e => console.log("ERROR: " + e));


    return false;
}

document.getElementById('fakeSubmitButton').addEventListener('click', fakeSubmit);


///////////////////////////////// Fill in details ?///////////////////////////////////////////
const fillDetails = response => {
    console.log("resp", response);

    const setRadio = (elementId, name) => {
        const element = document.getElementById(elementId);
        element.querySelector('input[value="' + name + '"]').checked = true;
    };
    const setCheckboxes = (elementId, names) => {
        const element = document.getElementById(elementId);
        for (const name of names) {
            element.querySelector('input[value="' + name + '"]').checked = true;
        }
    };

    animal = response.animal;

    if (animal) {
        document.getElementById('animalName').innerText = animal.name;
        document.getElementById('animalIntakeNumber').innerText = animal.intakeNumber;
    }
}

const searchParams = new URLSearchParams(window.location.search);

if (!searchParams.has('intakeNumber')) {
    // TODO: This shows exactly no information to the user that something has gone wrong
    console.error("Internal error");
} else {
    const intakeNumber = parseInt(searchParams.get('intakeNumber'));

    fetch("/persistence/animal", {
        method: 'POST',
        body: JSON.stringify({ intakeNumber }),
    })
        .then(resp => resp.json())
        .then(fillDetails)
        .catch(e => console.log("ERROR: " + e));
}