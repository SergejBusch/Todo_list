const form = document.querySelector('form');
// const url = 'http://localhost:8080/todo/';

form.addEventListener('submit', (e) => {
    e.preventDefault();
    register();
})



async function register() {
    try {
        const config = {
            method: 'POST',
            headers: {
                'Accept': 'application/html',
                'Content-Type': 'application/html',
            },
            body: JSON.stringify({
                email: form.elements.email.value,
                password: form.elements.password.value,
            })
        };
        const response = await fetch('/reg.do', config);
        console.log(form.elements.email.value);
        if (response.ok) {
            form.elements.email.value = '';
            form.elements.password.value = '';
            document.location.href = response.url;
            // await get();
        } else {
            if (response.status >= 400) {
                throw new Error("Bad response from server");
            }
        }
    } catch (e) {
        console.log(e);
    }
}

// async function get() {
//     try {
//         const response = await fetch(url + 'redir.do');
//         if (response.ok) {
//             console.log('get ok');
//             document.location.href = response.url;
//         } else {
//             throw new Error("Bad response from server");
//         }
//     } catch (e) {
//         console.log(e);
//     }
// }
//
//
// Response