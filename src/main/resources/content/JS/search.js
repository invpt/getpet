const elementsToFind = ['searchForm', 'result'];
const elements = {};
for (const elementToFind of elementsToFind)
    elements[elementToFind] = document.getElementById(elementToFind);

const readForm = form => {
    values = {};

    for (const option of form.querySelectorAll('.option')) {
        const name = option.getAttribute('name');
        const type = option.getAttribute('type');

        switch (type) {
            case 'radio':
                values[name] = form.elements[name].value !== '' ? form.elements[name].value : null;
                break;
            case 'checkbox':
                values[name] = [];
                for (const checkbox of option.querySelectorAll('input'))
                    if (checkbox.checked)
                        values[name].push(checkbox.value);
                break;
            default:
                values[name] = option.querySelector('input').value;
        }
    }

    return values;
};

const displaySearchResults = results => {
    while (elements.result.firstChild)
        elements.result.removeChild(elements.result.firstChild);

    console.log('Got response with results', results);

    for (const result of results.results) {
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

        animalName.appendChild(document.createTextNode(' '));

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

        leftAttributePair.appendChild(document.createTextNode(' '));

        const breedAttributeValue = document.createElement('span');
        breedAttributeValue.classList.add('animalAttributeValue');
        breedAttributeValue.innerText = result.breed;
        leftAttributePair.appendChild(breedAttributeValue);

        const leftBr = document.createElement('br');
        leftAttributePair.appendChild(leftBr);

        const colorAttributeName = document.createElement('span');
        colorAttributeName.classList.add('animalAttributeName');
        colorAttributeName.innerText = 'Color:';
        leftAttributePair.appendChild(colorAttributeName);

        leftAttributePair.appendChild(document.createTextNode(' '));

        const colorAttributeValue = document.createElement('span');
        colorAttributeValue.classList.add('animalAttributeValue');
        colorAttributeValue.innerText = result.colors.map(val => readableValues.color[val]).join(', ');
        leftAttributePair.appendChild(colorAttributeValue);

        const rightAttributePair = document.createElement('div');
        rightAttributePair.classList.add('animalAttributePair');
        rightAttributePair.classList.add('rightAttributePair');
        animalSubInfo.appendChild(rightAttributePair);

        const sizeAttributeName = document.createElement('span');
        sizeAttributeName.classList.add('animalAttributeName');
        sizeAttributeName.innerText = 'Size:';
        rightAttributePair.appendChild(sizeAttributeName);

        rightAttributePair.appendChild(document.createTextNode(' '));

        const sizeAttributeValue = document.createElement('span');
        sizeAttributeValue.classList.add('animalAttributeValue');
        sizeAttributeValue.innerText = readableValues.size[result.size];
        rightAttributePair.appendChild(sizeAttributeValue);

        const rightBr = document.createElement('br');
        rightAttributePair.appendChild(rightBr);

        const genderAttributeName = document.createElement('span');
        genderAttributeName.classList.add('animalAttributeName');
        genderAttributeName.innerText = 'Gender:';
        rightAttributePair.appendChild(genderAttributeName);

        rightAttributePair.appendChild(document.createTextNode(' '));

        const genderAttributeValue = document.createElement('span');
        genderAttributeValue.classList.add('animalAttributeValue');
        genderAttributeValue.innerText = readableValues.gender[result.gender];
        rightAttributePair.appendChild(genderAttributeValue);

        const detailsButton = document.createElement('button');
        detailsButton.classList.add('button');
        detailsButton.innerText = 'Details';
        resultDiv.appendChild(detailsButton);

        elements.result.appendChild(resultDiv);
    }
}

searchForm.addEventListener('submit', ev => {
    ev.preventDefault();

    let searchRequest = readForm(searchForm);

    console.log('Sending search request with body', searchRequest);

    fetch("/persistence/search", {
        method: 'POST',
        body: JSON.stringify(searchRequest),
    })
        .then(resp => resp.json())
        .then(displaySearchResults)
        .catch(e => console.log("ERROR: " + e));

    return false;
});