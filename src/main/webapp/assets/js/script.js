const btnAdd = document.querySelector('.btn-add-task');
const taskLabel = document.querySelector('.task-desc');
const tasksContainer = document.querySelector('.tasks-container');
const completeShowCheckbox = document.querySelector('.form-check-input');
const form = document.querySelector('form');
const url = 'http://localhost:8080/todo/';
let tasksArray = [];

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

tasksContainer.addEventListener("mousedown", (event) => {
    event.preventDefault();
    if (event.button === 2) {
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
    }
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
        let task = {
            id: 0,
            name: taskLabel.value.split(' ')[0],
            description: taskLabel.value,
            done: false,
        };
        if (obj != null) {
            console.log(obj.dataset.id);
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
            })
        };
        const response = await fetch(url + 'todoServlet', config);
        if (response.ok) {
            console.log('response ok')
            getAllTasks(completeShowCheckbox.checked);
        } else {
            if (response.status >= 400) {
                throw new Error("Bad response from server");
            }
        }
    } catch (e) {
        console.error(e);
    }
}

async function getAllTasks(all) {
    try {
        const response = await fetch(url + 'todoServlet');
        if (response.ok) {
            tasksArray = [];
            const tasks = await response.json();
            tasksArray.push(...tasks);
            console.log(tasksArray);
            showAllTasks(all);
        } else {
            throw new Error("Bad response from server");
        }
    } catch (e) {
        console.error(e);
    }
}

function showAllTasks(all) {
    tasksContainer.innerHTML = '';
    let array;
    if (all) {
        array = tasksArray;
    } else {
        array = tasksArray.filter((e) => e.done == false);
    }
    array.forEach((e) => {
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

getAllTasks(completeShowCheckbox.checked);