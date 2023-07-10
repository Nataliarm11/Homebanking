const { createApp } = Vue;

const app = createApp ({

    data (){
        return {
            clients:[],
            accounts:[],
            firstName:"",
            lastName:"",
            cardImage:[
                "../assets/images/firstAccount.png",
                "../assets/images/secondAccount.png",
                "../assets/images/thirdAccount.png",
            ],
            loans:[],
            payments:[],
            paymentMonthly:null,
            loan: {
                amount: null,
                payments: null,
              }
        }
    },


    created (){
        this.loadData();
        this.monthly();

    },

    methods: {
        loadData(){
            axios.get ('http://localhost:8080/api/clients/current')
            .then ( response => {
                this.clients=response.data;
                console.log(this.clients)
                this.accounts=response.data.accounts;
                this.accounts.sort((a,b)=> a.id - b.id)
                console.log(this.accounts)
                this.loans=response.data.loans;
                console.log(this.loans);
                this.payments = response.data.loans[0].payments;
                console.log(this.payments)

            })
            .catch(error => console.log(error));
        },

        monthly(amount, payments) {
            return (amount / payments).toFixed(2);
          },

        logOut() {
            axios.post(`/api/logout`)
                .then(response => {
                    return window.location.href = "/web/pages/index.html";
                })
                .catch(error => console.log(error));
        },

        createAccount(){
            axios.post(`/api/clients/current/accounts`)
            .then(response => {
                this.loadData();
            })
            .catch(error=> console.log(error));
        }


    },

})
app.mount("#app");


