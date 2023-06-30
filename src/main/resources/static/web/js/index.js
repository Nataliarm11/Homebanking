const { createApp } = Vue;

const app = createApp({

    data() {
        return {
            email: "",
            password: "",
            emailRegister: "",
            passwordRegister:"",
            firstNameRegister:"",
            lastNameRegister:"",
        }
    },

    methods: {
        logIn() {
            if (this.email && this.password) {
                console.log(this.email);
                console.log(this.password);
                axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' }})
                    .then(response => {
                        console.log("Welcome");
                        window.location.href = '/web/pages/accounts.html';
                    })
                    .catch(error => console.log(error));
            } else {
                Swal.fire({
                    title: "Error",
                    text: "Verify that the email and password are filled out and correct.",
                    icon: "error"
                });
            }
        },
        

        register() {
            if (this.emailRegister && this.passwordRegister && this.firstNameRegister && this.lastNameRegister) {
                console.log(this.emailRegister);
                console.log(this.passwordRegister);
                console.log(this.firstNameRegister);
                console.log(this.lastNameRegister);
                axios.post('/api/clients', `firstName=${this.firstNameRegister}&lastName=${this.lastNameRegister}&email=${this.emailRegister}&password=${this.passwordRegister}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' }})
                    .then(response => {
                        console.log("Registered");
                        axios.post('/api/login', `email=${this.emailRegister}&password=${this.passwordRegister}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' }})
                            .then(response => {
                                console.log(response);
                                window.location.href = '/web/pages/accounts.html';
                            })
                            .catch(error => console.log(error));
                    })
                    .catch(error => console.log(error));
            } else {
                Swal.fire({
                    title: "Error",
                    text: "Check that all fields are filled out",
                    icon: "error"
                });
            }
        },
        



    }
});

app.mount("#app");

  