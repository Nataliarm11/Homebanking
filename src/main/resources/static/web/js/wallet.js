const { createApp } = Vue;

const app = createApp ({

    data (){
        return {
            clients:[],
            accounts:[],
            firstName:"",
            lastName:"",


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
                 console.log(this.accounts)
                 this.firstName=response.data.firstName;
                 console.log(this.firstName)
                 this.lastName=response.data.lastName;
                 console.log(this.lastName)

            })
            .catch(error => console.log(error));
        }


    },

})
app.mount("#app");
