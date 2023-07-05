const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            selectedAccount: '',
            transferType: 'own',
            originNumber: '',
            destinationNumber: '',
            accountNumber: '',
            amount: '',
            description: '',
            
        };
    },

    created() {
        this.loadData();
    },

    methods: {
        loadData() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.clients = response.data;
                    this.accounts = this.clients.accounts;
                    this.accounts.sort((a, b) => a.id - b.id);
                })
                .catch(error => console.log(error));
        },

        createTransaction() {
            if (confirm('¿Estás seguro de realizar la transferencia?')) {
                this.newTransaction(this.amount, this.description, this.originNumber, this.destinationNumber);
            }
        },
        
        newTransaction(amount, description, originNumber, destinationNumber) {
            axios.post('/api/transactions', {
                amount: amount,
                description: description,
                originNumber: originNumber,
                destinationNumber: destinationNumber,

            })
              .then(response => {
                // Maneja la respuesta si es necesario
                alert('La transferencia se realizó correctamente.');
                location.reload();
              })
              .catch(error => {
                console.log(error);
                Swal.fire({
                    title: "Error",
                    text: error.response.data, // Muestra el mensaje de error específico
                    icon: "error"
                  });
              });
        },

        logOut() {
            axios
                .post('/api/logout')
                .then((response) => {
                    return (window.location.href = '/web/pages/index.html');
                })
                .catch((error) => console.log(error));
        },
    },
});

app.mount('#app');


