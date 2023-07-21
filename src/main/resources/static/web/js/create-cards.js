const { createApp } = Vue;

const app = createApp({
  data() {
    return {
      cardType: "",
      cardColor: "",
    };
  },

  methods: {
    createCard() {
      this.newCard(this.cardType, this.cardColor);
    },
    
    newCard(cardType, cardColor) {
      axios.post("/api/clients/current/cards", `cardType=${cardType}&cardColor=${cardColor}`)
        .then(response => {
          window.location.href = "/web/pages/cards.html";
        })
        .catch(error => {
          console.log(error);
          Swal.fire({
            title: "Error",
            text: "Sorry, remember that you can have three cards per type (Debit or credit) and one per color (Gold, Titanium, and Silver).",
            icon: "error"
          });
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


