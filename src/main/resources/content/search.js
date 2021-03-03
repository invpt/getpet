const elementsToFind = ['searchForm', 'result'];
const elements = {};
for (const elementToFind of elementsToFind)
    elements[elementToFind] = document.getElementById(elementTofind);

const readForm = form => {
    values = {};

    let currentName;
    let currentValue;
    for (const formElement of form.elements) {
        if (formElement.name) {
            if (formElement.name !== currentName) {
                if (formElement.name === 'breed')
                    console.log(formElement);

                if (currentName && currentValue)
                    if (currentValue.length === 0)
                        values[currentName] = null;
                    else if (currentValue.length === 1)
                        values[currentName] = currentValue[0];
                    else
                        values[currentName] = currentValue;

                currentName = formElement.name;
                currentValue = [];
            }

            if ((formElement.type !== 'radio' && formElement.type !== 'checkbox') || formElement.checked)
                currentValue.push(formElement.value);
        }
    }
    if (currentName && currentValue)
        if (currentValue.length === 0)
            values[currentName] = null;
        else if (currentValue.length === 1)
            values[currentName] = currentValue[0];
        else
            values[currentName] = currentValue;

    return values;
};

const displaySearchResults = results => {
    const resultBox = elements.result;

    for (const result of results) {
        const resultDiv = document.createElement('div');
        resultDiv.classList.add('result');

        const animalInfo = document.createElement('div');
        animalInfo.classList.add('animalInfo');
        resultDiv.appendChild(animalInfo);

        const animalName = document.createElement('div');
        animalName.classList.add('animalName');
        animalInfo.appendChild(animalName);

        const nameAttributeName = document.createElement('span');
        nameAttributeName.classList.add('animalAttributeName');
        nameAttributeName.innerText = 'Name:';
        animalName.appendChild(nameAttributeName);

        const nameAttributeValue = document.createElement('span');
        nameAttributeValue.classList.add('animalAttributeValue');
        nameAttributeValue.innerText = result.name;
        animalName.appendChild(nameAttributeValue);

        const animalSubInfo = document.createElement('div');
        animalSubInfo.classList.add('animalSubInfo');
        animalInfo.appendChild(animalSubInfo);

        const leftAttributePair = document.createElement('div');
        leftAttributePair.classList.add('animalAttributePair');
        leftAttributePair.classList.add('leftAttributePair');
        animalSubInfo.appendChild(leftAttributePair);

        const breedAttributeName = document.createElement('span');
        breedAttributeName.classList.add('animalAttributeName');
        breedAttributeName.innerText = 'Breed:';
        leftAttributePair.appendChild(breedAttributeName);

        const breedAttributeValue = document.createElement('span');
        breedAttributeValue.classList.add('animalAttributeValue');
        breedAttributeValue.innerText = result.breed;
        leftAttributePair.appendChild(breedAttributeName);
    }
}

searchForm.addEventListener('submit', ev => {
    ev.preventDefault();

    let searchRequest = readForm(searchForm);

    fetch("/persistence/search", {
        method: 'POST',
        body: JSON.stringify(searchRequest),
    })
        .then(resp => resp.json())
        .then(displaySearchResults)
        .catch(e => console.log("ERROR: " + e));

    return false;
});