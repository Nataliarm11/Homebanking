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
            accountsActiv:[],
            
        };
    },

    created() {
        this.loadData();
        this.accountsActive();
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

        accountsActive() {
            axios.get('/api/clients/accounts')
              .then(response => {
                this.accountsActiv = response.data;
                this.accountsActiv.sort((a, b) => a.id - b.id);
                console.log(this.accountsActiv);
              })
              .catch(error => console.log(error));
          },

          createTransaction() {
            Swal.fire({
              title: 'Are you sure?',
              text: 'Are you sure to make the transfer?',
              icon: 'warning',
              showCancelButton: true,
              confirmButtonText: 'Yes',
              cancelButtonText: 'Cancel',
              reverseButtons: true
            }).then((result) => {
              if (result.isConfirmed) {
                this.newTransaction(this.amount, this.description, this.originNumber, this.destinationNumber);
              }
            });
          },
          
          newTransaction(amount, description, originNumber, destinationNumber) {
            axios.post('/api/transactions', {
              amount: amount,
              description: description,
              originNumber: originNumber,
              destinationNumber: destinationNumber
            })
            .then(response => {
              Swal.fire('Success', 'The transfer was successful.', 'success').then(() => {
                window.location.href = '/web/pages/accounts.html';
              });
            })
            .catch(error => {
              Swal.fire({
                title: 'Error',
                text: error.response.data,
                icon: 'error'
              });
              console.log(error);
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


