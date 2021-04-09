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
                console.log(checkboxes);
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

    const options = {method: details.method, body: details.body};
    if (!options.method) options.method = 'GET';

    console.info('Performing API call to endpoint', details.endpoint, 'with body', options.body);

    return await (await fetch(`/persistence${details.endpoint}`, options)).json()
}

/**
 * Reports an error by bringing the user to an error page
 * @param {number} code the HTTP response code, or -1 if client error
 * @param {string?} message the error message
 * @param {Error?} error error to log to console
 */
const displayErrorPage = (code, message, error) => {
    // TODO: implement this
    console.warn("Displayed error popup instead of error page");
    displayErrorPopup(code, message, error);
}

/**
 * Reports an error by showing an alert to the user
 * @param {number} code the HTTP response code, or -1 if client error
 * @param {string?} message the error message
 * @param {Error?} error error to log to console
 */
const displayErrorPopup = (code, message, error) => {
    console.error(error);
    let alertMessage = "ERROR" + (code !== -1 ? " (code " + code + ")" : "") + ": " + (message ? message : "Internal error");
    alert(alertMessage);
}