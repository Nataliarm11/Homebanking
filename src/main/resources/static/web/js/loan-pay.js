const { createApp } = Vue;

const app = createApp ({

    data (){
        return {
            clients:[],
            accounts:[],
            id:0,
            transactions:[],
            activeAccounts:[],
            accountLoanPay: " ",
            account: [],
            loan:{},

        }
    },


    created (){
        //details loans
        console.log(location.search);
        const params = new URLSearchParams (location.search);
        console.log(params);
        this.id = params.get("id");
        console.log(this.id);

        //metod
        this.load();
        this.accountsActive();
        this.loansDetail();


    },

    methods: {
        load(){
            axios.get ('/api/clients/current')
            .then ( response => {
                this.clients=response.data;
                console.log(this.clients)


            })
            .catch(error => console.log(error));
        },
        accountsActive(){
            axios.get('/api/clients/accounts')
            .then(response => {
                this.activeAccounts = response.data;
                this.activeAccounts.sort((a,b)=> a.id - b.id)
                console.log(this.activeAccounts)
            })
            .catch(error => console.log(error));

        },

        pay(){
            Swal.fire({
                title: 'Do you want to make a payment for this loan?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: 'Confirm payment',
                denyButtonText: `Go back`,
            }).then((res) => {
                if (res.isConfirmed) {
                    console.log(this.id, this.accountLoanPay)
                    axios.post(`/api/client/loan/payments?id=${this.id}&account=${this.accountLoanPay}`)
                        .then(res => {
                            Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: `Payment correct`,
                                showConfirmButton: false,
                                timer: 1500
                            })
                            setTimeout(() => {
                                window.location.href = '/web/pages/accounts.html'
                            }, 1900)
                        }).catch(error => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: error.response.data,
                                showConfirmButton: false,
                                timer: 1500
                            })
                        })
                }
            })
        },

        monthly(amount, payments) {
            return (amount / payments).toFixed(2);
        },


        loansDetail() {
            axios.get(`/api/client/loans/${this.id}`)
                .then(response => {
                    this.loan = response.data
                    this.idLoan = response.data.id
                    console.log(this.loan)
                    console.log(this.idLoan)
                }).catch(err => {
                    console.log(err)
                })
        },

        logOut() {
            axios.post(`/api/logout`)
                .then(response => {
                    return window.location.href = "/web/pages/index.html";
                })
                .catch(error => console.log(error));
        },

        divisionResult(amount, payments) {
            const division = amount / payments
            const formatDivision = parseFloat(division.toFixed(2))
            return formatDivision.toLocaleString()
        },



    },


})
app.mount("#app");