const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            clients: [],
            cards: [],
            debitCards: [],
            creditCards: [],
          };
        
    },

    created() {
        this.loadData();
    },
    
    
    methods: {
        loadData() {
            
            axios.get('http://localhost:8080/api/clients/1')
            .then((response) => {
                this.clients = response.data;
                console.log(this.clients);

                this.cards = response.data.cards;
                this.cards.sort((a, b) => a.id - b.id);

                this.debitCards = this.cards.filter((card) => card.type === 'DEBIT');
                console.log(this.debitCards);
                
                this.creditCards = this.cards.filter((card) => card.type === 'CREDIT');
                console.log(this.creditCards);
                
                this.cards.forEach((card) => {
                    if (card.color === 'GOLD') {
                        card.cardClass = 'gold';
                    } else if (card.color === 'TITANIUM') {
                        card.cardClass = 'titanium';
                    } else if (card.color === 'SILVER') {
                        card.cardClass = 'silver';
                    }
                });
                
                console.log(this.cards);
            })
            
            .catch((error) => console.log(error));
        },
    },
});

app.mount("#app");


