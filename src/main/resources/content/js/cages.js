requirePrivilegeLevel('any');

const fillCages = cages => {
    for (const cage of cages.cages) {
        const cageDiv = document.createElement('div');
        cageDiv.classList.add('cage');

        const cageNumberDiv = document.createElement('div');
        cageNumberDiv.classList.add('cageNumber');
        cageNumberDiv.innerText = `Cage ${cage.cageNumber}`;
        cageDiv.appendChild(cageNumberDiv);

        const cageSepDiv = document.createElement('div');
        cageSepDiv.classList.add('cageSep');
        cageDiv.appendChild(cageSepDiv);

        const cageAnimalsDiv = document.createElement('div');
        cageAnimalsDiv.classList.add('cageAnimals');
        if (cage.dogCount == 0 && cage.catCount == 0)
            cageAnimalsDiv.innerText = 'empty';
        else if (cage.dogCount == 0)
            cageAnimalsDiv.innerText = `${cage.catCount} cat${cage.catCount != 1 ? 's' : ''}`;
        else if (cage.catCount == 0)
            cageAnimalsDiv.innerText = `${cage.dogCount} dog${cage.dogCount != 1 ? 's' : ''}`;
        else
            cageAnimalsDiv.innerText = `${cage.dogCount} dog${cage.dogCount != 1 ? 's' : ''}, ${cage.catCount} cat${cage.catCount != 1 ? 's' : ''}`;
        cageDiv.appendChild(cageAnimalsDiv);

        const viewAnimalsButton = document.createElement('button');
        viewAnimalsButton.classList.add('button', 'submit', 'viewAnimals');
        viewAnimalsButton.innerText = 'View Animals';
        viewAnimalsButton.onclick = () => {
            window.location = window.location.origin
                + '/search.html'
                + `?cageNumber=${cage.cageNumber}`;            
        };
        cageDiv.appendChild(viewAnimalsButton);

        document.getElementById('main').appendChild(cageDiv);
    }
}

apiCall({ endpoint: '/cages' })
.then(fillCages)
.catch(e => displayError('failed to get cages', e));