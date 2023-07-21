const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            clients: [],
            cards: [],
            debitCards: [],
            creditCards: [],
            cardDelect: 0,
            cardRenew: 0,
          };
        
    },

    created() {
        this.loadData();
    },
    
    
    methods: {
        loadData() {
            axios.get('/api/clients/current/cards')
            .then((response) => {
                this.cards = response.data;
                this.cards.sort((a, b) => a.id - b.id);
                console.log(this.cards);

                this.debitCards = this.cards.filter((card) => card.type === 'DEBIT');
                console.log(this.debitCards);
                
                this.creditCards = this.cards.filter((card) => card.type === 'CREDIT');
                console.log(this.creditCards);


                this.cards.forEach((card) => {
                    let currentDate = new Date();
                    card.cardClass = '';
                    if (card.color === 'SILVER') {
                      if (currentDate < new Date(card.thruDate)) {
                        card.cardClass = 'silver';
                      } else {
                        card.cardClass = 'cardExpired';
                      }
                    } else if (card.color === 'GOLD') {
                      if (currentDate < new Date(card.thruDate)) {
                        card.cardClass = 'gold';
                      } else {
                        card.cardClass = 'cardExpired';
                      }
                    } else if (card.color === 'TITANIUM') {
                      if (currentDate < new Date(card.thruDate)) {
                        card.cardClass = 'titanium';
                      } else {
                        card.cardClass = 'cardExpired';
                      }
                    }
                  });
                                  
            })
            
            .catch((error) => console.log(error));
        },

        deleteCard(id){
            Swal.fire({
                title: 'Are you sure to delete this card?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete!'
              }).then((result) => {
                if (result.isConfirmed) {
                    this.cardDelect = id 
                    axios.patch(`/api/clients/current/cards?id=${this.cardDelect}`)
                    .then((response) => {
                        Swal.fire({                            
                        position: 'center',
                        icon: 'success',
                        title: 'Your card has been deleted.',
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
                            title: 'Card cant be deleted, try again!',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    });     
                    
                } else if (result.isDenied){
                    Swal.fire('Card not deleted',  'info')
                }
              })
        },

        renewCardButton(thruDate) {
          const currentDate = new Date();
          const formattedThruDate = new Date(thruDate);
        
          return formattedThruDate < currentDate;
        },

        renewCard(id) {
          this.cardRenew = id;
          Swal.fire({
            title: 'Are you sure?',
            text: 'Do you want to renew this card?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'Cancel',
            reverseButtons: true
          }).then((result) => {
            if (result.isConfirmed) {
              axios.post(`/api/clients/cards/renew?id=${this.cardRenew}`)
                .then(response => {
                  Swal.fire('Renewal Successful', 'The card has been renewed successfully.', 'success').then(() => {
                    window.location.reload();
                  });
                })
                .catch(error => {
                  Swal.fire('Error', 'An error occurred while renewing the card.', 'error');
                  console.log(error);
                });
            }
          });
        },
        
        logOut() {
            axios.post(`/api/logout`)
                .then(response => {
                    return window.location.href = "/web/pages/index.html";
                })
                .catch(error => console.log(error));
        }
    },
});

app.mount("#app");


