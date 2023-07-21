const { createApp } = Vue;
const app = createApp({
    data() {
        return {
            name: "", 
            maxAmount: 0, 
            payments: [], 
            percentageLoan: 0,
        };
    },


    methods: {
        createLoanAndConfirmation() {
            console.log({
                name: this.name,
                maxAmount: this.maxAmount,
                payments: this.payments,
                percentageLoan: this.percentageLoan,
              });
            Swal.fire({
              title: 'Are you sure to create the loan?',
              icon: 'question',
              showCancelButton: true,
              confirmButtonText: 'Apply',
              cancelButtonText: 'Cancel',
            }).then((result) => {
              if (result.isConfirmed) {
                const paymentsArray = this.payments.split(',').map(Number);
                axios.post('/api/loans/admin', {
                  name: this.name,
                  maxAmount: this.maxAmount,
                  payments: paymentsArray,
                  percentageLoan: this.percentageLoan,
                })
                .then(response => {
                  Swal.fire({
                    title: 'Success',
                    text: 'The loan create was successful.',
                    icon: 'success'
                  }).then(() => {
                    window.location.href = '/web/manager.html';
                  });
                })
                .catch(error => {
                    console.log(error.response.data);
                  Swal.fire({
                    title: 'Error',
                    text: error.response.data,
                    icon: 'error'
                  });
                });
              }
            });
        },

        logOut() {
            axios.post(`/api/logout`)
                .then(response => {
                    window.location.href = "../pages/index.html";
                })
                .catch(error => console.log(error));
        }
    },
});

app.mount("#app");
