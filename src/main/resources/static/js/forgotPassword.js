document.getElementById("forgotPasswordForm").addEventListener("submit", async function(e){

    e.preventDefault();

    const email = document.getElementById("email").value.trim();

    const error = document.getElementById("errorMessage");

    error.innerText = "";

    try{

        const response = await fetch(
            `/api/v7/sendResetOtp?email=${encodeURIComponent(email)}`,
            {
                method:"GET"
            }
        );

        if(response.ok){

            sessionStorage.setItem("resetEmail", email);

            alert("OTP sent successfully.");

            window.location.href="/api/v7/view/resetPassword";

        }else{

            error.innerText="Unable to send OTP.";

        }

    }catch(err){

        error.innerText="Something went wrong.";

    }

});