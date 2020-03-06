let form = document.getElementById("signUpForm");
let error = document.getElementById("error");
let email = document.getElementById("email");
let password = document.getElementById("password");
let repeatPassword = document.getElementById("repeatPassword");

function validateForm() {
    if (email.value === "") {
        error.innerHTML = "Please enter email";
    } else if (password.value === "") {
        error.innerHTML = "Please enter password";
    } else if (password.value !== repeatPassword.value) {
        repeatPassword.value = "";
        error.innerHTML = "Passwords do not match";
    } else {
        form.submit();
    }
}

document.getElementById("submitButton").onclick = validateForm;