const readableValues = {
    species: {
        dog: 'Dog',
        cat: 'Cat',
    },
    size: {
        small: 'Small',
        medium: 'Medium',
        large: 'Large',
    },
    color: {
        black: 'Black',
        white: 'White',
        brown: 'Brown',
        gold: 'Gold',
        dGray: 'Dark Gray',
        lGray: 'Light Gray',
    },
    gender: {
        m: 'Male',
        f: 'Female',
    },
};

const readForm = form => {
    const values = {};

    for (const option of form.querySelectorAll('.option')) {
        const name = option.getAttribute('name');
        const type = option.getAttribute('type');

        switch (type) {
            case 'radio':
                values[name] = form.elements[name].value !== '' ? form.elements[name].value : null;
                break;
            case 'checkbox':
                const checkboxes = option.querySelectorAll('input');
                if (checkboxes.length > 1) {
                    values[name] = [];
                    for (const checkbox of option.querySelectorAll('input'))
                        if (checkbox.checked)
                            values[name].push(checkbox.value);
                } else {
                    values[name] = checkboxes.length != 0;
                }
                break;
            default:
                values[name] = option.querySelector('input').value;
        }
    }

    return values;
};

const hasPrivilegeLevel = level => {
    const role = window.sessionStorage.getItem('role');

    switch (level) {
        case 'any':
            return role !== null && role !== undefined;
        case 'assistant':
            return role === 'assistant'
                || role === 'director';
        case 'director':
            return role === 'director';
        default:
            console.error("unknown privilege level", role);
            return false;
    }
}

const requirePrivilegeLevel = level => {
    if (!hasPrivilegeLevel(level))
        // TODO: ok this could potentially be more advanced
        window.location.href = '/login.html';
};

const apiCall = async (details) => {
    if (!details.endpoint) {
        throw Error("apiCall requires endpoint");
    }

    const options = {method: details.method, body: JSON.stringify(details.body)};
    if (!options.method) options.method = 'GET';

    console.info('Performing API call to endpoint', details.endpoint, 'with body', options.body);

    let response = await fetch(`/persistence${details.endpoint}`, options);
    let json = await response.json();

    if (response.status >= 200 && response.status < 300)
        return json
    else {
        let error = new Error(`Error ${response.status}: ${response.statusText}`);
        error.responseJson = json;
        throw error;
    }
}

/**
 * Reports an error by showing an alert to the user
 * @param {string?} message the error message
 * @param {Error?} cause error to log to console
 */
const displayError = (message, cause) => {
    cause && console.error(cause);

    if (cause.responseJson) {
        alert(`Error: ${message} - ${cause.responseJson.message} (${cause.message})`);
    } else {
        alert(`Error: ${message}`);
    }
}