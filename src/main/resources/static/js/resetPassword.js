document.getElementById("resetPasswordForm").addEventListener("submit", async function (e) {

    e.preventDefault();

    const otp = [...document.querySelectorAll(".otp-box")]
        .map(input => input.value)
        .join("");

    const newPassword = document.getElementById("newPassword").value.trim();
    const confirmPassword = document.getElementById("confirmPassword").value.trim();
    const email = sessionStorage.getItem("resetEmail");

    const error = document.getElementById("errorMessage");
    error.innerText = "";

    if (otp.length !== 6) {
        error.innerText = "Enter a valid OTP.";
        return;
    }

    if (newPassword.length < 6) {
        error.innerText = "Password must be at least 6 characters.";
        return;
    }

    if (newPassword !== confirmPassword) {
        error.innerText = "Passwords do not match.";
        return;
    }

    try {

        const response = await fetch("/api/v7/resetPassword", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email,
                otp: otp,
                newPassword: newPassword
            })

        });

        if (response.ok) {

            alert("Password reset successful!");

            sessionStorage.removeItem("resetEmail");

            window.location.href = "/api/v7/view/login";

        } else {

            error.innerText = "Invalid OTP or unable to reset password.";

        }

    } catch (err) {

        error.innerText = "Something went wrong.";

    }

});