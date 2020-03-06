let form = document.querySelector("#signUpForm");
let error = document.querySelector("#error");
let email = document.querySelector("#email");
let password = document.querySelector("#p1");
let repeatPassword = document.querySelector("#p2");

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