const btnAdd = document.querySelector('.btn-add-task');
const taskLabel = document.querySelector('.task-desc');
const tasksContainer = document.querySelector('.tasks-container');
const completeShowCheckbox = document.querySelector('.form-check-input');
const form = document.querySelector('form');
const parName = document.querySelector('.name');
const logout = document.querySelector('.logout');
const selectGroup = document.querySelector('.opt-group');
const formSelect = document.querySelector('.form-select');
let tasksArray = [];
let categoriesArray = [];

completeShowCheckbox.addEventListener('change', function() {
    if (this.checked) {
        getAllTasks(true);
    } else {
       getAllTasks(false);
    }
});

tasksContainer.addEventListener("contextmenu", (event) => {
    event.preventDefault();
});

formSelect.addEventListener('click', evt => {
    console.log(formSelect.selectedOptions)
});

logout.addEventListener('click', () => {
    logOut();
});

tasksContainer.addEventListener("mousedown", (event) => {
    event.preventDefault();
    // if (event.button === 2) {
        console.log('mouse2');
        console.log(event.target);
        if (event.target.classList.contains('task-todo')) {
            event.target.classList.toggle('line-thr');
        }
        if (event.target.classList.contains('line-thr')) {
            event.target.dataset.done = true;
            post(event.target);
        } else {
            event.target.dataset.done = false;
            post(event.target);
        }
    // }
});

btnAdd.addEventListener('click', ()=> submitAction());

form.addEventListener('submit', (e) => {
    e.preventDefault();
    submitAction();
});

function submitAction() {
    if (taskLabel.value == '') {
        alert('Please enter a description')
    } else {
        post(null);
        taskLabel.value = '';
    }
}

async function post(obj) {
    try {
        if (formSelect.selectedOptions.length < 1) {
            alert('Please select Category');
            return;
        }
        let task = {
            id: 0,
            name: taskLabel.value.split(' ')[0],
            description: taskLabel.value,
            done: false,
            categories: [],
        };
        let selects = [...formSelect.selectedOptions]
            .map(option => option.value);
        if (obj != null) {
            task.id = parseInt(obj.dataset.id);
            task.name = obj.dataset.name;
            task.description = obj.dataset.desc;
            task.done = obj.dataset.done;
        }
        const config = {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: task.id,
                name: task.name,
                description: task.description,
                done: task.done,
                categories: selects,
            })
        };
        const response = await fetch('/to.do', config);
        if (response.ok) {
            console.log('response ok')
            getAllTasks(completeShowCheckbox.checked);
        } else {
            if (response.status >= 400) {
                throw new Error("Bad response from server");
            }
        }
    } catch (e) {
        console.log(e);
    }
}

function setName(uName) {
    parName.innerHTML = `Logged in as ${uName}`;
}

function logOut() {
    asyncLogOut();
}

async function asyncLogOut() {
    try {
        const response = await fetch('/logout');
        if (response.ok) {
            document.location.href = response.url;
        } else {
            throw new Error("Bad response from server");
        }
    } catch (e) {
        console.log(e);
    }
}

async function getAllCategories() {
    try {
        const response = await fetch('/categories.do');
        if (response.ok) {
            categoriesArray = [];
            const categories = await response.json();
            console.log(categoriesArray);
            categoriesArray.push(...categories);
            addCategories();
            getAllTasks(completeShowCheckbox.checked);
        } else {
            throw new Error("Bad response from server");
        }
    } catch (e) {
        console.log(e);
    }
}

function addCategories() {
    selectGroup.innerHTML = '';
    console.log(categoriesArray);
    categoriesArray.forEach(c => {
        selectGroup.innerHTML += `<option value="${c.id}">${c.name}</option>`;
    });
}

async function getAllTasks(all) {
    try {
        const response = await fetch('/to.do?' + new URLSearchParams(`all=${all}`));
        if (response.ok) {
            tasksArray = [];
            const tasks = await response.json();
            tasksArray.push(...tasks[0]);
            setName(tasks[1]);
            showAllTasks();
        } else {
            throw new Error("Bad response from server");
        }
    } catch (e) {
        console.log(e);
    }
}

function showAllTasks() {
    tasksContainer.innerHTML = '';
    tasksArray.forEach((e) => {
        const div = document.createElement('div');
        div.classList.add('d-flex', 'flex-row',
            'justify-content-center',
            'align-items-center',
            'mx-auto',
            'align-items-xxl-center',
            'one-task-container');

        let span = document.createElement('span');
        span.classList.add('task-todo');
        if (e.done == true) {
            span.classList.add('line-thr');
        }
        span.dataset.id = e.id;
        span.dataset.name = e.name;
        span.dataset.desc = e.description;
        span.dataset.done = e.done;
        span.innerHTML = e.description;
        div.append(span);
        tasksContainer.append(div);
    });
}

getAllCategories();


