const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            email: "",
            password: "",
            emailRegister: "",
            passwordRegister: "",
            firstNameRegister: "",
            lastNameRegister: "",
        };
    },

    methods: {
        logIn() {
            if (this.email && this.password) {
                if ( this.email.includes("admin")) {
                    axios.post(
                        "/api/login",
                        `email=${this.email}&password=${this.password}`,
                        { headers: { 'content-type': 'application/x-www-form-urlencoded' } }
                    )
                        .then(res => {
                            if (res.status == 200) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                                setTimeout(() => {
                                    window.location.href = "/web/manager.html";
                                }, 1800);
                            }
                        })
                        .catch(err => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Incorrect, try again!!',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        });
                } else {
                    axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' }})
                        .then(response => {
                            Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: 'Welcome!',
                                showConfirmButton: false,
                                timer: 1000
                            });
                            setTimeout(() => {
                                window.location.href = "/web/pages/accounts.html";
                            }, 1800);
                        })
                        .catch(err => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Incorrect, try again!!',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        });
                }
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
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1000
                                });
                                setTimeout(() => {
                                    window.location.href = "/web/pages/accounts.html";
                                }, 1800);
                            })
                            .catch(err => {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'error',
                                    title: 'Incorrect, try again!!',
                                    showConfirmButton: false,
                                    timer: 1500
                                });
                            });
                    })
                    .catch(err => {
                        Swal.fire({
                            position: 'center',
                            icon: 'error',
                            title: 'Incorrect, try again!!',
                            showConfirmButton: false,
                            timer: 1500
                        });
                    });
            }
        }
    }
});

app.mount("#app");


  