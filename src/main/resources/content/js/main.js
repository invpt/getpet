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