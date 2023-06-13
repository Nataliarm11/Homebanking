function verificarCredenciales() {

  let usuario = document.getElementById('usser').value;
  let contraseña = document.getElementById('password').value;
  

  if (usuario === 'melba@mindhub.com' && contraseña === '12345') {
    console.log(window.location)
    window.location.href = 'wallet.html';
  } else {
    alert('Credenciales incorrectas. Por favor, inténtalo nuevamente.');
  }
}
  