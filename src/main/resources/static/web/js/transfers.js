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
                    console.log(this.accounts)
                })
                .catch(error => console.log(error));
        },

        createTransaction() {
            if (confirm('Are you sure to make the transfer?')) {
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
                
                alert('The transfer was successful.');
                return (window.location.href = '/web/pages/accounts.html');
                
              })
              .catch(error => {
                console.log(error);
                Swal.fire({
                    title: "Error",
                    text: error.response.data, 
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


