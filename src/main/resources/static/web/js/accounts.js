const { createApp } = Vue;

const app = createApp ({

    data (){
        return {
            clients:[],
            accounts:[],
            firstName:"",
            lastName:"",
            cardImage:[
                "../assets/images/cardOne (2).png",
                "../assets/images/cardTwo (2).png"
            ],
            loans:[],
            payments:[],
        }
    },


    created (){
        this.loadData();

    },

    methods: {
        loadData(){
            axios.get ('http://localhost:8080/api/clients/1')
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
        }


    },

})
app.mount("#app");


