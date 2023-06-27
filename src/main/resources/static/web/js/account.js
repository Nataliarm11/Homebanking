const { createApp } = Vue;

const app = createApp ({

    data (){
        return {
            accounts:[],
            id:[],
            transactions:[],

        }
    },


    created (){
        //details transactions
        console.log(location.search);
        const params = new URLSearchParams (location.search);
        console.log(params);

        this.id = params.get("id");
        console.log(this.id);

        //metod
        this.loadData();


    },

    methods: {
        loadData(){
             axios.get (`http://localhost:8080/api/accounts/${this.id}`)
             .then ( response => {
                 this.accounts=response.data;
                 console.log(this.accounts)

                 this.transactions=response.data.transactions;
                 console.log(this.transactions);


    


             })
            .catch(error => console.log(error));
        },

        logOut() {
            axios.post(`/api/logout`)
                .then(response => {
                    return window.location.href = "/web/pages/index.html";
                })
                .catch(error => console.log(error));
        }



    },

})
app.mount("#app");