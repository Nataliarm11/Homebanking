const { createApp } = Vue;
const app = createApp({
    data() {
        return {
            clients: [],
            json: [],
            clientNew: {
                firstName: "",
                lastName: "",
                email: "",
            },
        };
    },

    created() {
        this.loadData();
    },

    methods: {
        loadData() {
            axios.get('http://localhost:8080/clients')
                .then(response => {
                    console.log(response);
                    this.clients = response.data._embedded.clients;
                    console.log(this.clients);
                    this.json = response.data;
                    console.log(this.json);
                })
                .catch(error => console.log(error));
        },

        addClient() {
            if (this.clientNew.firstName && this.clientNew.lastName && this.clientNew.email) {
                this.postClient();
            } else {
                alert("Please fill in the fields correctly");
            }
        },

        postClient() {
            axios.post('http://localhost:8080/clients', this.clientNew)
                .then(() => {
                    this.loadData();
                    this.clearInputFields(); 
                })
                .catch(error => console.log(error));
        },

        deleteClient(id) {
            axios.delete(id)
                .then(() => {
                    this.loadData();
                })
                .catch(error => console.error(error));
        },

        clearInputFields() {
            this.clientNew = {
                firstName: "",
                lastName: "",
                email: "",
            };
        },
    },
});

app.mount("#app");
