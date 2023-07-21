const { createApp } = Vue;

const app = createApp({
  data() {
    return {
      clients: [],
      accounts: [],
      payments: [],
      payment: '',
      paymentLoans : [],
      loansData: [],
      loanType: null,
      destinationAccountNumber: '',
      amount: null,
      loanId:null,
      accountsActiv:[],
      
    };
  },

  created() {
    this.loadData();
    this.loansFunction();
    this.accountsActive();

    
  },

  methods: {
    loadData() {
      axios.get('/api/clients/current')
        .then(response => {
          this.clients = response.data;
          this.accounts = this.clients.accounts;
          this.accounts.sort((a, b) => a.id - b.id);
          console.log(this.accounts);
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


    loansFunction() {
      axios.get('/api/loans')
        .then(response => {
          this.loansData = response.data;
          console.log(this.loansData);
          this.payment = this.loansData.filter(loan => loan.id == this.loanType)
          console.log(this.loanType)
          console.log(this.payment)
          this.paymentLoans = this.payment[0].payments
          console.log(this.paymentLoans)
          
          
        })
        .catch(error => console.log(error));
    },



    createLoans() {
      console.log(this.loanType, this.amount, this.payment, this.destinationAccountNumber);
  
      Swal.fire({
        title: 'Are you sure?',
        text: 'Are you sure about applying for a loan?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes',
        cancelButtonText: 'Cancel',
        reverseButtons: true
      }).then((result) => {
        if (result.isConfirmed) {
          axios.post('/api/loans', {
            loanId: this.loanType,
            amount: this.amount,
            payment: this.payment,
            destinationAccountNumber: this.destinationAccountNumber,
          })
          .then(response => {
            Swal.fire('Success', 'The loan application was successful.', 'success').then(() => {
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
        }
      });
    },
    

    logOut() {
      axios.post('/api/logout')
        .then((response) => {
          return (window.location.href = '/web/pages/index.html');
        })
        .catch((error) => console.log(error));
    },
  },

  computed: {
    divisionResult() {
      const amountWithInterest = this.amount * 1.2; 
      return amountWithInterest / this.payment;
    }
  },
});

app.mount('#app');
