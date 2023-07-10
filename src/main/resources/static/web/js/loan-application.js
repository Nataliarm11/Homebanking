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
      
    };
  },

  created() {
    this.loadData();
    this.loansFunction();

    
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
      console.log(this.loanType, this.amount, this.payment,this.destinationAccountNumber)
        if (confirm('Are you sure about applying for a loan?')) {
            axios.post('/api/loans', {
              loanId: this.loanType,
              amount: this.amount,
              payment: this.payment,
              destinationAccountNumber: this.destinationAccountNumber,
            })
            .then(response => {
                alert('The transfer was successful.');
                location.reload();
            })
            .catch(error => {
              Swal.fire({
                title: "Error",
                text: error.response.data,
                icon: "error"
              });
                console.log(error);

            });
        }
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



                    // this.loansData.forEach(loan => {
                    //     const payments = loan.payments;
                    //     console.log(payments);
