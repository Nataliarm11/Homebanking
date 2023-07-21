const { createApp } = Vue;

const app = createApp ({

    data (){
        return {
            clients:[],
            accounts:[],
            firstName:"",
            lastName:"",
            cardImage:[
                "../assets/images/accountOne.png",
                "../assets/images/accountTwo.png",
                "../assets/images/accountThree.png",
            ],
            activeAccounts:[],
            loans:[],
            payments:[],
            paymentMonthly:null,
            loan: {
                amount: null,
                payments: null,
            },
            accountDelect: 0,
            accountType: "",
        }
    },


    created (){
        this.loadData();
        this.monthly();
        this.accountsActive();
        this.loansGet();

    },

    methods: {
        loadData(){
            axios.get ('/api/clients/current')
            .then ( response => {
                this.clients=response.data;
                console.log(this.clients)


            })
            .catch(error => console.log(error));
        },

        monthly(amount, payments) {
            return (amount / payments).toFixed(2);
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

        loansGet(){
            axios.get(`/api/clients/current/loans`)
                .then(response => {
                    this.loans = response.data.sort((a, b) => a.id - b.id)
                    console.log(this.loans)

                }).catch(err => console.log(err))
        },

        deleteAccount(id){
            Swal.fire({
                title: 'Are you sure to delete this account?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete!'
              }).then((result) => {
                if (result.isConfirmed) {
                    this.accountDelect = id 
                    axios.patch(`/api/accounts/delete?id=${this.accountDelect}`)
                    .then((response) => {
                        Swal.fire({                            
                        position: 'center',
                        icon: 'success',
                        title: 'Your account has been deleted.',
                        showConfirmButton: false,
                        timer: 1500
                    })
                    setTimeout(() => {
                        window.location.reload();
                    }, 1800)
                    })
                    .catch( error => {
                        console.log(error)
                        Swal.fire({
                            position: 'center',
                            title: 'Account cant be deleted, try again!',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    });     
                    
                } else if (result.isDenied){
                    Swal.fire('Account not deleted',  'info')
                }
              })
        },  

        logOut(){
            axios.post(`/api/logout`)
                .then(response => {
                    return window.location.href = "/web/pages/index.html";
                })
                .catch(error => console.log(error));
        }, 

        createAccount(){
            Swal.fire({
              title: 'Select account type',
              input: 'select',
              inputOptions: {
                'SAVINGS': 'SAVINGS',
                'CURRENT': 'CURRENT'
              },
              showCancelButton: true,
              confirmButtonText: 'Create',
              cancelButtonText: 'Cancel',
              icon: 'question'
            }).then((result) => {
              if (result.isConfirmed) {
                const accountType = result.value;
                axios.post(`/api/clients/current/accounts?accountType=${accountType}`)
                  .then(res => {
                    console.log(res);
                    Swal.fire({
                      position: 'center',
                      title: 'Account created successfully!',
                      showConfirmButton: false,
                      timer: 1500
                    })
                    setTimeout(()=>{
                        window.location.href = "accounts.html"
                      },1800)
                  })
                  .catch(err => {
                    console.log(err);
                    Swal.fire('Error creating account', '', 'error');
                  });
              }
            });
        },
        


    },

})
app.mount("#app");


