const form = document.getElementById("searchForm");

form.addEventListener('submit', ev => {
    onFormSubmit();

    ev.preventDefault();
    return false;
});

function onFormSubmit() {
    let searchRequest = {
        species: form.elements["species"].value,
        gender: checkboxFormElement(form.elements["gender"]),
        breed: form.elements["breed"].value,
        color: checkboxFormElement(form.elements["color"]),
        size: checkboxFormElement(form.elements["size"]),
    };

    fetch("/persistence/search", {
        method: 'POST',
        body: JSON.stringify(searchRequest),
    }).then(resp => console.log(resp))
    .catch(e => console.log(e));
}

function checkboxFormElement(element) {
    console.log(element);
    let arr = [];
    for (cb of element)
        if (cb.checked)
            arr.push(cb.value);
    return arr;
}